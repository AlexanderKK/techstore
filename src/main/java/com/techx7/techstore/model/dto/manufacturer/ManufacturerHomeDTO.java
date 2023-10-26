package com.techx7.techstore.model.dto.manufacturer;

import jakarta.validation.constraints.NotBlank;

public class ManufacturerHomeDTO {

    @NotBlank
    private String imageUrl;

    @NotBlank
    private String name;

    public ManufacturerHomeDTO() {}

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

}
