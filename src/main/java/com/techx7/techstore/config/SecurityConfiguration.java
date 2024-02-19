package com.techx7.techstore.config;

import com.techx7.techstore.config.csrf.CsrfRequestMatcher;
import com.techx7.techstore.repository.UserRepository;
import com.techx7.techstore.service.impl.TechstoreUserDetailsServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.actuate.autoconfigure.security.servlet.EndpointRequest;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.crypto.password.Pbkdf2PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;

@Configuration
public class SecurityConfiguration {

    private final String rememberMeKey;

    @Autowired
    public SecurityConfiguration(@Value("${techstore.rememberme.key}") String rememberMeKey) {
        this.rememberMeKey = rememberMeKey;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception {
        return httpSecurity
                .headers(headers -> headers
                        .frameOptions(HeadersConfigurer.FrameOptionsConfig::sameOrigin)
                ).authorizeHttpRequests(authorizeRequests -> authorizeRequests

                        // Anonymous
                        .requestMatchers(PathRequest.toStaticResources().atCommonLocations()).permitAll()
                        .requestMatchers(
                                "/users/login",
                                "/users/login-error",
                                "/users/login-rest",
                                "/users/activate/**",
                                "/users/register",
                                "/users/register/success").permitAll()
                        .requestMatchers("/error").permitAll()
                        .requestMatchers("/", "/products", "/contact").permitAll()
                        .requestMatchers("/products/detail/**").permitAll()

                        .requestMatchers(
                                "/cart/add/**",
                                "/cart/update/**"
                        ).permitAll()

                        // Any role
                        .requestMatchers(
                                "/users/profile/**",
                                "/users/credentials/**",
                                "/users/password/**"
                        ).hasAnyRole("CARRIER", "USER", "SUPPORT", "MANAGER", "ADMIN")

                        // User
                        .requestMatchers(
                                "/cart/add/**",
                                "/cart/update/**",
                                "/cart/remove/**",
                                "/cart/load"
                        ).hasRole("USER")

                        // Manager
                        .requestMatchers("/products/**").hasRole("MANAGER")
                        .requestMatchers("/manufacturers/**").hasRole("MANAGER")
                        .requestMatchers("/models/**").hasRole("MANAGER")
                        .requestMatchers("/categories/**").hasRole("MANAGER")

                        // Admin
                        .requestMatchers("/users/**").hasRole("ADMIN")
                        .requestMatchers("/roles/**").hasRole("ADMIN")
                        .requestMatchers(EndpointRequest.toAnyEndpoint()).hasRole("ADMIN")
                        .requestMatchers("/seeds/**").hasRole("ADMIN")

                        .anyRequest().authenticated()

                ).formLogin(formLogin -> formLogin
                        .loginPage("/users/login")
                        .usernameParameter("emailOrUsername")
                        .passwordParameter("password")
                        .defaultSuccessUrl("/", true)
                        .failureForwardUrl("/users/login-error")
                ).logout(logout -> logout
                        .logoutUrl("/users/logout")
                        .logoutSuccessUrl("/")
                        .invalidateHttpSession(true)
                ).csrf(csrf -> csrf
                        .requireCsrfProtectionMatcher(matcher -> new CsrfRequestMatcher().buildMatcher(matcher))
                        .csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse())
                ).rememberMe(rememberMe -> rememberMe
                        .alwaysRemember(true)
                        .key(rememberMeKey)
                        .rememberMeParameter("rememberme")
                        .rememberMeCookieName("rememberme")
                ).build();
    }

    @Bean
    public UserDetailsService userDetailService(UserRepository userRepository) {
        return new TechstoreUserDetailsServiceImpl(userRepository);
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return Pbkdf2PasswordEncoder.defaultsForSpringSecurity_v5_8();
    }

}
