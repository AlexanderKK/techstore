package com.techx7.techstore.repository;

import com.techx7.techstore.model.entity.Category;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ContextConfiguration;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@ContextConfiguration(classes = {CategoryRepository.class})
@EnableAutoConfiguration
@EntityScan(basePackages = {"com.techx7.techstore.model.entity"})
@DataJpaTest
class CategoryRepositoryTest {

    @Autowired
    private CategoryRepository categoryRepository;

    @BeforeEach
    void setUp() {
        categoryRepository.deleteAll();
    }

    @AfterEach
    void tearDown() {
        categoryRepository.deleteAll();
    }

    @Test
    void testFindByName() {
        // Arrange
        Category category = new Category();
        category.setName("Test Category");

        categoryRepository.save(category);

        // Act
        Optional<Category> foundCategory = categoryRepository.findByName(category.getName());

        // Assert
        assertTrue(foundCategory.isPresent());
        assertEquals(category.getId(), foundCategory.get().getId());
    }

    @Test
    void testDeleteByUuid() {
        // Arrange
        Category category = new Category();
        category.setUuid(UUID.randomUUID());

        // Act
        categoryRepository.deleteByUuid(category.getUuid());

        // Assert
        Optional<Category> deletedCategory = categoryRepository.findByUuid(category.getUuid());

        assertFalse(deletedCategory.isPresent());
    }

    @Test
    void testFindByUuid() {
        // Arrange
        Category category = new Category();
        category.setUuid(UUID.randomUUID());
        category.setName("Test Category");

        categoryRepository.save(category);

        // Act
        Optional<Category> foundCategory = categoryRepository.findByUuid(category.getUuid());

        // Assert
        assertTrue(foundCategory.isPresent());
        assertEquals(category.getId(), foundCategory.get().getId());
    }

}
