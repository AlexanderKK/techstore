package com.techx7.techstore.service;

import com.techx7.techstore.model.dto.product.ProductDTO;
import com.techx7.techstore.model.entity.CartItem;
import com.techx7.techstore.model.entity.User;

import java.security.Principal;
import java.util.List;
import java.util.UUID;

public interface CartService {

    List<CartItem> getCartItems(Principal principal);

    String addProductToCart(Principal principal, UUID uuid, Integer quantity);

}
