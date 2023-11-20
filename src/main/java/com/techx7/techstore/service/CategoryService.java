package com.techx7.techstore.service;

import com.techx7.techstore.model.dto.category.AddCategoryDTO;
import com.techx7.techstore.model.dto.category.CategoryDTO;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

public interface CategoryService {

    void createCategory(AddCategoryDTO addCategoryDTO);

    List<CategoryDTO> getAllCategories();

    void deleteAllCategories();

    void deleteCategoryByUuid(UUID uuid);

    CategoryDTO getCategoryByUuid(UUID uuid);

    void editCategory(CategoryDTO categoryDTO) throws IOException;

}
