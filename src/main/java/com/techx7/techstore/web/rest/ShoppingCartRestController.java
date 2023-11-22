package com.techx7.techstore.web.rest;

import com.techx7.techstore.model.entity.CartItem;
import com.techx7.techstore.service.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.security.Principal;
import java.util.UUID;

@RestController
public class ShoppingCartRestController {

    private final CartService cartService;

    @Autowired
    public ShoppingCartRestController(CartService cartService) {
        this.cartService = cartService;
    }

    @PostMapping("/cart/add/{pid}/{qty}")
    public ResponseEntity<CartItem> addToCart(@PathVariable("pid") UUID productUuid,
                                              @PathVariable("qty") Integer quantity,
                                              Principal principal) {
        CartItem cartItem = cartService.addProductToCart(quantity, productUuid, principal);

        if(cartItem == null) {
            return ResponseEntity.badRequest().build();
        }

        return ResponseEntity.ok().build();
    }

    @PostMapping("/cart/update/{pid}/{qty}")
    public ResponseEntity<BigDecimal> updateQuantity(@PathVariable("pid") UUID productUuid,
                                                   @PathVariable("qty") Integer quantity,
                                                   Principal principal) {
        BigDecimal subtotal = cartService.updateQuantity(quantity, productUuid, principal);

        return ResponseEntity.ok().body(subtotal);
    }

    @PostMapping("/cart/remove/{pid}")
    public ResponseEntity<CartItem> removeFromCart(@PathVariable("pid") UUID productUuid,
                                                     Principal principal) {
        cartService.removeProduct(productUuid, principal);

        return ResponseEntity.ok().build();
    }

}
