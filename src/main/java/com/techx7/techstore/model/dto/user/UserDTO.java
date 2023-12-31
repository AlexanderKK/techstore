package com.techx7.techstore.model.dto.user;

import com.techx7.techstore.model.dto.role.RoleDTO;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.util.Set;
import java.util.UUID;

public class UserDTO {

    @NotNull(message = "UUID should not be empty")
    private UUID uuid;

    @NotNull(message = "Please enter an email")
    @Size(min = 5, max = 35, message = "Email should have from 5 to 35 characters")
    @Email(message = "Please enter a valid email", regexp = "^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$")
    private String email;

    @NotNull(message = "Please enter a username")
    @Size(min = 5, max = 20, message = "Username should have from 5 to 20 characters")
    private String username;

    @NotBlank(message = "Please choose at least one role")
    private String roles;

    private Set<RoleDTO> rolesSet;

    private String created;

    private String modified;

    public UserDTO() {}

    public UUID getUuid() {
        return uuid;
    }

    public void setUuid(UUID uuid) {
        this.uuid = uuid;
    }

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

    public String getRoles() {
        return roles;
    }

    public void setRoles(String roles) {
        this.roles = roles.trim();
    }

    public Set<RoleDTO> getRolesSet() {
        return rolesSet;
    }

    public void setRolesSet(Set<RoleDTO> rolesSet) {
        this.rolesSet = rolesSet;
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
        this.modified = modified == null ? modified : modified.trim();
    }

}
