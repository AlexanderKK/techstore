package com.techx7.techstore.config;

import com.techx7.techstore.service.interceptors.RegistrationInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class InterceptorConfiguration implements WebMvcConfigurer {

    private final RegistrationInterceptor registrationInterceptor;

    @Autowired
    public InterceptorConfiguration(RegistrationInterceptor registrationInterceptor) {
        this.registrationInterceptor = registrationInterceptor;
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(registrationInterceptor)
                .addPathPatterns("/users/register")
                .order(1);
    }

}
