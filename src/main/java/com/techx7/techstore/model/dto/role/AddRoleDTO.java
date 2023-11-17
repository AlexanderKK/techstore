package com.techx7.techstore.model.dto.role;

import com.techx7.techstore.validation.multipart.MultiPartFile;
import com.techx7.techstore.validation.role.UniqueRoleName;
import jakarta.validation.constraints.NotBlank;
import org.springframework.web.multipart.MultipartFile;

public class AddRoleDTO {

    @NotBlank(message = "Please enter a role")
    @UniqueRoleName
    private String name;

    @MultiPartFile(contentTypes = "image/png")
    private MultipartFile image;

    private String description;

    public AddRoleDTO() {}

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
