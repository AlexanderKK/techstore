package com.techx7.techstore.model.dto.product;

import com.techx7.techstore.validation.multipart.MultipartFileNotNull;
import com.techx7.techstore.validation.product.ProductDiscount;
import com.techx7.techstore.validation.product.ProductPrice;
import com.techx7.techstore.validation.product.UniqueProductModel;
import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.springframework.web.multipart.MultipartFile;

public class AddProductDTO {

    @MultipartFileNotNull
    private MultipartFile image;

    @NotBlank(message = "You must choose at least one category!")
    private String categories;

    @NotNull(message = "You must choose a model!")
    @UniqueProductModel
    private Long model;

    private String description;

    @NotBlank(message = "You need to add technical characteristics!")
    private String specification;

    @ProductPrice
    @NotNull(message = "Cannot be empty!")
    @DecimalMin(value = "1", message = "Price must be a positive number!")
    @DecimalMax(value = "1000000", message = "Price limit is 1000000!")
    private String price;

    @ProductDiscount
    private String discountPercentage;

    public AddProductDTO() {}

    public MultipartFile getImage() {
        return image;
    }

    public void setImage(MultipartFile image) {
        this.image = image;
    }

    public String getCategories() {
        return categories;
    }

    public void setCategories(String categories) {
        this.categories = categories;
    }

    public Long getModel() {
        return model;
    }

    public void setModel(Long model) {
        this.model = model;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getSpecification() {
        return specification;
    }

    public void setSpecification(String specification) {
        this.specification = specification;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getDiscountPercentage() {
        return discountPercentage;
    }

    public void setDiscountPercentage(String discountPercentage) {
        this.discountPercentage = discountPercentage;
    }

}
