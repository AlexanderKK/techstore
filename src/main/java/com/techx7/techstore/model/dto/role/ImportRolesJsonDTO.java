package com.techx7.techstore.model.dto.role;

import com.techx7.techstore.validation.role.UniqueRoleName;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Valid
public class ImportRolesJsonDTO {

    @UniqueRoleName
    @NotBlank(message = "Please enter a role")
    @Size(max = 15, message = "Role name should have a maximum length of 15 characters")
    private String name;

    @NotBlank(message = "Please enter an image url")
    private String imageUrl;

    @NotBlank(message = "Please enter a description")
    private String description;

    public ImportRolesJsonDTO() {}

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name.trim();
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl.trim();
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description.trim();
    }

}
