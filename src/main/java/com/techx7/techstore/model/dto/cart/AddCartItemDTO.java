package com.techx7.techstore.model.dto.cart;

import java.util.UUID;

public class AddCartItemDTO {

    private UUID productId;

    private Integer quantity;

    public AddCartItemDTO() {}

    public UUID getProductId() {
        return productId;
    }

    public void setProductId(UUID productId) {
        this.productId = productId;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

}
