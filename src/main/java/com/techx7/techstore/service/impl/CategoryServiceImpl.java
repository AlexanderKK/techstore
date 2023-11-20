package com.techx7.techstore.service.impl;

import com.techx7.techstore.exception.EntityNotFoundException;
import com.techx7.techstore.model.dto.category.AddCategoryDTO;
import com.techx7.techstore.model.dto.category.CategoryDTO;
import com.techx7.techstore.model.entity.Category;
import com.techx7.techstore.repository.CategoryRepository;
import com.techx7.techstore.service.CategoryService;
import jakarta.transaction.Transactional;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

import static com.techx7.techstore.constant.Messages.ENTITY_NOT_FOUND;
import static com.techx7.techstore.util.FileUtils.saveFileLocally;

@Service
public class CategoryServiceImpl implements CategoryService {

    private final ModelMapper mapper;
    private final CategoryRepository categoryRepository;

    @Autowired
    public CategoryServiceImpl(ModelMapper mapper,
                               CategoryRepository categoryRepository) {
        this.mapper = mapper;
        this.categoryRepository = categoryRepository;
    }

    @Override
    public void createCategory(AddCategoryDTO addCategoryDTO) throws IOException {
        saveFileLocally(addCategoryDTO.getImage());

        Category category = mapper.map(addCategoryDTO, Category.class);

        categoryRepository.save(category);
    }

    @Override
    public List<CategoryDTO> getAllCategories() {
        return categoryRepository.findAll(Sort.by("name"))
                .stream()
                .map(category -> mapper.map(category, CategoryDTO.class))
                .toList();
    }

    @Override
    public void deleteAllCategories() {
        categoryRepository.deleteAll();
    }

    @Override
    @Transactional
    public void deleteCategoryByUuid(UUID uuid) {
        categoryRepository.deleteByUuid(uuid);
    }

    @Override
    public CategoryDTO getCategoryByUuid(UUID uuid) {
        return categoryRepository.findByUuid(uuid)
                .map(category -> mapper.map(category, CategoryDTO.class))
                .orElseThrow(() -> new EntityNotFoundException(String.format(ENTITY_NOT_FOUND, "Category")));
    }

    @Override
    public void editCategory(CategoryDTO categoryDTO) throws IOException {
        saveFileLocally(categoryDTO.getImage());

        Category category = categoryRepository.findByUuid(categoryDTO.getUuid())
                .orElseThrow(() -> new EntityNotFoundException(String.format(ENTITY_NOT_FOUND, "Category")));

        category.editCategory(categoryDTO);

        categoryRepository.save(category);
    }

}
