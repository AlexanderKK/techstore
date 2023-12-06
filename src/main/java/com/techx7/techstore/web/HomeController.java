package com.techx7.techstore.web;

import com.techx7.techstore.model.dto.product.ProductDTO;
import com.techx7.techstore.service.MonitoringService;
import com.techx7.techstore.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.security.Principal;
import java.util.List;

@Controller
public class HomeController {

    private final ProductService productService;
    private final MonitoringService monitoringService;

    @Autowired
    public HomeController(ProductService productService,
                          MonitoringService monitoringService) {
        this.productService = productService;
        this.monitoringService = monitoringService;
    }

    @GetMapping("/")
    public String index(Model model, Principal principal) {
        List<ProductDTO> productsWithDiscount = productService.getAllProductsWithDiscount();

        model.addAttribute("products", productsWithDiscount);

        if(principal != null) {
            monitoringService.logUsersLogins();
        }

        return "index";
    }

}
