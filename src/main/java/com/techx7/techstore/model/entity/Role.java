package com.techx7.techstore.model.entity;

import com.techx7.techstore.model.dto.role.RoleDTO;
import jakarta.persistence.*;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.util.Locale;
import java.util.Set;

@Entity
@Table(name = "roles")
public class Role extends BaseEntity {

    @NotBlank(message = "Name should not be empty")
    @Size(max = 15)
    @Column(nullable = false, unique = true)
    private String name;

    @NotBlank(message = "Image url should not be empty")
    @Column(nullable = false)
    private String imageUrl;

    @Size(max = 255, message = "Description should have a maximum length of 255 characters")
    private String description;

    @ManyToMany
    @JoinTable(name = "users_roles",
            joinColumns = @JoinColumn(name = "role_id"),
            inverseJoinColumns = @JoinColumn(name = "user_id"))
    @Valid
    private Set<User> users;

    public Role() {}

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Set<User> getUsers() {
        return users;
    }

    public void setUsers(Set<User> users) {
        this.users = users;
    }

    public Role editRole(RoleDTO roleDTO) {
        this.name = roleDTO.getName().toUpperCase(Locale.getDefault());
        this.description = roleDTO.getDescription();
        this.imageUrl = roleDTO.getImageUrl();

        return this;
    }

}
