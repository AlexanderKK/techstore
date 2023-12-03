package com.techx7.techstore.model.dto.user;

import com.techx7.techstore.validation.user.UniqueEmail;
import com.techx7.techstore.validation.user.UniqueUsername;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class RegisterDTO {

    @UniqueEmail
    @NotNull(message = "Please enter an email")
    @Size(min = 5, max = 35, message = "Email should have from 5 to 35 characters")
    @Email(message = "Please enter a valid email", regexp = "^(([^<>()\\[\\]\\.,;:\\s@\"]+(\\.[^<>()\\[\\]\\.,;:\\s@\"]+)*)|(\".+\"))@(((\\d{1,3}\\.){3}\\d{1,3})|([a-zA-Z0-9]+(\\.[a-zA-Z0-9]+)+))$")
    private String email;

    @UniqueUsername
    @NotNull(message = "Please enter an username")
    @Size(min = 5, max = 20, message = "Username should have from 5 to 20 characters")
    private String username;

    @NotNull(message = "Please enter an password")
    @Size(min = 8, max = 20, message = "Password should have from 8 to 20 characters")
    private String password;

    public RegisterDTO() {}

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email.toLowerCase().replaceAll(" ", "").trim();
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username.toLowerCase().replaceAll(" ", "").trim();
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

}
