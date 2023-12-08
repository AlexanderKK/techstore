package com.techx7.techstore.model.dto.user;

import jakarta.validation.constraints.NotBlank;

public class UserMetadataDTO {

    @NotBlank(message = "IP address should not be empty")
    private String ip;

    @NotBlank(message = "User agent should not be empty")
    private String userAgent;

    public UserMetadataDTO() {}

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getUserAgent() {
        return userAgent;
    }

    public void setUserAgent(String userAgent) {
        this.userAgent = userAgent;
    }

}
