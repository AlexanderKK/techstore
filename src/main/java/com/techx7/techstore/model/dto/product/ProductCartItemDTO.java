package com.techx7.techstore.model.dto.product;

import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.util.UUID;

public class ProductCartItemDTO {

    @NotNull
    private UUID uuid;

    @NotBlank
    private String imageUrl;

    @NotBlank
    private String link;

    @NotNull
    @Digits(integer = 6, fraction = 2)
    private BigDecimal price;

    @Digits(integer = 6, fraction = 2)
    private BigDecimal discountPrice;

    public ProductCartItemDTO() {}

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

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link == null ? "Missing Manufacturer & Model" : link.trim();
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public BigDecimal getDiscountPrice() {
        return discountPrice;
    }

    public void setDiscountPrice(BigDecimal discountPrice) {
        this.discountPrice = discountPrice;
    }

}
