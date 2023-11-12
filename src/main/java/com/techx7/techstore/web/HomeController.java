package com.techx7.techstore.web;

import com.techx7.techstore.model.dto.product.ProductDTO;
import com.techx7.techstore.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
public class HomeController {

    private final ProductService productService;

    @Autowired
    public HomeController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping("/")
    public String index(Model model) {
        List<ProductDTO> productsWithDiscount = productService.getAllProductsWithDiscount();
        model.addAttribute("products", productsWithDiscount);

        return "index";
    }

}
