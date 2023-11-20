package com.techx7.techstore.model.dto.role;

import com.techx7.techstore.validation.multipart.MultiPartFile;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.UUID;

import static com.techx7.techstore.util.FileUtils.manageImage;

public class RoleDTO {

    private Long id;

    @NotNull(message = "UUID should not be empty")
    private UUID uuid;

    @NotBlank(message = "Please enter a role")
    @Size(max = 15, message = "Role name should have maximum length of 15 characters")
    private String name;

    @MultiPartFile(contentTypes = "image/png")
    private MultipartFile image;

    private String imageUrl;

    @Size(max = 255, message = "Please do not exceed the maximum length of 255 characters")
    private String description;

    public RoleDTO() {}

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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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
        this.imageUrl = imageUrl;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

}
