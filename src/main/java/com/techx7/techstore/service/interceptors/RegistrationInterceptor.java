package com.techx7.techstore.service.interceptors;

import com.techx7.techstore.model.dto.user.UserMetadataDTO;
import com.techx7.techstore.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import static org.hibernate.sql.ast.SqlTreeCreationLogger.LOGGER;

@Component
public class RegistrationInterceptor implements HandlerInterceptor {

    private final UserService userService;

    @Autowired
    public RegistrationInterceptor(UserService userService) {
        this.userService = userService;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        LOGGER.info("RegistrationInterceptor::preHandle()");

        String method = request.getMethod();
        if(method.equals("POST")) {
            String ip = request.getRemoteAddr();
            String userAgent = request.getHeader("user-agent");

            UserMetadataDTO userMetadataDTO = new UserMetadataDTO();
            userMetadataDTO.setIp(ip);
            userMetadataDTO.setUserAgent(userAgent);

            userService.createUserMetadata(userMetadataDTO);
        }

        return true;
    }

}
