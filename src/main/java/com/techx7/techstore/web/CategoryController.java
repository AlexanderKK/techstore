package com.techx7.techstore.web;

import com.techx7.techstore.model.dto.category.AddCategoryDTO;
import com.techx7.techstore.model.dto.category.CategoryDTO;
import com.techx7.techstore.service.CategoryService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

import static com.techx7.techstore.constant.Paths.BINDING_RESULT_PATH;
import static com.techx7.techstore.constant.Paths.DOT;

@Controller
@RequestMapping("/categories")
public class CategoryController {

    private final static String flashAttributeDTO = "addCategoryDTO";
    private final CategoryService categoryService;

    @Autowired
    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @GetMapping
    public String manageCategory(Model model) {
        List<CategoryDTO> categoryDTOs = categoryService.getAllCategories();

        model.addAttribute("categories", categoryDTOs);

        if(!model.containsAttribute(flashAttributeDTO)) {
            model.addAttribute(flashAttributeDTO, new AddCategoryDTO());
        }

        return "categories";
    }

    @PostMapping("/add")
    public String addCategory(@Valid AddCategoryDTO addCategoryDTO,
                                  BindingResult bindingResult,
                                  RedirectAttributes redirectAttributes) throws IOException {
        if(bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute(flashAttributeDTO, addCategoryDTO);
            redirectAttributes.addFlashAttribute(BINDING_RESULT_PATH + DOT + flashAttributeDTO, bindingResult);

            return "redirect:/categories";
        }

        categoryService.createCategory(addCategoryDTO);

        return "redirect:/categories";
    }

    @GetMapping("/edit/{uuid}")
    public String getCategory(Model model,
                          @PathVariable("uuid") UUID uuid) {
        CategoryDTO categoryDTO = categoryService.getCategoryByUuid(uuid);

        if(!model.containsAttribute("categoryToEdit")) {
            model.addAttribute("categoryToEdit", categoryDTO);
        }

        return "category-edit";
    }

    @PatchMapping("/edit")
    public String editCategory(@Valid CategoryDTO categoryDTO,
                           BindingResult bindingResult,
                           RedirectAttributes redirectAttributes) throws IOException {
        if(bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute("categoryToEdit", categoryDTO);
            redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.categoryToEdit", bindingResult);

            return "redirect:/categories/edit/" + categoryDTO.getUuid();
        }

        categoryService.editCategory(categoryDTO);

        return "redirect:/categories";
    }

    @DeleteMapping("/delete/{uuid}")
    public String deleteCategory(@PathVariable("uuid") UUID uuid) {
        categoryService.deleteCategoryByUuid(uuid);

        return "redirect:/categories";
    }

    @DeleteMapping("/delete-all")
    public String deleteAllCategories() {
        categoryService.deleteAllCategories();

        return "redirect:/categories";
    }

}
