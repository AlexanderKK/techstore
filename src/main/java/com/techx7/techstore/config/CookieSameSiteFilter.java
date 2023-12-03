package com.techx7.techstore.config;

import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
@WebFilter
public class CookieSameSiteFilter implements Filter {

    private static final boolean IS_SECURE = false;

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        HttpServletResponse httpServletResponse = (HttpServletResponse) response;

        String secureValue = "";
        if(IS_SECURE) {
            secureValue = "Secure;";
        }

        httpServletResponse.setHeader(
                "Set-Cookie",
                String.format("%s SameSite=None", secureValue)
        );

        chain.doFilter(request, response);
    }

}
