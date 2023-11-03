package com.techx7.techstore.web;

import com.techx7.techstore.model.dto.category.AddCategoryDTO;
import com.techx7.techstore.model.dto.category.CategoryHomeDTO;
import com.techx7.techstore.service.CategoryService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.BufferedOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.UUID;

import static com.techx7.techstore.constant.FilePaths.RESOURCES_IMAGES_DIRECTORY;

@Controller
@RequestMapping("/categories")
public class CategoryController {

    private final CategoryService categoryService;

    @Autowired
    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @GetMapping("/manage")
    public String manageCategory(Model model) {
        List<CategoryHomeDTO> categoryHomeDTOs = categoryService.getAllCategories();
        model.addAttribute("categories", categoryHomeDTOs);

        if(!model.containsAttribute("addCategoryDTO")) {
            model.addAttribute("addCategoryDTO", new AddCategoryDTO());
        }

        return "categories";
    }

    @PostMapping("/manage/add")
    public String addCategory(@Valid AddCategoryDTO addCategoryDTO,
                                  BindingResult bindingResult,
                                  RedirectAttributes redirectAttributes) throws IOException {
        if(bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute("addCategoryDTO", addCategoryDTO);
            redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.addCategoryDTO", bindingResult);

            return "redirect:/categories/manage";
        }

        saveImageLocally(addCategoryDTO.getImage());

        categoryService.createCategory(addCategoryDTO);

        return "redirect:/categories/manage";
    }

    private void saveImageLocally(MultipartFile image) throws IOException {
        byte[] imgBytes = image.getBytes();

        if(imgBytes.length != 0) {
            BufferedOutputStream bufferedWriter = new BufferedOutputStream(
                    new FileOutputStream(RESOURCES_IMAGES_DIRECTORY + image.getOriginalFilename()));

            bufferedWriter.write(imgBytes);
            bufferedWriter.close();
        }
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
