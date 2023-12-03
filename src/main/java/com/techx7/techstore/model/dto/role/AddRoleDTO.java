package com.techx7.techstore.model.dto.role;

import com.techx7.techstore.validation.multipart.MultiPartFile;
import com.techx7.techstore.validation.role.UniqueRoleName;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import org.springframework.web.multipart.MultipartFile;

public class AddRoleDTO {

    @UniqueRoleName
    @NotBlank(message = "Please enter a role")
    @Size(max = 15, message = "Role name should have a maximum length of 15 characters")
    private String name;

    @MultiPartFile(contentTypes = "image/png")
    private MultipartFile image;

    @Size(max = 255, message = "Please do not exceed the maximum length of 255 characters")
    private String description;

    public AddRoleDTO() {}

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
