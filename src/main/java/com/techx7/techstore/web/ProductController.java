package com.techx7.techstore.web;

import com.techx7.techstore.exception.CategoryNotFoundException;
import com.techx7.techstore.exception.ModelNotFoundException;
import com.techx7.techstore.model.dto.category.CategoryDTO;
import com.techx7.techstore.model.dto.manufacturer.ManufacturerWithModelsDTO;
import com.techx7.techstore.model.dto.product.AddProductDTO;
import com.techx7.techstore.model.dto.product.ProductDTO;
import com.techx7.techstore.service.CategoryService;
import com.techx7.techstore.service.ManufacturerService;
import com.techx7.techstore.service.ProductService;
import com.techx7.techstore.util.FileUtils;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.util.List;

import static com.techx7.techstore.constant.Paths.BINDING_RESULT_PATH;
import static com.techx7.techstore.constant.Paths.DOT;

@Controller
@RequestMapping("/products")
public class ProductController {

    private final static String flashAttributeDTO = "addProductDTO";
    private final ProductService productService;
    private final ManufacturerService manufacturerService;
    private final CategoryService categoryService;

    @Autowired
    public ProductController(ProductService productService,
                             ManufacturerService manufacturerService,
                             CategoryService categoryService) {
        this.productService = productService;
        this.manufacturerService = manufacturerService;
        this.categoryService = categoryService;
    }

    @ExceptionHandler(CategoryNotFoundException.class)
    public String handleCategoryError(CategoryNotFoundException e) {
        System.out.println(e.getMessage());

        return "redirect:/products/manage/add";
    }

    @ExceptionHandler(ModelNotFoundException.class)
    public String handleModelError(ModelNotFoundException e) {
        System.out.println(e.getMessage());

        return "redirect:/products/manage/add";
    }

    @GetMapping
    public String index(Model model) {
        List<ProductDTO> products = productService.getAllProducts();

        model.addAttribute("products", products);

        return "products";
    }

    @GetMapping("/manage/add")
    public String manageProduct(Model model) {
        List<ManufacturerWithModelsDTO> manufacturerWithModelsDTOs = manufacturerService.getManufacturersWithModelsDTO();

        List<CategoryDTO> categoryDTOs = categoryService.getAllCategories();

        model.addAttribute("categories", categoryDTOs);
        model.addAttribute("manufacturers", manufacturerWithModelsDTOs);

        if(!model.containsAttribute(flashAttributeDTO)) {
            model.addAttribute(flashAttributeDTO, new AddProductDTO());
        }

        return "product-add";
    }

    @PostMapping("/manage/add")
    public String addProduct(@Valid AddProductDTO addProductDTO,
                                  BindingResult bindingResult,
                                  RedirectAttributes redirectAttributes) throws IOException {
        if(bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute(flashAttributeDTO, addProductDTO);
            redirectAttributes.addFlashAttribute(BINDING_RESULT_PATH + DOT + flashAttributeDTO, bindingResult);

            return "redirect:/products/manage/add";
        }

        FileUtils.saveImageLocally(addProductDTO.getImage());

        productService.createProduct(addProductDTO);

        return "redirect:/products/manage/add";
    }

}
