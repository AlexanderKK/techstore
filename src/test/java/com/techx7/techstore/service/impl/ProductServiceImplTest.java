package com.techx7.techstore.service.impl;

import com.techx7.techstore.exception.EntityNotFoundException;
import com.techx7.techstore.model.dto.product.AddProductDTO;
import com.techx7.techstore.model.dto.product.ProductDTO;
import com.techx7.techstore.model.dto.product.ProductDetailsDTO;
import com.techx7.techstore.model.entity.Manufacturer;
import com.techx7.techstore.model.entity.Model;
import com.techx7.techstore.model.entity.Product;
import com.techx7.techstore.repository.ProductRepository;
import com.techx7.techstore.service.CloudinaryService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
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

@MockitoSettings(strictness = Strictness.LENIENT)
@ExtendWith(MockitoExtension.class)
class ProductServiceImplTest {

    @Mock
    private ModelMapper mapper;

    @Mock
    private ProductRepository productRepository;

    @Mock
    private CloudinaryService cloudinaryService;

    @InjectMocks
    private ProductServiceImpl productService;

    @Test
    void testCreateProductShouldSaveProduct() throws IOException {
        // Arrange
        AddProductDTO addProductDTO = new AddProductDTO();
        addProductDTO.setImage(createMultipartFile());

        Product product = new Product();

        Model model = new Model();
        model.setManufacturer(new Manufacturer());

        product.setModel(model);

        when(mapper.map(addProductDTO, Product.class)).thenReturn(product);
        when(cloudinaryService.uploadFile(any(), anyString(), anyString())).thenReturn("dummyImageUrl");

        // Act
        productService.createProduct(addProductDTO);

        // Assert
        verify(productRepository).save(product);
    }

    @Test
    void testGetAllProductsShouldReturnAllProducts() {
        // Arrange
        Pageable pageable = Pageable.ofSize(1);
        List<Product> productList = new ArrayList<>();

        Product product = new Product();
        product.setId(1L);

        Page<Product> productPage= new PageImpl<>(productList);

        when(productRepository.findAll(pageable)).thenReturn(productPage);

        ProductDTO productDTO = new ProductDTO();
        productDTO.setUuid(UUID.randomUUID());

        when(mapper.map(any(Product.class), eq(ProductDTO.class))).thenReturn(productDTO);

        // Act
        Page<ProductDTO> result = productService.getAllPageableProducts(pageable);

        // Assert
        assertEquals(productList.size(), result.getContent().size());
    }

    @Test
    void testGetAllProductsWithDiscount() {
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
    void testFindPaginated() {
        // Arrange
        Pageable pageable = Pageable.ofSize(4);

        List<Product> productList = new ArrayList<>();

        Page<Product> productPage = new PageImpl<>(productList);

        when(productRepository.findAll(pageable)).thenReturn(productPage);

        ProductDTO productDTO = new ProductDTO();
        productDTO.setModel("Test model");

        when(mapper.map(any(Product.class), eq(ProductDTO.class))).thenReturn(productDTO);

        // Act
        Page<ProductDTO> result = productService.findPaginated(pageable);

        // Assert
        assertEquals(productList.size(), result.getContent().size());
    }

    @Test
    void testGetProductByUuid() {
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
    void testGetProductDetailsByUuid() {
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
    void testGetProductDetailsByUuidThrowEntityNotFoundException() {
        // Arrange
        UUID uuid = UUID.randomUUID();
        when(productRepository.findByUuid(uuid)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(EntityNotFoundException.class, () -> productService.getProductDetailsByUuid(uuid));
    }

}
