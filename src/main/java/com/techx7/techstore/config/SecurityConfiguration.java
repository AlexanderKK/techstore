package com.techx7.techstore.config;

import com.techx7.techstore.repository.UserRepository;
import com.techx7.techstore.service.impl.TechstoreUserDetailsServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.crypto.password.Pbkdf2PasswordEncoder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfiguration {

    private final String rememberMeKey;

    @Autowired
    public SecurityConfiguration(@Value("${techstore.rememberme.key}") String rememberMeKey) {
        this.rememberMeKey = rememberMeKey;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception {
        return httpSecurity.authorizeHttpRequests(authorizeRequests -> authorizeRequests
                .requestMatchers(PathRequest.toStaticResources().atCommonLocations()).permitAll()
                .requestMatchers(
                        "/", "/users/login", "/users/register", "/users/login-error", "/products")
                .permitAll()
                .requestMatchers("/products/manage/**").hasRole("MANAGER")
                .requestMatchers("/manufacturers/manage/**").hasRole("MANAGER")
                .requestMatchers("/models/manage/**").hasRole("MANAGER")
                .requestMatchers("/categories/manage/**").hasRole("MANAGER")
                .anyRequest().authenticated()
        ).formLogin(formLogin -> formLogin
                    .loginPage("/users/login")
                    .usernameParameter("username")
                    .passwordParameter("password")
                    .defaultSuccessUrl("/", true)
                    .failureForwardUrl("/users/login-error")
        ).logout(logout -> logout
                    .logoutUrl("/users/logout")
                    .logoutSuccessUrl("/")
                    .invalidateHttpSession(true)
//        ).exceptionHandling(exceptionHandling -> exceptionHandling
//                .authenticationEntryPoint(authenticationEntryPoint())
        ).rememberMe(rememberMe -> rememberMe
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
