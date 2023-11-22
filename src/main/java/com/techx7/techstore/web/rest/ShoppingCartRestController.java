package com.techx7.techstore.web.rest;

import com.techx7.techstore.model.dto.cart.CartItemDTO;
import com.techx7.techstore.service.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.security.Principal;
import java.util.List;
import java.util.UUID;

@CrossOrigin("*")
@RestController
public class ShoppingCartRestController {

    private final CartService cartService;

    @Autowired
    public ShoppingCartRestController(CartService cartService) {
        this.cartService = cartService;
    }

    @GetMapping("/cart/load")
    public List<CartItemDTO> loadCartItems(Principal principal) {
        if(principal == null) {
            return null;
        }

        List<CartItemDTO> cartItems = cartService.getCartItems(principal);

        return cartItems;
    }

    @PostMapping("/cart/add/{pid}/{qty}")
    public ResponseEntity<CartItemDTO> addToCart(@PathVariable("pid") UUID productUuid,
                                              @PathVariable("qty") Integer quantity,
                                              Principal principal) {
        if(principal == null) {
            return ResponseEntity.badRequest().build();
        }

        CartItemDTO cartItem = cartService.addProductToCart(quantity, productUuid, principal);

        if(cartItem == null) {
            return ResponseEntity.badRequest().build();
        }

        return ResponseEntity.ok().build();
    }

    @PostMapping("/cart/update/{pid}/{qty}")
    public ResponseEntity<BigDecimal> updateQuantity(@PathVariable("pid") UUID productUuid,
                                                   @PathVariable("qty") Integer quantity,
                                                   Principal principal) {
        if(principal == null) {
            return ResponseEntity.badRequest().build();
        }

        BigDecimal subtotal = cartService.updateQuantity(quantity, productUuid, principal);

        return ResponseEntity.ok().body(subtotal);
    }

    @PostMapping("/cart/remove/{pid}")
    public ResponseEntity<CartItemDTO> removeFromCart(@PathVariable("pid") UUID productUuid,
                                                      Principal principal) {
        if(principal == null) {
            return ResponseEntity.badRequest().build();
        }

        cartService.removeProduct(productUuid, principal);

        return ResponseEntity.ok().build();
    }

}
