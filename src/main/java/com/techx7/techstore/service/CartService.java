package com.techx7.techstore.service;

import com.techx7.techstore.model.entity.CartItem;

import java.security.Principal;
import java.util.List;

public interface CartService {

    List<CartItem> getCartItems(Principal principal);

}
