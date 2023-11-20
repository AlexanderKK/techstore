package com.techx7.techstore.model;

import com.techx7.techstore.model.dto.product.ProductDTO;
import com.techx7.techstore.model.entity.Product;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class ShoppingCart {

    private final List<ProductDTO> items = new ArrayList<>();

    public List<ProductDTO> getItems() {
        return items;
    }

    public void addItem(ProductDTO product) {
        items.add(product);
    }

    public void removeItem(ProductDTO product) {
        items.remove(product);
    }

    public void clearCart() {
        items.clear();
    }

    public BigDecimal calculateTotal() {
        return items.stream()
                .map(ProductDTO::getPrice)
                .reduce(BigDecimal::add)
                .orElse(BigDecimal.ZERO);
    }

}
