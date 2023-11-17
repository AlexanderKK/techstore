package com.techx7.techstore.model.dto.role;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Valid
public class ImportRolesJsonDTO {

    @NotBlank
    @Size(max = 25)
    private String name;

    @NotBlank
    private String imageUrl;

    @NotBlank
    private String description;

    public ImportRolesJsonDTO() {}

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
