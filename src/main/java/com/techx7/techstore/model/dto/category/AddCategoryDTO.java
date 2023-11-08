package com.techx7.techstore.model.dto.category;

import com.techx7.techstore.validation.category.UniqueCategoryName;
import com.techx7.techstore.validation.multipart.MultiPartFile;
import jakarta.validation.constraints.NotBlank;
import org.springframework.web.multipart.MultipartFile;

public class AddCategoryDTO {

    @NotBlank(message = "Please enter a category")
    @UniqueCategoryName
    private String name;

    @MultiPartFile(contentTypes = {"image/png", "image/jpeg"})
    private MultipartFile image;

    private String description;

    public AddCategoryDTO() {}

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public org.springframework.web.multipart.MultipartFile getImage() {
        return image;
    }

    public void setImage(org.springframework.web.multipart.MultipartFile image) {
        this.image = image;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

}
