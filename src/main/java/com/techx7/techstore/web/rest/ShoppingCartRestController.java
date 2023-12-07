package com.techx7.techstore.web.rest;

import com.techx7.techstore.exception.EntityNotFoundException;
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
import java.util.Map;
import java.util.UUID;

@CrossOrigin(origins = {"http://localhost:8080", "https://techx7.yellowflower-41c8e8d4.westeurope.azurecontainerapps.io"})
@RestController
public class ShoppingCartRestController {

    private final CartService cartService;

    @Autowired
    public ShoppingCartRestController(CartService cartService) {
        this.cartService = cartService;
    }

    @GetMapping("/cart/load")
    public ResponseEntity<List<CartItemDTO>> loadCartItems(Principal principal) {
        List<CartItemDTO> cartItems = cartService.getCartItems(principal);

        return ResponseEntity.ok(cartItems);
    }

    @PostMapping("/cart/add/{uuid}/{quantity}")
    public ResponseEntity<CartItemDTO> addToCart(@PathVariable("uuid") UUID productUuid,
                                                 @PathVariable("quantity") Integer quantity,
                                                 Principal principal) {
        CartItemDTO cartItemDTO = cartService.addProductToCart(quantity, productUuid, principal);

        return ResponseEntity.ok(cartItemDTO);
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
    public Map<String, String> handleProductQuantityError(ProductQuantityException ex) {
        System.out.println(ex.getMessage());

        return Map.of(
                "error", ex.getMessage(),
                "availableQuantity", ex.getAvailableQuantity().toString()
        );
    }

    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    @ExceptionHandler(PrincipalNotFoundException.class)
    public String handlePrincipalError(PrincipalNotFoundException ex) {
        System.out.println(ex.getMessage());

        return ex.getMessage();
    }

    @ExceptionHandler(EntityNotFoundException.class)
    public String handleEntityError(EntityNotFoundException ex) {
        System.out.println(ex.getMessage());

        return ex.getMessage();
    }

}
