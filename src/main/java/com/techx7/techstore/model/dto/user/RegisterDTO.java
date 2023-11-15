package com.techx7.techstore.model.dto.user;

import com.techx7.techstore.validation.user.UniqueUsername;
import com.techx7.techstore.validation.user.UniqueEmail;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class RegisterDTO {

    @UniqueEmail
    @NotBlank(message = "Please enter an email")
    @Email(message = "Please enter a valid email")
    private String email;

    @UniqueUsername
    @NotNull(message = "Please enter an username")
    @Size(min = 5, message = "Username should consist of at least 5 characters")
    private String username;

    @NotNull(message = "Please enter an password")
    @Size(min = 8, message = "Password should consist of at least 8 characters")
    private String password;

    public RegisterDTO() {}

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

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

}
