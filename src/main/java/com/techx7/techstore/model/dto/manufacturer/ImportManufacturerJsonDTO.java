package com.techx7.techstore.model.dto.manufacturer;

import com.techx7.techstore.validation.manufacturer.UniqueManufacturerName;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;

@Valid
public class ImportManufacturerJsonDTO {

    @UniqueManufacturerName
    @NotBlank(message = "Please enter a manufacturer")
    private String name;

    @NotBlank(message = "Please enter an image url")
    private String imageUrl;

    private String description;

    public ImportManufacturerJsonDTO() {}

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
