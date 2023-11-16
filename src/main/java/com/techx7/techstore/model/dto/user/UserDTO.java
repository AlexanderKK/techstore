package com.techx7.techstore.model.dto.user;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.Set;

public class UserDTO {

    @NotBlank(message = "UUID should not be empty")
    private String uuid;

    @NotBlank(message = "Email should not be empty")
    private String email;

    @NotBlank(message = "Username should not be empty")
    private String username;

    @NotNull(message = "There should be at least one role")
    private Set<String> roles;

    @NotBlank(message = "Creation date should not be empty")
    private String created;

    private String modified;

    public UserDTO() {}

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
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

    public Set<String> getRoles() {
        return roles;
    }

    public void setRoles(Set<String> roles) {
        this.roles = roles;
    }

    public String getCreated() {
        return created;
    }

    public void setCreated(String created) {
        this.created = created;
    }

    public String getModified() {
        return modified;
    }

    public void setModified(String modified) {
        this.modified = modified;
    }

}
