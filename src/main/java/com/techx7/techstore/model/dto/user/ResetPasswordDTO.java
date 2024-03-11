package com.techx7.techstore.model.dto.user;

import com.techx7.techstore.validation.matches.EmailMatch;
import com.techx7.techstore.validation.matches.PasswordMatch;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.util.UUID;

@EmailMatch(
        first = "email",
        second = "originalEmail",
        message = "Please enter your valid email"
)
@PasswordMatch(
        first = "password",
        second = "confirmPassword",
        message = "Passwords do not match"
)
public class ResetPasswordDTO {

    private String email;

    private String originalEmail;

    @NotNull(message = "Please enter a new password")
    @Size(min = 8, max = 20, message = "Password should have from 8 to 20 characters")
    private String password;

    private String confirmPassword;

    private UUID uuid;

    public ResetPasswordDTO() {}

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getOriginalEmail() {
        return originalEmail;
    }

    public void setOriginalEmail(String originalEmail) {
        this.originalEmail = originalEmail;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getConfirmPassword() {
        return confirmPassword;
    }

    public void setConfirmPassword(String confirmPassword) {
        this.confirmPassword = confirmPassword;
    }

    public UUID getUuid() {
        return uuid;
    }

    public void setUuid(UUID uuid) {
        this.uuid = uuid;
    }

}
