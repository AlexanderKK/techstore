package com.techx7.techstore.service.impl;

import com.techx7.techstore.exception.EntityNotFoundException;
import com.techx7.techstore.model.dto.category.AddCategoryDTO;
import com.techx7.techstore.model.dto.category.CategoryDTO;
import com.techx7.techstore.model.entity.Category;
import com.techx7.techstore.repository.CategoryRepository;
import com.techx7.techstore.testUtils.TestData;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Sort;
import org.springframework.mock.web.MockMultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CategoryServiceImplTest {

    @Mock
    private CategoryRepository categoryRepository;

    @Mock
    private ModelMapper modelMapper;

    @InjectMocks
    private CategoryServiceImpl categoryService;

    private MockMultipartFile mockMultipartFile;

    @BeforeEach
    void testSetUp() {
        mockMultipartFile = TestData.createMultipartFile();
    }

    @Test
    void testCreateCategoryShouldSaveCategory() throws IOException {
        // Arrange
        AddCategoryDTO addCategoryDTO = new AddCategoryDTO();

        addCategoryDTO.setImage(mockMultipartFile);

        Category category = new Category();

        when(modelMapper.map(addCategoryDTO, Category.class)).thenReturn(category);

        // Act
        categoryService.createCategory(addCategoryDTO);

        // Assert
        verify(categoryRepository, times(1)).save(category);
    }

    @Test
    void testGetAllCategoriesShouldReturnAllCategories() {
        // Arrange
        List<Category> categories = new ArrayList<>();
        categories.add(new Category());
        when(categoryRepository.findAll(Sort.by("name"))).thenReturn(categories);

        // Act
        List<CategoryDTO> result = categoryService.getAllCategories();

        // Assert
        assertEquals(categories.size(), result.size());
        verify(modelMapper, times(categories.size())).map(any(Category.class), eq(CategoryDTO.class));
    }

    @Test
    void testDeleteAllCategoriesShouldDeleteAllCategories() {
        // Act
        categoryService.deleteAllCategories();

        // Assert
        verify(categoryRepository, times(1)).deleteAll();
    }

    @Test
    void testDeleteCategoryByUuidShouldDeleteCategory() {
        // Arrange
        UUID uuid = UUID.randomUUID();

        // Act
        categoryService.deleteCategoryByUuid(uuid);

        // Assert
        verify(categoryRepository, times(1)).deleteByUuid(uuid);
    }

    @Test
    void testGetCategoryByUuidNonExistingUuidShouldReturnCategory() {
        // Arrange
        UUID uuid = UUID.randomUUID();
        Category category = new Category();
        when(categoryRepository.findByUuid(uuid)).thenReturn(Optional.of(category));
        when(modelMapper.map(category, CategoryDTO.class)).thenReturn(new CategoryDTO());

        // Act
        CategoryDTO result = categoryService.getCategoryByUuid(uuid);

        // Assert
        assertNotNull(result);
        verify(categoryRepository, times(1)).findByUuid(uuid);
        verify(modelMapper, times(1)).map(category, CategoryDTO.class);
    }

    @Test
    void testGetCategoryByUuidNonExistingUuidShouldThrowEntityNotFoundException() {
        // Arrange
        UUID uuid = UUID.randomUUID();
        when(categoryRepository.findByUuid(uuid)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(EntityNotFoundException.class, () -> categoryService.getCategoryByUuid(uuid));
        verify(categoryRepository, times(1)).findByUuid(uuid);
    }

    @Test
    void testEditCategoryShouldUpdateCategory() throws IOException {
        // Arrange
        CategoryDTO categoryDTO = new CategoryDTO();
        categoryDTO.setImageUrl("test.png");
        categoryDTO.setImage(mockMultipartFile);

        Category category = new Category();
        when(categoryRepository.findByUuid(categoryDTO.getUuid())).thenReturn(Optional.of(category));

        // Act
        categoryService.editCategory(categoryDTO);

        // Assert
        verify(categoryRepository, times(1)).save(category);
    }

}
