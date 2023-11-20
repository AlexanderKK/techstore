package com.techx7.techstore.web.rest;

import com.techx7.techstore.model.ShoppingCart;
import com.techx7.techstore.model.dto.product.ProductDTO;
import com.techx7.techstore.model.entity.Product;
import com.techx7.techstore.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@Controller
@SessionAttributes("cart")
public class ShoppingCartController {

    private final ProductService productService;

    @Autowired
    public ShoppingCartController(ProductService productService) {
        this.productService = productService;
    }

    @PostMapping("/addToCart")
    public String addToCart(@RequestParam UUID productId, Model model) {
        if (!model.containsAttribute("cart")) {
            model.addAttribute("cart", new ShoppingCart());
        }

        ProductDTO productDTO = productService.getProductByUuid(productId);

        if (productDTO != null) {
            ShoppingCart cart = (ShoppingCart) model.getAttribute("cart");

            cart.addItem(productDTO);
        }

        return "redirect:/products";
    }

    @GetMapping("/viewCart")
    public String viewCart(Model model) {
        if (!model.containsAttribute("cart")) {
            model.addAttribute("cart", new ShoppingCart());
        }

        ShoppingCart cart = (ShoppingCart) model.getAttribute("cart");

        model.addAttribute("cart", cart);

        cart.getItems().forEach(System.out::println);

        return "cart";
    }

    @GetMapping("/clearCart")
    public String clearCart(Model model) {
        if (!model.containsAttribute("cart")) {
            model.addAttribute("cart", new ShoppingCart());
        }

        ShoppingCart cart = (ShoppingCart) model.getAttribute("cart");

        cart.clearCart();

        return "redirect:/viewCart";
    }

}
