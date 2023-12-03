package com.techx7.techstore.model.dto.manufacturer;

import com.techx7.techstore.service.converters.StringTrimConverter;
import com.techx7.techstore.validation.manufacturer.UniqueManufacturerName;
import com.techx7.techstore.validation.multipart.MultiPartFile;
import jakarta.persistence.Convert;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import org.springframework.web.multipart.MultipartFile;

public class AddManufacturerDTO {

    @Convert(converter = StringTrimConverter.class)
    @UniqueManufacturerName
    @NotBlank(message = "Please enter a manufacturer")
    private String name;

    @MultiPartFile(contentTypes = "image/png")
    private MultipartFile image;

    @Convert(converter = StringTrimConverter.class)
    @Size(max = 255, message = "Please do not exceed the maximum length of 255 characters")
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
        this.name = name.trim();
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description == null ? description : description.trim();
    }

}
