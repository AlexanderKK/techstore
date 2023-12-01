package com.techx7.techstore.model.dto.product;

import com.techx7.techstore.validation.product.ProductPrice;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import static com.techx7.techstore.utils.PriceUtils.formatPrice;

public class ProductDTO {

    @NotNull
    private UUID uuid;

    @NotBlank
    private String imageUrl;

    @NotNull
    private Set<String> categories;

    @NotBlank
    private String manufacturer;

    @NotBlank
    private String model;

    @ProductPrice
    private BigDecimal price;

    @ProductPrice
    private BigDecimal discountPrice;

    public ProductDTO() {
        this.categories = new HashSet<>();
    }

    public UUID getUuid() {
        return uuid;
    }

    public void setUuid(UUID uuid) {
        this.uuid = uuid;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public Set<String> getCategories() {
        return categories;
    }

    public void setCategories(Set<String> categories) {
        this.categories = categories;
    }

    public String getManufacturer() {
        return manufacturer;
    }

    public void setManufacturer(String manufacturer) {
        this.manufacturer = manufacturer;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public BigDecimal getPrice() {
        return formatPrice(price);
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public BigDecimal getDiscountPrice() {
        return formatPrice(discountPrice);
    }

    public void setDiscountPrice(BigDecimal discountPrice) {
        this.discountPrice = discountPrice;
    }

}
