package com.techx7.techstore.web.rest;

import com.techx7.techstore.model.dto.product.ProductDTO;
import com.techx7.techstore.model.entity.CartItem;
import com.techx7.techstore.model.entity.Product;
import com.techx7.techstore.model.entity.User;
import com.techx7.techstore.repository.CartRepository;
import com.techx7.techstore.repository.ProductRepository;
import com.techx7.techstore.service.CartService;
import com.techx7.techstore.service.ProductService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;
import java.util.UUID;

@Controller
//@SessionAttributes("cart")
public class ShoppingCartController {

    private final CartService cartService;
    private final ProductRepository productRepository;
    private final ProductService productService;
    private final ModelMapper mapper;
    private final CartRepository cartRepository;

    @Autowired
    public ShoppingCartController(CartService cartService,
                                  ProductRepository productRepository,
                                  ProductService productService,
                                  ModelMapper mapper,
                                  CartRepository cartRepository) {
        this.cartService = cartService;
        this.productRepository = productRepository;
        this.productService = productService;
        this.mapper = mapper;
        this.cartRepository = cartRepository;
    }

    @PreAuthorize("hasRole('USER')")
    @GetMapping("/cart")
    public String showShoppingCart(Model model, Principal principal) {
        List<CartItem> cartItems = cartService.getCartItems(principal);

        model.addAttribute("cartItems", cartItems);

        return "cart";
    }

}
