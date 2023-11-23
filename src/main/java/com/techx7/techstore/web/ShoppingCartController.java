package com.techx7.techstore.web;

import com.techx7.techstore.exception.PrincipalNotFoundException;
import com.techx7.techstore.model.dto.cart.CartItemDTO;
import com.techx7.techstore.service.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseStatus;

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

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(PrincipalNotFoundException.class)
    public String handlePrincipalError(PrincipalNotFoundException ex) {
        return ex.getMessage();
    }

}
