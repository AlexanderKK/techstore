package com.techx7.techstore.model.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Entity
@Table(name = "roles")
public class Role extends BaseEntity {

    @NotBlank(message = "Name should not be empty")
    @Size(max = 25)
    @Column(nullable = false, unique = true)
    private String name;

    @NotBlank(message = "Image url should not be empty")
    @Column(nullable = false)
    private String imageUrl;

    @NotBlank(message = "Description should not be empty")
    @Column(nullable = false)
    private String description;

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

}
