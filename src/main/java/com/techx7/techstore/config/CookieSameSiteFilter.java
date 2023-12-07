package com.techx7.techstore.config;

import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
@WebFilter
public class CookieSameSiteFilter implements Filter {

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        HttpServletResponse httpServletResponse = (HttpServletResponse) response;


        httpServletResponse.setHeader(
                "Set-Cookie",
                "Secure; SameSite=None"
        );

        chain.doFilter(request, response);
    }

}
