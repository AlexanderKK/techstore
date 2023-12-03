package com.techx7.techstore.model.dto.user;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class UserCredentialsDTO {

    @NotNull(message = "Please enter an email")
    @Size(min = 5, max = 35, message = "Email should have from 5 to 35 characters")
    @Email(message = "Please enter a valid email", regexp = "^(([^<>()\\[\\]\\.,;:\\s@\"]+(\\.[^<>()\\[\\]\\.,;:\\s@\"]+)*)|(\".+\"))@(((\\d{1,3}\\.){3}\\d{1,3})|([a-zA-Z0-9]+(\\.[a-zA-Z0-9]+)+))$")
    private String email;

    @NotNull(message = "Please enter a username")
    @Size(min = 5, max = 20, message = "Username should have from 5 to 20 characters")
    private String username;

    public UserCredentialsDTO() {}

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

}
