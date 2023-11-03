package com.techx7.techstore.web;

import com.techx7.techstore.model.dto.product.AddProductDTO;
import com.techx7.techstore.service.ProductService;
import com.techx7.techstore.util.FileUtils;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.util.UUID;

@Controller
@RequestMapping("/products")
public class ProductController {

    private final ProductService productService;

    @Autowired
    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping
    public String index() {
        return "products";
    }

    @GetMapping("/manage")
    public String manageProduct(Model model) {
        if(!model.containsAttribute("addProductDTO")) {
            model.addAttribute("addProductDTO", new AddProductDTO());
        }

        return "product-add";
    }

    @PostMapping("/manage/add")
    public String addProduct(@Valid AddProductDTO addProductDTO,
                                  BindingResult bindingResult,
                                  RedirectAttributes redirectAttributes) throws IOException {
        if(bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute("addProductDTO", addProductDTO);
            redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.addProductDTO", bindingResult);

            return "redirect:/products/manage";
        }

        FileUtils.saveImageLocally(addProductDTO.getImage());

        productService.createProduct(addProductDTO);

        return "redirect:/manufacturers/manage";
    }

    @DeleteMapping("/manage/delete-all")
    public String deleteAllProducts() {
        productService.deleteAllManufacturers();

        return "redirect:/manufacturers/manage";
    }

    @DeleteMapping("/manage/delete/{uuid}")
    public String deleteProduct(@PathVariable("uuid") UUID uuid) {
        productService.deleteProductByUuid(uuid);

        return "redirect:/products/manage";
    }

}
