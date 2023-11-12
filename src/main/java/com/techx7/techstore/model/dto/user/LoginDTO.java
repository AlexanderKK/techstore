package com.techx7.techstore.model.dto.user;

import jakarta.validation.constraints.NotBlank;

public class LoginDTO {

    @NotBlank(message = "Please enter an email")
    private String emailOrUsername;

    @NotBlank(message = "Please enter a password")
    private String password;

    public LoginDTO() {}

    public String getEmailOrUsername() {
        return emailOrUsername;
    }

    public void setEmailOrUsername(String emailOrUsername) {
        this.emailOrUsername = emailOrUsername;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

}
