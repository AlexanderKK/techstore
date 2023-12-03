package com.techx7.techstore.model.entity;

import com.techx7.techstore.model.dto.user.UserCredentialsDTO;
import com.techx7.techstore.model.dto.user.UserDTO;
import jakarta.persistence.*;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "users")
public class User extends BaseEntity {

    @NotBlank(message = "Email should not be empty")
    @Size(min = 5, max = 35, message = "Email should have from 5 to 35 characters")
    @Email(message = "Email should be valid", regexp = "^(([^<>()\\[\\]\\.,;:\\s@\"]+(\\.[^<>()\\[\\]\\.,;:\\s@\"]+)*)|(\".+\"))@(((\\d{1,3}\\.){3}\\d{1,3})|([a-zA-Z0-9]+(\\.[a-zA-Z0-9]+)+))$")
    @Column(nullable = false, unique = true)
    private String email;

    @NotBlank(message = "Username should not be empty")
    @Size(min = 5, max = 20, message = "Username should have from 5 to 20 characters")
    @Column(nullable = false, unique = true)
    private String username;

    @NotBlank(message = "Password should not be empty")
    @Column(nullable = false)
    private String password;

    @NotNull(message = "Creation date should not be empty")
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

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "users_roles",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id"))
    @Valid
    private Set<Role> roles;

    @Column(name = "is_active")
    private boolean isActive = false;

    @OneToMany(mappedBy = "user", fetch = FetchType.EAGER, cascade = CascadeType.REMOVE)
    private Set<UserActivationCode> activationCodes;

    @OneToOne(fetch = FetchType.EAGER, cascade = CascadeType.REMOVE)
    private UserInfo userInfo;

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

    public UserInfo getUserInfo() {
        return userInfo;
    }

    public void setUserInfo(UserInfo userInfo) {
        this.userInfo = userInfo;
    }

    public void editUser(UserDTO userDTO) {
        this.setEmail(userDTO.getEmail());
        this.setUsername(userDTO.getUsername());
        this.setModified(LocalDateTime.now());
    }

    public void editUserCredentials(UserCredentialsDTO userCredentialsDTO) {
        this.setEmail(userCredentialsDTO.getEmail());
        this.setUsername(userCredentialsDTO.getUsername());
        this.setModified(LocalDateTime.now());
    }

}
