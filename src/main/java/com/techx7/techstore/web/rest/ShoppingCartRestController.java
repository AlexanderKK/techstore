package com.techx7.techstore.web.rest;

import com.techx7.techstore.exception.PrincipalNotFoundException;
import com.techx7.techstore.exception.ProductQuantityException;
import com.techx7.techstore.model.dto.cart.CartItemDTO;
import com.techx7.techstore.service.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
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
        List<CartItemDTO> cartItems = cartService.getCartItems(principal);

        return cartItems;
    }

    @PostMapping("/cart/add/{uuid}/{quantity}")
    public ResponseEntity<CartItemDTO> addToCart(@PathVariable("uuid") UUID productUuid,
                                                 @PathVariable("quantity") Integer quantity,
                                                 Principal principal) {
        cartService.addProductToCart(quantity, productUuid, principal);

        return ResponseEntity.ok().build();
    }

    @PostMapping("/cart/update/{uuid}/{quantity}")
    public ResponseEntity<BigDecimal> updateQuantity(@PathVariable("uuid") UUID productUuid,
                                                     @PathVariable("quantity") Integer quantity,
                                                     Principal principal) {
        BigDecimal subtotal = cartService.updateQuantity(quantity, productUuid, principal);

        return ResponseEntity.ok().body(subtotal);
    }

    @PostMapping("/cart/remove/{uuid}")
    public ResponseEntity<CartItemDTO> removeFromCart(@PathVariable("uuid") UUID productUuid,
                                                      Principal principal) {
        cartService.removeProduct(productUuid, principal);

        return ResponseEntity.ok().build();
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(ProductQuantityException.class)
    public String handleProductQuantityError(ProductQuantityException ex) {
        System.out.println(ex.getMessage());

        return ex.getMessage();
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(PrincipalNotFoundException.class)
    public String handlePrincipalError(PrincipalNotFoundException ex) {
        System.out.println(ex.getMessage());

        return ex.getMessage();
    }

}
