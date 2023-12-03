package com.techx7.techstore.model.dto.product;

import com.techx7.techstore.service.converters.StringTrimConverter;
import com.techx7.techstore.validation.product.ProductPrice;
import jakarta.persistence.Convert;
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

    @Convert(converter = StringTrimConverter.class)
    @NotBlank(message = "Please choose an image")
    private String imageUrl;

    @NotBlank(message = "Please choose at least one category")
    private Set<String> categories;

    @Convert(converter = StringTrimConverter.class)
    private String manufacturer;

    @Convert(converter = StringTrimConverter.class)
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
        this.imageUrl = imageUrl.trim();
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
        this.manufacturer = manufacturer == null ? manufacturer : manufacturer.trim();
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model == null ? model : model.trim();
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
