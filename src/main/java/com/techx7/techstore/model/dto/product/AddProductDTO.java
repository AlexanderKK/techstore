package com.techx7.techstore.model.dto.product;

import com.techx7.techstore.model.entity.Model;
import com.techx7.techstore.model.entity.Specification;
import com.techx7.techstore.validation.multipart.MultipartFileContentType;
import com.techx7.techstore.validation.multipart.MultipartFileMaxSize;
import com.techx7.techstore.validation.multipart.MultipartFileNotNull;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;
import java.util.Set;

public class AddProductDTO {

    @MultipartFileNotNull
    @MultipartFileMaxSize
    @MultipartFileContentType
    private MultipartFile image;

    @NotNull
    private Set<String> categories;

    @NotNull
    private Model model;

    private String description;

    private Specification specification;

    @NotNull
    @Positive
    private BigDecimal price;

    public AddProductDTO() {}

    public MultipartFile getImage() {
        return image;
    }

    public void setImage(MultipartFile image) {
        this.image = image;
    }

    public Set<String> getCategories() {
        return categories;
    }

    public void setCategories(Set<String> categories) {
        this.categories = categories;
    }

    public Model getModel() {
        return model;
    }

    public void setModel(Model model) {
        this.model = model;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Specification getSpecification() {
        return specification;
    }

    public void setSpecification(Specification specification) {
        this.specification = specification;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

}
