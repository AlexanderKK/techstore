package com.techx7.techstore.service.interceptors;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
public class LogHandlerInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object handler) {
//        if(isNullOrEmpty(httpServletRequest.getHeader("emp-auth-key"))) {
//            throw new InvalidHeaderFileException("Invalid request");
//        }

        return true;
    }

}
