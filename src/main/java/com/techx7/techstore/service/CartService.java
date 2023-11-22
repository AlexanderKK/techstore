package com.techx7.techstore.service;

import com.techx7.techstore.model.dto.cart.CartItemDTO;

import java.math.BigDecimal;
import java.security.Principal;
import java.util.List;
import java.util.UUID;

public interface CartService {

    List<CartItemDTO> getCartItems(Principal principal);

    CartItemDTO addProductToCart(Integer quantity, UUID productUuid, Principal principal);

    BigDecimal updateQuantity(Integer quantity, UUID productUuid, Principal principal);

    void removeProduct(UUID productUuid, Principal principal);

}
