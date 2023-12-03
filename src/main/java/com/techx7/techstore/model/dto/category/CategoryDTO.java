package com.techx7.techstore.model.dto.category;

import com.techx7.techstore.validation.multipart.MultiPartFile;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.UUID;

import static com.techx7.techstore.utils.FileUtils.manageImage;

public class CategoryDTO {

    private Long id;

    @NotNull
    private UUID uuid;

    @MultiPartFile(contentTypes = "image/png")
    private MultipartFile image;

    private String imageUrl;

    @NotBlank(message = "Please enter a category")
    private String name;

    @Size(max = 255, message = "Please do not exceed the maximum length of 255 characters")
    private String description;

    private String created;

    private String modified;

    public CategoryDTO() {}

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public UUID getUuid() {
        return uuid;
    }

    public void setUuid(UUID uuid) {
        this.uuid = uuid;
    }

    public MultipartFile getImage() {
        return image;
    }

    public void setImage(MultipartFile image) throws IOException {
        this.image = manageImage(image, imageUrl);
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl == null ? imageUrl : imageUrl.trim();
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

    public String getCreated() {
        return created;
    }

    public void setCreated(String created) {
        this.created = created;
    }

    public String getModified() {
        return modified;
    }

    public void setModified(String modified) {
        this.modified = modified == null ? modified : modified.trim();
    }

}
