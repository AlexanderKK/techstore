package com.techx7.techstore.web.rest;

import com.techx7.techstore.model.dto.cart.CartItemDTO;
import com.techx7.techstore.service.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.security.Principal;
import java.util.List;

@Controller
public class ShoppingCartController {

    private final CartService cartService;

    @Autowired
    public ShoppingCartController(CartService cartService) {
        this.cartService = cartService;
    }

    @PreAuthorize("hasRole('USER')")
    @GetMapping("/cart")
    public String showShoppingCart(Model model, Principal principal) {
        List<CartItemDTO> cartItems = cartService.getCartItems(principal);

        model.addAttribute("cartItems", cartItems);

        return "cart";
    }

}
