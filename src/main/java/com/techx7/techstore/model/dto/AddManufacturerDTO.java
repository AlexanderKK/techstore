package com.techx7.techstore.model.dto;

import com.techx7.techstore.validation.manufacturer.UniqueManufacturerName;
import jakarta.validation.constraints.NotBlank;

public class AddManufacturerDTO {

    private String imageUrl;

    @NotBlank(message = "Cannot be empty!")
    @UniqueManufacturerName
    private String name;

    private String description;

    public AddManufacturerDTO() {}

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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

}
