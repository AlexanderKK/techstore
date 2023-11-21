package com.techx7.techstore.web.rest;

import com.techx7.techstore.service.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;
import java.util.UUID;

@RestController
public class ShoppingCartRestController {

    private final CartService cartService;

    @Autowired
    public ShoppingCartRestController(CartService cartService) {
        this.cartService = cartService;
    }

    @PreAuthorize("hasRole('USER')")
    @PostMapping("/cart/add/{pid}/{qty}")
    public String addToCart(@PathVariable("pid") UUID uuid,
                            @PathVariable("qty") Integer quantity,
                            Principal principal) {
        return cartService.addProductToCart(principal, uuid, quantity);
    }

}
