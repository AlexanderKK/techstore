package com.techx7.techstore.web;

import com.techx7.techstore.model.dto.category.AddCategoryDTO;
import com.techx7.techstore.model.dto.category.CategoryDTO;
import com.techx7.techstore.service.CategoryService;
import com.techx7.techstore.util.FileUtils;
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

import static com.techx7.techstore.constant.Paths.*;

@Controller
@RequestMapping("/categories")
public class CategoryController {

    private final static String flashAttributeDTO = "addCategoryDTO";
    private final CategoryService categoryService;

    @Autowired
    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @GetMapping("/manage")
    public String manageCategory(Model model) {
        List<CategoryDTO> categoryDTOS = categoryService.getAllCategories();
        model.addAttribute("categories", categoryDTOS);

        if(!model.containsAttribute(flashAttributeDTO)) {
            model.addAttribute(flashAttributeDTO, new AddCategoryDTO());
        }

        return "categories";
    }

    @PostMapping("/manage/add")
    public String addCategory(@Valid AddCategoryDTO addCategoryDTO,
                                  BindingResult bindingResult,
                                  RedirectAttributes redirectAttributes) throws IOException {
        if(bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute(flashAttributeDTO, addCategoryDTO);
            redirectAttributes.addFlashAttribute(BINDING_RESULT_PATH + DOT + flashAttributeDTO, bindingResult);

            return "redirect:/categories/manage";
        }

        FileUtils.saveImageLocally(addCategoryDTO.getImage());

        categoryService.createCategory(addCategoryDTO);

        return "redirect:/categories/manage";
    }

    @DeleteMapping("/manage/delete-all")
    public String deleteAllCategories() {
        categoryService.deleteAllCategories();

        return "redirect:/categories/manage";
    }

    @DeleteMapping("/manage/delete/{uuid}")
    public String deleteCategory(@PathVariable("uuid") UUID uuid) {
        categoryService.deleteCategoryByUuid(uuid);

        return "redirect:/categories/manage";
    }

}
