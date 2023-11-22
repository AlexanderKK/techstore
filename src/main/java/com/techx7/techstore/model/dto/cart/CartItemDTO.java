package com.techx7.techstore.model.dto.cart;

import com.techx7.techstore.model.dto.product.ProductCartItemDTO;

public class CartItemDTO {

    private ProductCartItemDTO productDTO;

    private Integer quantity;

    public CartItemDTO() {}

    public ProductCartItemDTO getProductDTO() {
        return productDTO;
    }

    public void setProductDTO(ProductCartItemDTO productDTO) {
        this.productDTO = productDTO;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

}
