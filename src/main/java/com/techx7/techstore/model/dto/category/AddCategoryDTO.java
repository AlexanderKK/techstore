package com.techx7.techstore.model.dto.category;

import com.techx7.techstore.validation.category.UniqueCategoryName;
import com.techx7.techstore.validation.multipart.MultiPartFile;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import org.springframework.web.multipart.MultipartFile;

public class AddCategoryDTO {

    @NotBlank(message = "Please enter a category")
    @UniqueCategoryName
    private String name;

    @MultiPartFile(contentTypes = "image/png")
    private MultipartFile image;

    @Size(max = 255, message = "Please do not exceed the maximum length of 255 characters")
    private String description;

    public AddCategoryDTO() {}

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name.trim();
    }

    public MultipartFile getImage() {
        return image;
    }

    public void setImage(MultipartFile image) {
        this.image = image;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description == null ? description : description.trim();
    }

}
