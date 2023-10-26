package com.techx7.techstore.web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/products")
public class ProductController {

    @GetMapping
    public String index() {
        return "products";
    }

    @GetMapping("/manage/add")
    public String addProduct() {
        return "product-management-add";
    }

}
