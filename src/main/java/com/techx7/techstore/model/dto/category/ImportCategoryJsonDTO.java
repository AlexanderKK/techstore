package com.techx7.techstore.model.dto.category;

import com.techx7.techstore.validation.category.UniqueCategoryName;
import jakarta.validation.constraints.NotBlank;

public class ImportCategoryJsonDTO {

    @UniqueCategoryName
    @NotBlank(message = "Please enter a category")
    private String name;

    @NotBlank(message = "Please enter an image url")
    private String imageUrl;

    private String description;

    public ImportCategoryJsonDTO() {}

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
