package com.techx7.techstore.model.dto.cart;

import com.techx7.techstore.model.dto.product.ProductCartItemDTO;

import java.math.BigDecimal;

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

    public BigDecimal getSubtotal() {
        BigDecimal discountPrice = productDTO.getDiscountPrice();
        BigDecimal bigDecimalQuantity = BigDecimal.valueOf(quantity);

        if(discountPrice != null && discountPrice.compareTo(BigDecimal.ZERO) >= 0) {
            return discountPrice.multiply(bigDecimalQuantity);
        }

        return productDTO.getPrice().multiply(bigDecimalQuantity);
    }

}
