package com.techx7.techstore.model.dto.product;

import com.techx7.techstore.model.entity.Specification;
import com.techx7.techstore.validation.multipart.MultipartFileContentType;
import com.techx7.techstore.validation.multipart.MultipartFileMaxSize;
import com.techx7.techstore.validation.multipart.MultipartFileNotNull;
import com.techx7.techstore.validation.product.ProductPrice;
import com.techx7.techstore.validation.product.UniqueProductModel;
import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import org.springframework.format.annotation.NumberFormat;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;

public class AddProductDTO {

    @MultipartFileNotNull
    @MultipartFileMaxSize
    @MultipartFileContentType
    private MultipartFile image;

    @NotNull(message = "You must choose at least one category!")
    private String categories;

    @NotNull(message = "You must choose a model!")
    @UniqueProductModel
    private Long model;

    private String description;

    private Specification specification;

    @ProductPrice
    @NotNull(message = "Cannot be empty!")
    @DecimalMin(value = "1", message = "Price must be a positive number!")
    @DecimalMax(value = "1000000", message = "Price limit is 1000000!")
    @NumberFormat(style = NumberFormat.Style.NUMBER, pattern = "####.##")
    private String price;

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

    public Specification getSpecification() {
        return specification;
    }

    public void setSpecification(Specification specification) {
        this.specification = specification;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

}
