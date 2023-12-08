package com.techx7.techstore.model.dto.product;

import com.techx7.techstore.validation.product.ProductDiscount;
import com.techx7.techstore.validation.product.ProductPrice;
import com.techx7.techstore.validation.product.ProductQuantity;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class ImportProductJsonDTO {

    @NotBlank(message = "Please enter an image url")
    private String imageUrl;

    @NotBlank(message = "Please enter at least one category")
    private String categories;

    @NotNull(message = "Please enter a model name")
    private String model;

    @Size(max = 255, message = "Please do not exceed the maximum length of 255 characters")
    private String description;

    @NotBlank(message = "Please describe product's technical characteristics")
    private String specification;

    @ProductPrice(min = 1, max = 1000000)
    private String price;

    @ProductDiscount
    private String discountPercentage;

    @ProductQuantity(max = 100)
    private String initialQuantity;

    public ImportProductJsonDTO() {}

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getCategories() {
        return categories;
    }

    public void setCategories(String categories) {
        this.categories = categories.trim();
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description == null ? description : description.trim();
    }

    public String getSpecification() {
        return specification;
    }

    public void setSpecification(String specification) {
        this.specification = specification.trim();
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price == null ? price : price.trim();
    }

    public String getDiscountPercentage() {
        return discountPercentage;
    }

    public void setDiscountPercentage(String discountPercentage) {
        this.discountPercentage = discountPercentage == null ? discountPercentage : discountPercentage.trim();
    }

    public String getInitialQuantity() {
        return initialQuantity;
    }

    public void setInitialQuantity(String initialQuantity) {
        this.initialQuantity = initialQuantity == null ? initialQuantity : initialQuantity.trim();
    }

}
