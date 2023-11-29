package com.techx7.techstore.repository;

import com.techx7.techstore.model.entity.Product;
import com.techx7.techstore.testUtils.TestData;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ContextConfiguration;

import java.util.Optional;

@ContextConfiguration(classes = {ProductRepository.class, TestData.class})
@EnableAutoConfiguration
@EntityScan(basePackages = {"com.techx7.techstore.model.entity"})
@DataJpaTest
class ProductRepositoryTest {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private TestData testData;

    @BeforeEach
    void setUp() {
        productRepository.deleteAll();
    }

    @AfterEach
    void tearDown() {
        productRepository.deleteAll();
    }

    @Test
    void testFindByModelId() {
        // Arrange
        Product product = testData.createProduct();

        productRepository.save(product);

        // Act
        Optional<Product> foundProduct = productRepository.findByModelId(product.getId());

        // Assert
        Assertions.assertTrue(foundProduct.isPresent());
        Assertions.assertEquals(product.getId(), foundProduct.get().getId());
    }

    @Test
    void testFindByUuid() {
        // Arrange
        Product product = testData.createProduct();

        productRepository.save(product);

        // Act
        Optional<Product> foundProduct = productRepository.findByUuid(product.getUuid());

        // Assert
        Assertions.assertTrue(foundProduct.isPresent());
        Assertions.assertEquals(product.getId(), foundProduct.get().getId());
    }

}
