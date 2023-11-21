package com.techx7.techstore.service;

import com.techx7.techstore.model.entity.CartItem;

import java.security.Principal;
import java.util.List;
import java.util.UUID;

public interface CartService {

    List<CartItem> getCartItems(Principal principal);

    CartItem addProductToCart(Principal principal, UUID uuid, Integer quantity);

}
