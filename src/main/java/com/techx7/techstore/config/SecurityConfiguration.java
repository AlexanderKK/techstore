package com.techx7.techstore.config;

import com.techx7.techstore.config.csrf.CsrfRequestMatcher;
import com.techx7.techstore.repository.UserRepository;
import com.techx7.techstore.service.impl.TechstoreUserDetailsServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.crypto.password.Pbkdf2PasswordEncoder;
import org.springframework.security.web.AuthenticationEntryPoint;
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
                        // User
                        .requestMatchers(PathRequest.toStaticResources().atCommonLocations()).permitAll()
                        .requestMatchers(
                                "/users/login",
                                "/users/login-error",
                                "/users/login-rest",
                                "/users/activate/**",
                                "/users/register",
                                "/users/register/success").permitAll()
                        .requestMatchers("/error").permitAll()
                        .requestMatchers("/", "/products").permitAll()
                        .requestMatchers("/", "/contact").permitAll()
                        // Manager
                        .requestMatchers("/products/manage/**").hasRole("MANAGER")
                        .requestMatchers("/manufacturers/**").hasRole("MANAGER")
                        .requestMatchers("/models/manage/**").hasRole("MANAGER")
                        .requestMatchers("/categories/manage/**").hasRole("MANAGER")
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
                ).rememberMe(rememberMe -> rememberMe
                        .key(rememberMeKey)
                        .rememberMeParameter("rememberme")
                        .rememberMeCookieName("rememberme")
                ).csrf(csrf -> csrf
                        .requireCsrfProtectionMatcher(matcher -> new CsrfRequestMatcher().buildMatcher(matcher))
                        .csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse())
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
