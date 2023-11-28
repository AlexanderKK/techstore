package com.techx7.techstore.service.impl;

import com.techx7.techstore.exception.EntityNotFoundException;
import com.techx7.techstore.model.dto.product.AddProductDTO;
import com.techx7.techstore.model.dto.product.ProductDTO;
import com.techx7.techstore.model.dto.product.ProductDetailsDTO;
import com.techx7.techstore.model.entity.Product;
import com.techx7.techstore.repository.ProductRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static com.techx7.techstore.testUtils.TestData.createMultipartFile;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProductServiceImplTest {

    @Mock
    private ModelMapper mapper;

    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private ProductServiceImpl productService;

    @Test
    void testCreateProductShouldSaveProduct() throws IOException {
        // Arrange
        AddProductDTO addProductDTO = new AddProductDTO();
        addProductDTO.setImage(createMultipartFile());

        Product product = new Product();
        when(mapper.map(addProductDTO, Product.class)).thenReturn(product);

        // Act
        productService.createProduct(addProductDTO);

        // Assert
        verify(productRepository, times(1)).save(product);
    }

    @Test
    void testGetAllProductsShouldReturnAllProducts() {
        // Arrange
        Pageable pageable = Pageable.unpaged();
        List<Product> productList = new ArrayList<>();

        productList.add(new Product());
        Page<Product> productPage = new PageImpl<>(productList);

        when(productRepository.findAll(pageable)).thenReturn(productPage);
        when(mapper.map(any(Product.class), eq(ProductDTO.class))).thenReturn(new ProductDTO());

        // Act
        Page<ProductDTO> result = productService.getAllProducts(pageable);

        // Assert
        assertEquals(productList.size(), result.getContent().size());
    }

    @Test
    void testGetAllProductsWithDiscountShouldReturnProductsWithDiscount() {
        // Arrange
        List<Product> productList = new ArrayList<>();

        Product product = new Product();

        productList.add(product);

        when(productRepository.findAll()).thenReturn(productList);

        ProductDTO productDTO = new ProductDTO();

        productDTO.setPrice(BigDecimal.valueOf(2999));
        productDTO.setDiscountPrice(BigDecimal.TEN);

        when(mapper.map(any(Product.class), eq(ProductDTO.class))).thenReturn(productDTO);

        // Act
        List<ProductDTO> result = productService.getAllProductsWithDiscount();

        // Assert
        assertEquals(productList.size(), result.size());
    }

    @Test
    void testFindPaginatedShouldReturnPaginatedProducts() {
        // Arrange
        Pageable pageable = Pageable.ofSize(4);

        List<Product> productList = new ArrayList<>();

        productList.add(new Product());
        Page<Product> productPage = new PageImpl<>(productList);

        when(productRepository.findAll(pageable)).thenReturn(productPage);
        when(mapper.map(any(Product.class), eq(ProductDTO.class))).thenReturn(new ProductDTO());

        // Act
        Page<ProductDTO> result = productService.findPaginated(pageable);

        // Assert
        assertEquals(productList.size(), result.getContent().size());
    }

    @Test
    void testGetProductByUuidShouldReturnProduct() {
        // Arrange
        UUID uuid = UUID.randomUUID();
        Product product = new Product();

        when(productRepository.findByUuid(uuid)).thenReturn(Optional.of(product));
        when(mapper.map(product, ProductDTO.class)).thenReturn(new ProductDTO());

        // Act
        ProductDTO result = productService.getProductByUuid(uuid);

        // Assert
        assertNotNull(result);
    }

    @Test
    void testGetProductByUuidShouldThrowEntityNotFoundException() {
        //Arrange
        UUID uuid = UUID.randomUUID();
        when(productRepository.findByUuid(uuid)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(EntityNotFoundException.class, () -> productService.getProductByUuid(uuid));
    }

    @Test
    void testGetProductDetailsByUuidShouldReturnProductDetails() {
        // Arrange
        UUID uuid = UUID.randomUUID();
        Product product = new Product();
        when(productRepository.findByUuid(uuid)).thenReturn(Optional.of(product));
        when(mapper.map(product, ProductDetailsDTO.class)).thenReturn(new ProductDetailsDTO());

        // Act
        ProductDetailsDTO result = productService.getProductDetailsByUuid(uuid);

        // Assert
        assertNotNull(result);
    }

    @Test
    void testGetProductDetailsByUuidShouldThrowEntityNotFoundException() {
        // Arrange
        UUID uuid = UUID.randomUUID();
        when(productRepository.findByUuid(uuid)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(EntityNotFoundException.class, () -> productService.getProductDetailsByUuid(uuid));
    }

}
