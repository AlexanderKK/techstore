package com.techx7.techstore.web;

import com.techx7.techstore.exception.EntityNotFoundException;
import com.techx7.techstore.model.dto.category.CategoryDTO;
import com.techx7.techstore.model.dto.manufacturer.ManufacturerWithModelsDTO;
import com.techx7.techstore.model.dto.product.AddProductDTO;
import com.techx7.techstore.model.dto.product.ProductDTO;
import com.techx7.techstore.model.dto.product.ProductDetailsDTO;
import com.techx7.techstore.service.CategoryService;
import com.techx7.techstore.service.ManufacturerService;
import com.techx7.techstore.service.ProductService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.IntStream;

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

    @GetMapping
    public String getProducts(Model model,
                              @PageableDefault(size = 4, sort = "discountPercentage") Pageable pageable,
                              @RequestParam("page") Optional<Integer> page,
                              @RequestParam("size") Optional<Integer> size) {
        Page<ProductDTO> products = productService.getAllProducts(pageable);

        model.addAttribute("products", products);

        int currentPage = page.orElse(1);
        int pageSize = size.orElse(4);

        Page<ProductDTO> productPage =
                productService.findPaginated(PageRequest.of(currentPage - 1, pageSize));

        model.addAttribute("productPage", productPage);

        int totalPages = products.getTotalPages();
        if (totalPages > 0) {
            List<Integer> pageNumbers = IntStream.rangeClosed(1, totalPages)
                    .boxed()
                    .toList();

            model.addAttribute("pageNumbers", pageNumbers);
        }

        return "products";
    }

    @GetMapping("/add")
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

    @PostMapping("/add")
    public String addProduct(@Valid AddProductDTO addProductDTO,
                                  BindingResult bindingResult,
                                  RedirectAttributes redirectAttributes) throws IOException {
        if(bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute(flashAttributeDTO, addProductDTO);
            redirectAttributes.addFlashAttribute(BINDING_RESULT_PATH + DOT + flashAttributeDTO, bindingResult);

            return "redirect:/products/add";
        }

        productService.createProduct(addProductDTO);

        return "redirect:/products";
    }

    @GetMapping("/detail/{uuid}")
    public String productDetails(Model model,
                                 @PathVariable("uuid") UUID uuid) {
        ProductDetailsDTO productDetailsDTO = productService.getProductDetailsByUuid(uuid);

        model.addAttribute("product", productDetailsDTO);

        return "product-details";
    }

    @ExceptionHandler(EntityNotFoundException.class)
    public String handleProductError(EntityNotFoundException e) {
        System.out.println(e.getMessage());

        return "redirect:/products";
    }

}
