package com.techx7.techstore.model.entity;

import com.techx7.techstore.model.dto.user.UserDTO;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;
import java.util.Calendar;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "users")
public class User extends BaseEntity {

    @NotBlank(message = "Email should not be empty")
    @Column(nullable = false, unique = true)
    private String email;

    @NotBlank(message = "Username should not be empty")
    @Column(nullable = false, unique = true)
    private String username;

    @NotBlank(message = "Password should not be empty")
    @Column(nullable = false)
    private String password;

    @NotNull(message = "Should not be empty")
    @DateTimeFormat(pattern = "yyyy-MM-dd hh:mm:ss")
    @Temporal(TemporalType.TIMESTAMP)
    @Column(nullable = false, columnDefinition = "TIMESTAMP")
    private LocalDateTime created = LocalDateTime.now();

    @DateTimeFormat(pattern = "yyyy-MM-dd hh:mm:ss")
    @Temporal(TemporalType.TIMESTAMP)
    @Column(columnDefinition = "TIMESTAMP")
    private LocalDateTime modified;

    @DateTimeFormat(pattern = "yyyy-MM-dd hh:mm:ss")
    @Temporal(TemporalType.TIMESTAMP)
    @Column(columnDefinition = "TIMESTAMP")
    private LocalDateTime lastLogin;

    @Column(name = "failed_login_attempts")
    private Integer failedLoginAttempts;

    @NotNull(message = "There should be at least one role")
    @ManyToMany(fetch = FetchType.EAGER)
    private Set<Role> roles;

    @Column(name = "is_active")
    private boolean isActive = false;

    @OneToMany(mappedBy = "user", fetch = FetchType.EAGER)
    private Set<UserActivationCode> activationCodes;

    public User() {
        this.roles = new HashSet<>();
    }

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

    public LocalDateTime getCreated() {
        return created;
    }

    public void setCreated(LocalDateTime created) {
        this.created = created;
    }

    public LocalDateTime getModified() {
        return modified;
    }

    public void setModified(LocalDateTime modified) {
        this.modified = modified;
    }

    public LocalDateTime getLastLogin() {
        return lastLogin;
    }

    public void setLastLogin(LocalDateTime lastLogin) {
        this.lastLogin = lastLogin;
    }

    public Integer getFailedLoginAttempts() {
        return failedLoginAttempts;
    }

    public void setFailedLoginAttempts(Integer failedLoginAttempts) {
        this.failedLoginAttempts = failedLoginAttempts;
    }

    public Set<Role> getRoles() {
        return roles;
    }

    public void setRoles(Set<Role> roles) {
        this.roles = roles;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }

    public Set<UserActivationCode> getActivationCodes() {
        return activationCodes;
    }

    public void setActivationCodes(Set<UserActivationCode> activationCodes) {
        this.activationCodes = activationCodes;
    }

    public User editUser(UserDTO userDTO) {
        this.setEmail(userDTO.getEmail());
        this.setUsername(userDTO.getUsername());
        this.setModified(LocalDateTime.now());

        return this;
    }

}
