package com.techx7.techstore.config;

import com.techx7.techstore.service.interceptors.LogHandlerInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class InterceptorConfiguration implements WebMvcConfigurer {

    private final LogHandlerInterceptor logHandlerInterceptor;

    @Autowired
    public InterceptorConfiguration(LogHandlerInterceptor logHandlerInterceptor) {
        this.logHandlerInterceptor = logHandlerInterceptor;
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(logHandlerInterceptor).addPathPatterns("/users/register");
    }

}
