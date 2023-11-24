package com.techx7.techstore.model.dto.user;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class UserCredentialsDTO {

    @NotBlank(message = "Please enter an email")
    @Email(message = "Please enter a valid email")
    private String email;

    @NotNull(message = "Please enter a username")
    @Size(min = 5, message = "Username should consist of at least 5 characters")
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
