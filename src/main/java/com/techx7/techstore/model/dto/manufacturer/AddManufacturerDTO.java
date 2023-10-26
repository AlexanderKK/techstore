package com.techx7.techstore.model.dto.manufacturer;

import com.techx7.techstore.validation.multipart.MultipartFileMaxSize;
import com.techx7.techstore.validation.multipart.MultipartFileNotNull;
import com.techx7.techstore.validation.manufacturer.UniqueManufacturerName;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.springframework.web.multipart.MultipartFile;

public class AddManufacturerDTO {

    @NotBlank(message = "Cannot be empty!")
    @UniqueManufacturerName
    private String name;

    @MultipartFileNotNull
    @MultipartFileMaxSize
    private MultipartFile image;

    private String description;

    public AddManufacturerDTO() {}

    public MultipartFile getImage() {
        return image;
    }

    public void setImage(MultipartFile image) {
        this.image = image;
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
