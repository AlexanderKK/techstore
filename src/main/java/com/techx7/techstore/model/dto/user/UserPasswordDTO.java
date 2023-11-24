package com.techx7.techstore.model.dto.user;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class UserPasswordDTO {

    @NotNull(message = "Please enter an password")
    @Size(min = 8, message = "Password should consist of at least 8 characters")
    private String password;

    @NotNull(message = "Please enter an password")
    @Size(min = 8, message = "New password should consist of at least 8 characters")
    private String newPassword;

    public UserPasswordDTO() {}

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getNewPassword() {
        return newPassword;
    }

    public void setNewPassword(String newPassword) {
        this.newPassword = newPassword;
    }

}
