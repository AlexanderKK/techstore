package com.techx7.techstore.model.dto.category;

import com.techx7.techstore.validation.category.UniqueCategoryName;
import com.techx7.techstore.validation.multipart.MultipartFileContentType;
import com.techx7.techstore.validation.multipart.MultipartFileMaxSize;
import com.techx7.techstore.validation.multipart.MultipartFileNotNull;
import jakarta.validation.constraints.NotBlank;
import org.springframework.web.multipart.MultipartFile;

public class AddCategoryDTO {

    @NotBlank(message = "Cannot be empty!")
    @UniqueCategoryName
    private String name;

    @MultipartFileNotNull
    private MultipartFile image;

    private String description;

    public AddCategoryDTO() {}

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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
        this.description = description;
    }

}
