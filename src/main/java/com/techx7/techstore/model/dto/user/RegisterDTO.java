package com.techx7.techstore.model.dto.user;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class RegisterDTO {

    @NotBlank(message = "Cannot be empty!")
    @Email(message = "Must be a valid email!")
    private String email;

    @NotBlank(message = "Cannot be empty!")
    @Size(min = 5, message = "Must have at least 5 length")
    private String username;

    @NotBlank(message = "Cannot be empty!")
    @Size(min = 8, message = "Must have at least 8 length")
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
