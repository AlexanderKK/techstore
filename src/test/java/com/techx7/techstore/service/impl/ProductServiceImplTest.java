package com.techx7.techstore.service.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.atLeast;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.techx7.techstore.config.ApplicationConfiguration;
import com.techx7.techstore.exception.EntityNotFoundException;
import com.techx7.techstore.model.dto.product.AddProductDTO;
import com.techx7.techstore.model.dto.product.ProductDTO;
import com.techx7.techstore.model.dto.product.ProductDetailsDTO;
import com.techx7.techstore.model.entity.Manufacturer;
import com.techx7.techstore.model.entity.Model;
import com.techx7.techstore.model.entity.Product;
import com.techx7.techstore.model.multipart.MultipartFileImpl;
import com.techx7.techstore.repository.ProductRepository;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.multipart.MultipartFile;

@RunWith(SpringRunner.class)
@SpringBootTest
@ContextConfiguration(classes= {ApplicationConfiguration.class})
@ExtendWith(MockitoExtension.class)
class ProductServiceImplTest {

    @Spy
    @Mock
    private ModelMapper modelMapper;

    @Mock
    private ProductRepository productRepository;

    private ProductServiceImpl productServiceImpl;

    @BeforeEach
    void setUp() {
        productServiceImpl = new ProductServiceImpl(modelMapper, productRepository);
    }

    /**
    * Method under test: {@link ProductServiceImpl#createProduct(AddProductDTO)}
    */
    @Test
    void testCreateProduct() throws IOException {
    Manufacturer manufacturer = new Manufacturer();
    manufacturer.setCreated(LocalDate.of(1970, 1, 1).atStartOfDay());
    manufacturer.setDescription("The characteristics of someone or something");
    manufacturer.setId(1L);
    manufacturer.setImageUrl("https://example.org/example");
    manufacturer.setModels(new HashSet<>());
    manufacturer.setModified(LocalDate.of(1970, 1, 1).atStartOfDay());
    manufacturer.setName("Name");
    manufacturer.setUuid(UUID.randomUUID());

    Model model = new Model();
    model.setCreated(LocalDate.of(1970, 1, 1).atStartOfDay());
    model.setId(1L);
    model.setManufacturer(manufacturer);
    model.setModified(LocalDate.of(1970, 1, 1).atStartOfDay());
    model.setName("Name");
    model.setProducts(new HashSet<>());
    model.setUuid(UUID.randomUUID());

    Product product = new Product();
    product.setAvailableQuantity(1);
    product.setCategories(new HashSet<>());
    product.setCreated(LocalDate.of(1970, 1, 1).atStartOfDay());
    product.setDescription("The characteristics of someone or something");
    product.setDiscountPercentage(new BigDecimal("2.3"));
    product.setId(1L);
    product.setImageUrl("https://example.org/example");
    product.setInitialQuantity(1);
    product.setModel(model);
    product.setModified(LocalDate.of(1970, 1, 1).atStartOfDay());
    product.setPrice(new BigDecimal("2.3"));
    product.setSpecification("Specification");
    product.setUuid(UUID.randomUUID());
    when(modelMapper.map(Mockito.<Object>any(), Mockito.<Class<Object>>any())).thenReturn(product);

    Manufacturer manufacturer2 = new Manufacturer();
    manufacturer2.setCreated(LocalDate.of(1970, 1, 1).atStartOfDay());
    manufacturer2.setDescription("The characteristics of someone or something");
    manufacturer2.setId(1L);
    manufacturer2.setImageUrl("https://example.org/example");
    manufacturer2.setModels(new HashSet<>());
    manufacturer2.setModified(LocalDate.of(1970, 1, 1).atStartOfDay());
    manufacturer2.setName("Name");
    manufacturer2.setUuid(UUID.randomUUID());

    Model model2 = new Model();
    model2.setCreated(LocalDate.of(1970, 1, 1).atStartOfDay());
    model2.setId(1L);
    model2.setManufacturer(manufacturer2);
    model2.setModified(LocalDate.of(1970, 1, 1).atStartOfDay());
    model2.setName("Name");
    model2.setProducts(new HashSet<>());
    model2.setUuid(UUID.randomUUID());

    Product product2 = new Product();
    product2.setAvailableQuantity(1);
    product2.setCategories(new HashSet<>());
    product2.setCreated(LocalDate.of(1970, 1, 1).atStartOfDay());
    product2.setDescription("The characteristics of someone or something");
    product2.setDiscountPercentage(new BigDecimal("2.3"));
    product2.setId(1L);
    product2.setImageUrl("https://example.org/example");
    product2.setInitialQuantity(1);
    product2.setModel(model2);
    product2.setModified(LocalDate.of(1970, 1, 1).atStartOfDay());
    product2.setPrice(new BigDecimal("2.3"));
    product2.setSpecification("Specification");
    product2.setUuid(UUID.randomUUID());
    when(productRepository.save(Mockito.<Product>any())).thenReturn(product2);
    AddProductDTO addProductDTO = mock(AddProductDTO.class);
    when(addProductDTO.getImage()).thenReturn(new MultipartFileImpl("Name", new ByteArrayInputStream(new byte[]{})));
    doNothing().when(addProductDTO).setCategories(Mockito.<String>any());
    doNothing().when(addProductDTO).setDescription(Mockito.<String>any());
    doNothing().when(addProductDTO).setDiscountPercentage(Mockito.<String>any());
    doNothing().when(addProductDTO).setImage(Mockito.<MultipartFile>any());
    doNothing().when(addProductDTO).setInitialQuantity(Mockito.<Integer>any());
    doNothing().when(addProductDTO).setModel(Mockito.<Long>any());
    doNothing().when(addProductDTO).setPrice(Mockito.<String>any());
    doNothing().when(addProductDTO).setSpecification(Mockito.<String>any());
    addProductDTO.setCategories("Categories");
    addProductDTO.setDescription("The characteristics of someone or something");
    addProductDTO.setDiscountPercentage("3");
    addProductDTO.setImage(new MultipartFileImpl("Name", new ByteArrayInputStream("TESTTESTTEST".getBytes("UTF-8"))));
    addProductDTO.setInitialQuantity(1);
    addProductDTO.setModel(1L);
    addProductDTO.setPrice("Price");
    addProductDTO.setSpecification("Specification");
    productServiceImpl.createProduct(addProductDTO);
    verify(addProductDTO).getImage();
    verify(addProductDTO).setCategories(Mockito.<String>any());
    verify(addProductDTO).setDescription(Mockito.<String>any());
    verify(addProductDTO).setDiscountPercentage(Mockito.<String>any());
    verify(addProductDTO).setImage(Mockito.<MultipartFile>any());
    verify(addProductDTO).setInitialQuantity(Mockito.<Integer>any());
    verify(addProductDTO).setModel(Mockito.<Long>any());
    verify(addProductDTO).setPrice(Mockito.<String>any());
    verify(addProductDTO).setSpecification(Mockito.<String>any());
    verify(modelMapper).map(Mockito.<Object>any(), Mockito.<Class<Object>>any());
    verify(productRepository).save(Mockito.<Product>any());
    }

    /**
    * Method under test:  {@link ProductServiceImpl#getAllProducts(Pageable)}
    */
    @Test
    void testGetAllProducts() {
    when(productRepository.findAll(Mockito.<Pageable>any())).thenReturn(new PageImpl<>(new ArrayList<>()));
    Page<ProductDTO> actualAllProducts = productServiceImpl.getAllProducts(null);
    verify(productRepository).findAll(Mockito.<Pageable>any());
    assertTrue(actualAllProducts.toList().isEmpty());
    }

    /**
    * Method under test: {@link ProductServiceImpl#getAllProducts(Pageable)}
    */
    @Test
    void testGetAllProducts2() {
    ProductDTO productDTO = new ProductDTO();
    productDTO.setCategories(new HashSet<>());
    productDTO.setDiscountPrice(new BigDecimal("2.3"));
    productDTO.setImageUrl("https://example.org/example");
    productDTO.setManufacturer("Manufacturer");
    productDTO.setModel("Model");
    productDTO.setPrice(new BigDecimal("2.3"));
    productDTO.setUuid(UUID.randomUUID());
    when(modelMapper.map(Mockito.<Object>any(), Mockito.<Class<ProductDTO>>any())).thenReturn(productDTO);

    Manufacturer manufacturer = new Manufacturer();
    manufacturer.setCreated(LocalDate.of(1970, 1, 1).atStartOfDay());
    manufacturer.setDescription("The characteristics of someone or something");
    manufacturer.setId(1L);
    manufacturer.setImageUrl("https://example.org/example");
    manufacturer.setModels(new HashSet<>());
    manufacturer.setModified(LocalDate.of(1970, 1, 1).atStartOfDay());
    manufacturer.setName("Displaying products");
    manufacturer.setUuid(UUID.randomUUID());

    Model model = new Model();
    model.setCreated(LocalDate.of(1970, 1, 1).atStartOfDay());
    model.setId(1L);
    model.setManufacturer(manufacturer);
    model.setModified(LocalDate.of(1970, 1, 1).atStartOfDay());
    model.setName("Displaying products");
    model.setProducts(new HashSet<>());
    model.setUuid(UUID.randomUUID());

    Product product = new Product();
    product.setAvailableQuantity(1);
    product.setCategories(new HashSet<>());
    product.setCreated(LocalDate.of(1970, 1, 1).atStartOfDay());
    product.setDescription("The characteristics of someone or something");
    product.setDiscountPercentage(new BigDecimal("2.3"));
    product.setId(1L);
    product.setImageUrl("https://example.org/example");
    product.setInitialQuantity(1);
    product.setModel(model);
    product.setModified(LocalDate.of(1970, 1, 1).atStartOfDay());
    product.setPrice(new BigDecimal("2.3"));
    product.setSpecification("Displaying products");
    product.setUuid(UUID.randomUUID());

    ArrayList<Product> content = new ArrayList<>();
    content.add(product);
    PageImpl<Product> pageImpl = new PageImpl<>(content);
    when(productRepository.findAll(Mockito.<Pageable>any())).thenReturn(pageImpl);
    Page<ProductDTO> actualAllProducts = productServiceImpl.getAllProducts(null);
    verify(modelMapper).map(Mockito.<Object>any(), Mockito.<Class<ProductDTO>>any());
    verify(productRepository).findAll(Mockito.<Pageable>any());
    assertEquals(1, actualAllProducts.toList().size());
    }

    /**
    * Method under test: {@link ProductServiceImpl#getAllProducts(Pageable)}
    */
    @Test
    void testGetAllProducts3() {
    ProductDTO productDTO = mock(ProductDTO.class);
    when(productDTO.getPrice()).thenThrow(new EntityNotFoundException("An error occurred"));
    when(productDTO.getDiscountPrice()).thenReturn(new BigDecimal("2.3"));
    doNothing().when(productDTO).setCategories(Mockito.<Set<String>>any());
    doNothing().when(productDTO).setDiscountPrice(Mockito.<BigDecimal>any());
    doNothing().when(productDTO).setImageUrl(Mockito.<String>any());
    doNothing().when(productDTO).setManufacturer(Mockito.<String>any());
    doNothing().when(productDTO).setModel(Mockito.<String>any());
    doNothing().when(productDTO).setPrice(Mockito.<BigDecimal>any());
    doNothing().when(productDTO).setUuid(Mockito.<UUID>any());
    productDTO.setCategories(new HashSet<>());
    productDTO.setDiscountPrice(new BigDecimal("2.3"));
    productDTO.setImageUrl("https://example.org/example");
    productDTO.setManufacturer("Manufacturer");
    productDTO.setModel("Model");
    productDTO.setPrice(new BigDecimal("2.3"));
    productDTO.setUuid(UUID.randomUUID());
    when(modelMapper.map(Mockito.<Object>any(), Mockito.<Class<ProductDTO>>any())).thenReturn(productDTO);

    Manufacturer manufacturer = new Manufacturer();
    manufacturer.setCreated(LocalDate.of(1970, 1, 1).atStartOfDay());
    manufacturer.setDescription("The characteristics of someone or something");
    manufacturer.setId(1L);
    manufacturer.setImageUrl("https://example.org/example");
    manufacturer.setModels(new HashSet<>());
    manufacturer.setModified(LocalDate.of(1970, 1, 1).atStartOfDay());
    manufacturer.setName("Displaying products");
    manufacturer.setUuid(UUID.randomUUID());

    Model model = new Model();
    model.setCreated(LocalDate.of(1970, 1, 1).atStartOfDay());
    model.setId(1L);
    model.setManufacturer(manufacturer);
    model.setModified(LocalDate.of(1970, 1, 1).atStartOfDay());
    model.setName("Displaying products");
    model.setProducts(new HashSet<>());
    model.setUuid(UUID.randomUUID());

    Product product = new Product();
    product.setAvailableQuantity(1);
    product.setCategories(new HashSet<>());
    product.setCreated(LocalDate.of(1970, 1, 1).atStartOfDay());
    product.setDescription("The characteristics of someone or something");
    product.setDiscountPercentage(new BigDecimal("2.3"));
    product.setId(1L);
    product.setImageUrl("https://example.org/example");
    product.setInitialQuantity(1);
    product.setModel(model);
    product.setModified(LocalDate.of(1970, 1, 1).atStartOfDay());
    product.setPrice(new BigDecimal("2.3"));
    product.setSpecification("Displaying products");
    product.setUuid(UUID.randomUUID());

    ArrayList<Product> content = new ArrayList<>();
    content.add(product);
    PageImpl<Product> pageImpl = new PageImpl<>(content);
    when(productRepository.findAll(Mockito.<Pageable>any())).thenReturn(pageImpl);
    assertThrows(EntityNotFoundException.class, () -> productServiceImpl.getAllProducts(null));
    verify(productDTO, atLeast(1)).getDiscountPrice();
    verify(productDTO).getPrice();
    verify(productDTO).setCategories(Mockito.<Set<String>>any());
    verify(productDTO).setDiscountPrice(Mockito.<BigDecimal>any());
    verify(productDTO).setImageUrl(Mockito.<String>any());
    verify(productDTO).setManufacturer(Mockito.<String>any());
    verify(productDTO).setModel(Mockito.<String>any());
    verify(productDTO).setPrice(Mockito.<BigDecimal>any());
    verify(productDTO).setUuid(Mockito.<UUID>any());
    verify(modelMapper).map(Mockito.<Object>any(), Mockito.<Class<ProductDTO>>any());
    verify(productRepository).findAll(Mockito.<Pageable>any());
    }

    /**
    * Method under test: {@link ProductServiceImpl#getAllProducts(Pageable)}
    */
    @Test
    void testGetAllProducts4() {
    ProductDTO productDTO = mock(ProductDTO.class);
    when(productDTO.getDiscountPrice()).thenReturn(new BigDecimal("-2.3"));
    doNothing().when(productDTO).setCategories(Mockito.<Set<String>>any());
    doNothing().when(productDTO).setDiscountPrice(Mockito.<BigDecimal>any());
    doNothing().when(productDTO).setImageUrl(Mockito.<String>any());
    doNothing().when(productDTO).setManufacturer(Mockito.<String>any());
    doNothing().when(productDTO).setModel(Mockito.<String>any());
    doNothing().when(productDTO).setPrice(Mockito.<BigDecimal>any());
    doNothing().when(productDTO).setUuid(Mockito.<UUID>any());
    productDTO.setCategories(new HashSet<>());
    productDTO.setDiscountPrice(new BigDecimal("2.3"));
    productDTO.setImageUrl("https://example.org/example");
    productDTO.setManufacturer("Manufacturer");
    productDTO.setModel("Model");
    productDTO.setPrice(new BigDecimal("2.3"));
    productDTO.setUuid(UUID.randomUUID());
    when(modelMapper.map(Mockito.<Object>any(), Mockito.<Class<ProductDTO>>any())).thenReturn(productDTO);

    Manufacturer manufacturer = new Manufacturer();
    manufacturer.setCreated(LocalDate.of(1970, 1, 1).atStartOfDay());
    manufacturer.setDescription("The characteristics of someone or something");
    manufacturer.setId(1L);
    manufacturer.setImageUrl("https://example.org/example");
    manufacturer.setModels(new HashSet<>());
    manufacturer.setModified(LocalDate.of(1970, 1, 1).atStartOfDay());
    manufacturer.setName("Displaying products");
    manufacturer.setUuid(UUID.randomUUID());

    Model model = new Model();
    model.setCreated(LocalDate.of(1970, 1, 1).atStartOfDay());
    model.setId(1L);
    model.setManufacturer(manufacturer);
    model.setModified(LocalDate.of(1970, 1, 1).atStartOfDay());
    model.setName("Displaying products");
    model.setProducts(new HashSet<>());
    model.setUuid(UUID.randomUUID());

    Product product = new Product();
    product.setAvailableQuantity(1);
    product.setCategories(new HashSet<>());
    product.setCreated(LocalDate.of(1970, 1, 1).atStartOfDay());
    product.setDescription("The characteristics of someone or something");
    product.setDiscountPercentage(new BigDecimal("2.3"));
    product.setId(1L);
    product.setImageUrl("https://example.org/example");
    product.setInitialQuantity(1);
    product.setModel(model);
    product.setModified(LocalDate.of(1970, 1, 1).atStartOfDay());
    product.setPrice(new BigDecimal("2.3"));
    product.setSpecification("Displaying products");
    product.setUuid(UUID.randomUUID());

    ArrayList<Product> content = new ArrayList<>();
    content.add(product);
    PageImpl<Product> pageImpl = new PageImpl<>(content);
    when(productRepository.findAll(Mockito.<Pageable>any())).thenReturn(pageImpl);
    Page<ProductDTO> actualAllProducts = productServiceImpl.getAllProducts(null);
    verify(productDTO, atLeast(1)).getDiscountPrice();
    verify(productDTO).setCategories(Mockito.<Set<String>>any());
    verify(productDTO).setDiscountPrice(Mockito.<BigDecimal>any());
    verify(productDTO).setImageUrl(Mockito.<String>any());
    verify(productDTO).setManufacturer(Mockito.<String>any());
    verify(productDTO).setModel(Mockito.<String>any());
    verify(productDTO).setPrice(Mockito.<BigDecimal>any());
    verify(productDTO).setUuid(Mockito.<UUID>any());
    verify(modelMapper).map(Mockito.<Object>any(), Mockito.<Class<ProductDTO>>any());
    verify(productRepository).findAll(Mockito.<Pageable>any());
    assertEquals(1, actualAllProducts.toList().size());
    }

    /**
    * Method under test: {@link ProductServiceImpl#getAllProducts(Pageable)}
    */
    @Test
    void testGetAllProducts5() {
    ProductDTO productDTO = mock(ProductDTO.class);
    when(productDTO.getDiscountPrice()).thenReturn(null);
    doNothing().when(productDTO).setCategories(Mockito.<Set<String>>any());
    doNothing().when(productDTO).setDiscountPrice(Mockito.<BigDecimal>any());
    doNothing().when(productDTO).setImageUrl(Mockito.<String>any());
    doNothing().when(productDTO).setManufacturer(Mockito.<String>any());
    doNothing().when(productDTO).setModel(Mockito.<String>any());
    doNothing().when(productDTO).setPrice(Mockito.<BigDecimal>any());
    doNothing().when(productDTO).setUuid(Mockito.<UUID>any());
    productDTO.setCategories(new HashSet<>());
    productDTO.setDiscountPrice(new BigDecimal("2.3"));
    productDTO.setImageUrl("https://example.org/example");
    productDTO.setManufacturer("Manufacturer");
    productDTO.setModel("Model");
    productDTO.setPrice(new BigDecimal("2.3"));
    productDTO.setUuid(UUID.randomUUID());
    when(modelMapper.map(Mockito.<Object>any(), Mockito.<Class<ProductDTO>>any())).thenReturn(productDTO);

    Manufacturer manufacturer = new Manufacturer();
    manufacturer.setCreated(LocalDate.of(1970, 1, 1).atStartOfDay());
    manufacturer.setDescription("The characteristics of someone or something");
    manufacturer.setId(1L);
    manufacturer.setImageUrl("https://example.org/example");
    manufacturer.setModels(new HashSet<>());
    manufacturer.setModified(LocalDate.of(1970, 1, 1).atStartOfDay());
    manufacturer.setName("Displaying products");
    manufacturer.setUuid(UUID.randomUUID());

    Model model = new Model();
    model.setCreated(LocalDate.of(1970, 1, 1).atStartOfDay());
    model.setId(1L);
    model.setManufacturer(manufacturer);
    model.setModified(LocalDate.of(1970, 1, 1).atStartOfDay());
    model.setName("Displaying products");
    model.setProducts(new HashSet<>());
    model.setUuid(UUID.randomUUID());

    Product product = new Product();
    product.setAvailableQuantity(1);
    product.setCategories(new HashSet<>());
    product.setCreated(LocalDate.of(1970, 1, 1).atStartOfDay());
    product.setDescription("The characteristics of someone or something");
    product.setDiscountPercentage(new BigDecimal("2.3"));
    product.setId(1L);
    product.setImageUrl("https://example.org/example");
    product.setInitialQuantity(1);
    product.setModel(model);
    product.setModified(LocalDate.of(1970, 1, 1).atStartOfDay());
    product.setPrice(new BigDecimal("2.3"));
    product.setSpecification("Displaying products");
    product.setUuid(UUID.randomUUID());

    ArrayList<Product> content = new ArrayList<>();
    content.add(product);
    PageImpl<Product> pageImpl = new PageImpl<>(content);
    when(productRepository.findAll(Mockito.<Pageable>any())).thenReturn(pageImpl);
    Page<ProductDTO> actualAllProducts = productServiceImpl.getAllProducts(null);
    verify(productDTO).getDiscountPrice();
    verify(productDTO).setCategories(Mockito.<Set<String>>any());
    verify(productDTO).setDiscountPrice(Mockito.<BigDecimal>any());
    verify(productDTO).setImageUrl(Mockito.<String>any());
    verify(productDTO).setManufacturer(Mockito.<String>any());
    verify(productDTO).setModel(Mockito.<String>any());
    verify(productDTO).setPrice(Mockito.<BigDecimal>any());
    verify(productDTO).setUuid(Mockito.<UUID>any());
    verify(modelMapper).map(Mockito.<Object>any(), Mockito.<Class<ProductDTO>>any());
    verify(productRepository).findAll(Mockito.<Pageable>any());
    assertEquals(1, actualAllProducts.toList().size());
    }

    /**
    * Method under test: {@link ProductServiceImpl#getAllProducts(Pageable)}
    */
    @Test
    void testGetAllProducts6() {
    ProductDTO productDTO = mock(ProductDTO.class);
    when(productDTO.getPrice()).thenReturn(new BigDecimal("2.3"));
    when(productDTO.getDiscountPrice()).thenReturn(new BigDecimal("2.3"));
    doNothing().when(productDTO).setCategories(Mockito.<Set<String>>any());
    doNothing().when(productDTO).setDiscountPrice(Mockito.<BigDecimal>any());
    doNothing().when(productDTO).setImageUrl(Mockito.<String>any());
    doNothing().when(productDTO).setManufacturer(Mockito.<String>any());
    doNothing().when(productDTO).setModel(Mockito.<String>any());
    doNothing().when(productDTO).setPrice(Mockito.<BigDecimal>any());
    doNothing().when(productDTO).setUuid(Mockito.<UUID>any());
    productDTO.setCategories(new HashSet<>());
    productDTO.setDiscountPrice(new BigDecimal("2.3"));
    productDTO.setImageUrl("https://example.org/example");
    productDTO.setManufacturer("Manufacturer");
    productDTO.setModel("Model");
    productDTO.setPrice(new BigDecimal("2.3"));
    productDTO.setUuid(UUID.randomUUID());
    when(modelMapper.map(Mockito.<Object>any(), Mockito.<Class<ProductDTO>>any())).thenReturn(productDTO);

    Manufacturer manufacturer = new Manufacturer();
    manufacturer.setCreated(LocalDate.of(1970, 1, 1).atStartOfDay());
    manufacturer.setDescription("The characteristics of someone or something");
    manufacturer.setId(1L);
    manufacturer.setImageUrl("https://example.org/example");
    manufacturer.setModels(new HashSet<>());
    manufacturer.setModified(LocalDate.of(1970, 1, 1).atStartOfDay());
    manufacturer.setName("Displaying products");
    manufacturer.setUuid(UUID.randomUUID());

    Model model = new Model();
    model.setCreated(LocalDate.of(1970, 1, 1).atStartOfDay());
    model.setId(1L);
    model.setManufacturer(manufacturer);
    model.setModified(LocalDate.of(1970, 1, 1).atStartOfDay());
    model.setName("Displaying products");
    model.setProducts(new HashSet<>());
    model.setUuid(UUID.randomUUID());

    Product product = new Product();
    product.setAvailableQuantity(1);
    product.setCategories(new HashSet<>());
    product.setCreated(LocalDate.of(1970, 1, 1).atStartOfDay());
    product.setDescription("The characteristics of someone or something");
    product.setDiscountPercentage(new BigDecimal("2.3"));
    product.setId(1L);
    product.setImageUrl("https://example.org/example");
    product.setInitialQuantity(1);
    product.setModel(model);
    product.setModified(LocalDate.of(1970, 1, 1).atStartOfDay());
    product.setPrice(new BigDecimal("2.3"));
    product.setSpecification("Displaying products");
    product.setUuid(UUID.randomUUID());

    Manufacturer manufacturer2 = new Manufacturer();
    manufacturer2.setCreated(LocalDate.of(1970, 1, 1).atStartOfDay());
    manufacturer2.setDescription("Description");
    manufacturer2.setId(2L);
    manufacturer2.setImageUrl("Image Url");
    manufacturer2.setModels(new HashSet<>());
    manufacturer2.setModified(LocalDate.of(1970, 1, 1).atStartOfDay());
    manufacturer2.setName("42");
    manufacturer2.setUuid(UUID.randomUUID());

    Model model2 = new Model();
    model2.setCreated(LocalDate.of(1970, 1, 1).atStartOfDay());
    model2.setId(2L);
    model2.setManufacturer(manufacturer2);
    model2.setModified(LocalDate.of(1970, 1, 1).atStartOfDay());
    model2.setName("42");
    model2.setProducts(new HashSet<>());
    model2.setUuid(UUID.randomUUID());

    Product product2 = new Product();
    product2.setAvailableQuantity(-1);
    product2.setCategories(new HashSet<>());
    product2.setCreated(LocalDate.of(1970, 1, 1).atStartOfDay());
    product2.setDescription("Description");
    product2.setDiscountPercentage(new BigDecimal("2.3"));
    product2.setId(2L);
    product2.setImageUrl("Image Url");
    product2.setInitialQuantity(-1);
    product2.setModel(model2);
    product2.setModified(LocalDate.of(1970, 1, 1).atStartOfDay());
    product2.setPrice(new BigDecimal("2.3"));
    product2.setSpecification("42");
    product2.setUuid(UUID.randomUUID());

    ArrayList<Product> content = new ArrayList<>();
    content.add(product2);
    content.add(product);
    PageImpl<Product> pageImpl = new PageImpl<>(content);
    when(productRepository.findAll(Mockito.<Pageable>any())).thenReturn(pageImpl);
    Page<ProductDTO> actualAllProducts = productServiceImpl.getAllProducts(null);
    verify(productDTO, atLeast(1)).getDiscountPrice();
    verify(productDTO, atLeast(1)).getPrice();
    verify(productDTO).setCategories(Mockito.<Set<String>>any());
    verify(productDTO, atLeast(1)).setDiscountPrice(Mockito.<BigDecimal>any());
    verify(productDTO).setImageUrl(Mockito.<String>any());
    verify(productDTO).setManufacturer(Mockito.<String>any());
    verify(productDTO).setModel(Mockito.<String>any());
    verify(productDTO).setPrice(Mockito.<BigDecimal>any());
    verify(productDTO).setUuid(Mockito.<UUID>any());
    verify(modelMapper, atLeast(1)).map(Mockito.<Object>any(), Mockito.<Class<ProductDTO>>any());
    verify(productRepository).findAll(Mockito.<Pageable>any());
    assertEquals(2, actualAllProducts.toList().size());
    }

    /**
    * Method under test:  {@link ProductServiceImpl#getAllProductsWithDiscount()}
    */
    @Test
    void testGetAllProductsWithDiscount() {
    when(productRepository.findAll()).thenReturn(new ArrayList<>());
    List<ProductDTO> actualAllProductsWithDiscount = productServiceImpl.getAllProductsWithDiscount();
    verify(productRepository).findAll();
    assertTrue(actualAllProductsWithDiscount.isEmpty());
    }

    /**
    * Method under test: {@link ProductServiceImpl#getAllProductsWithDiscount()}
    */
    @Test
    void testGetAllProductsWithDiscount2() {
    ProductDTO productDTO = new ProductDTO();
    productDTO.setCategories(new HashSet<>());
    productDTO.setDiscountPrice(new BigDecimal("2.3"));
    productDTO.setImageUrl("https://example.org/example");
    productDTO.setManufacturer("Manufacturer");
    productDTO.setModel("Model");
    productDTO.setPrice(new BigDecimal("2.3"));
    productDTO.setUuid(UUID.randomUUID());
    when(modelMapper.map(Mockito.<Object>any(), Mockito.<Class<ProductDTO>>any())).thenReturn(productDTO);

    Manufacturer manufacturer = new Manufacturer();
    manufacturer.setCreated(LocalDate.of(1970, 1, 1).atStartOfDay());
    manufacturer.setDescription("The characteristics of someone or something");
    manufacturer.setId(1L);
    manufacturer.setImageUrl("https://example.org/example");
    manufacturer.setModels(new HashSet<>());
    manufacturer.setModified(LocalDate.of(1970, 1, 1).atStartOfDay());
    manufacturer.setName("Name");
    manufacturer.setUuid(UUID.randomUUID());

    Model model = new Model();
    model.setCreated(LocalDate.of(1970, 1, 1).atStartOfDay());
    model.setId(1L);
    model.setManufacturer(manufacturer);
    model.setModified(LocalDate.of(1970, 1, 1).atStartOfDay());
    model.setName("Name");
    model.setProducts(new HashSet<>());
    model.setUuid(UUID.randomUUID());

    Product product = new Product();
    product.setAvailableQuantity(1);
    product.setCategories(new HashSet<>());
    product.setCreated(LocalDate.of(1970, 1, 1).atStartOfDay());
    product.setDescription("The characteristics of someone or something");
    product.setDiscountPercentage(new BigDecimal("2.3"));
    product.setId(1L);
    product.setImageUrl("https://example.org/example");
    product.setInitialQuantity(1);
    product.setModel(model);
    product.setModified(LocalDate.of(1970, 1, 1).atStartOfDay());
    product.setPrice(new BigDecimal("2.3"));
    product.setSpecification("Specification");
    product.setUuid(UUID.randomUUID());

    ArrayList<Product> productList = new ArrayList<>();
    productList.add(product);
    when(productRepository.findAll()).thenReturn(productList);
    List<ProductDTO> actualAllProductsWithDiscount = productServiceImpl.getAllProductsWithDiscount();
    verify(modelMapper).map(Mockito.<Object>any(), Mockito.<Class<ProductDTO>>any());
    verify(productRepository).findAll();
    assertEquals(1, actualAllProductsWithDiscount.size());
    }

    /**
    * Method under test: {@link ProductServiceImpl#getAllProductsWithDiscount()}
    */
    @Test
    void testGetAllProductsWithDiscount3() {
    ProductDTO productDTO = mock(ProductDTO.class);
    when(productDTO.getPrice()).thenThrow(new EntityNotFoundException("An error occurred"));
    when(productDTO.getDiscountPrice()).thenReturn(new BigDecimal("2.3"));
    doNothing().when(productDTO).setCategories(Mockito.<Set<String>>any());
    doNothing().when(productDTO).setDiscountPrice(Mockito.<BigDecimal>any());
    doNothing().when(productDTO).setImageUrl(Mockito.<String>any());
    doNothing().when(productDTO).setManufacturer(Mockito.<String>any());
    doNothing().when(productDTO).setModel(Mockito.<String>any());
    doNothing().when(productDTO).setPrice(Mockito.<BigDecimal>any());
    doNothing().when(productDTO).setUuid(Mockito.<UUID>any());
    productDTO.setCategories(new HashSet<>());
    productDTO.setDiscountPrice(new BigDecimal("2.3"));
    productDTO.setImageUrl("https://example.org/example");
    productDTO.setManufacturer("Manufacturer");
    productDTO.setModel("Model");
    productDTO.setPrice(new BigDecimal("2.3"));
    productDTO.setUuid(UUID.randomUUID());
    when(modelMapper.map(Mockito.<Object>any(), Mockito.<Class<ProductDTO>>any())).thenReturn(productDTO);

    Manufacturer manufacturer = new Manufacturer();
    manufacturer.setCreated(LocalDate.of(1970, 1, 1).atStartOfDay());
    manufacturer.setDescription("The characteristics of someone or something");
    manufacturer.setId(1L);
    manufacturer.setImageUrl("https://example.org/example");
    manufacturer.setModels(new HashSet<>());
    manufacturer.setModified(LocalDate.of(1970, 1, 1).atStartOfDay());
    manufacturer.setName("Name");
    manufacturer.setUuid(UUID.randomUUID());

    Model model = new Model();
    model.setCreated(LocalDate.of(1970, 1, 1).atStartOfDay());
    model.setId(1L);
    model.setManufacturer(manufacturer);
    model.setModified(LocalDate.of(1970, 1, 1).atStartOfDay());
    model.setName("Name");
    model.setProducts(new HashSet<>());
    model.setUuid(UUID.randomUUID());

    Product product = new Product();
    product.setAvailableQuantity(1);
    product.setCategories(new HashSet<>());
    product.setCreated(LocalDate.of(1970, 1, 1).atStartOfDay());
    product.setDescription("The characteristics of someone or something");
    product.setDiscountPercentage(new BigDecimal("2.3"));
    product.setId(1L);
    product.setImageUrl("https://example.org/example");
    product.setInitialQuantity(1);
    product.setModel(model);
    product.setModified(LocalDate.of(1970, 1, 1).atStartOfDay());
    product.setPrice(new BigDecimal("2.3"));
    product.setSpecification("Specification");
    product.setUuid(UUID.randomUUID());

    ArrayList<Product> productList = new ArrayList<>();
    productList.add(product);
    when(productRepository.findAll()).thenReturn(productList);
    assertThrows(EntityNotFoundException.class, () -> productServiceImpl.getAllProductsWithDiscount());
    verify(productDTO, atLeast(1)).getDiscountPrice();
    verify(productDTO).getPrice();
    verify(productDTO).setCategories(Mockito.<Set<String>>any());
    verify(productDTO).setDiscountPrice(Mockito.<BigDecimal>any());
    verify(productDTO).setImageUrl(Mockito.<String>any());
    verify(productDTO).setManufacturer(Mockito.<String>any());
    verify(productDTO).setModel(Mockito.<String>any());
    verify(productDTO).setPrice(Mockito.<BigDecimal>any());
    verify(productDTO).setUuid(Mockito.<UUID>any());
    verify(modelMapper).map(Mockito.<Object>any(), Mockito.<Class<ProductDTO>>any());
    verify(productRepository).findAll();
    }

    /**
    * Method under test: {@link ProductServiceImpl#getAllProductsWithDiscount()}
    */
    @Test
    void testGetAllProductsWithDiscount4() {
    ProductDTO productDTO = mock(ProductDTO.class);
    when(productDTO.getDiscountPrice()).thenReturn(new BigDecimal("-2.3"));
    doNothing().when(productDTO).setCategories(Mockito.<Set<String>>any());
    doNothing().when(productDTO).setDiscountPrice(Mockito.<BigDecimal>any());
    doNothing().when(productDTO).setImageUrl(Mockito.<String>any());
    doNothing().when(productDTO).setManufacturer(Mockito.<String>any());
    doNothing().when(productDTO).setModel(Mockito.<String>any());
    doNothing().when(productDTO).setPrice(Mockito.<BigDecimal>any());
    doNothing().when(productDTO).setUuid(Mockito.<UUID>any());
    productDTO.setCategories(new HashSet<>());
    productDTO.setDiscountPrice(new BigDecimal("2.3"));
    productDTO.setImageUrl("https://example.org/example");
    productDTO.setManufacturer("Manufacturer");
    productDTO.setModel("Model");
    productDTO.setPrice(new BigDecimal("2.3"));
    productDTO.setUuid(UUID.randomUUID());
    when(modelMapper.map(Mockito.<Object>any(), Mockito.<Class<ProductDTO>>any())).thenReturn(productDTO);

    Manufacturer manufacturer = new Manufacturer();
    manufacturer.setCreated(LocalDate.of(1970, 1, 1).atStartOfDay());
    manufacturer.setDescription("The characteristics of someone or something");
    manufacturer.setId(1L);
    manufacturer.setImageUrl("https://example.org/example");
    manufacturer.setModels(new HashSet<>());
    manufacturer.setModified(LocalDate.of(1970, 1, 1).atStartOfDay());
    manufacturer.setName("Name");
    manufacturer.setUuid(UUID.randomUUID());

    Model model = new Model();
    model.setCreated(LocalDate.of(1970, 1, 1).atStartOfDay());
    model.setId(1L);
    model.setManufacturer(manufacturer);
    model.setModified(LocalDate.of(1970, 1, 1).atStartOfDay());
    model.setName("Name");
    model.setProducts(new HashSet<>());
    model.setUuid(UUID.randomUUID());

    Product product = new Product();
    product.setAvailableQuantity(1);
    product.setCategories(new HashSet<>());
    product.setCreated(LocalDate.of(1970, 1, 1).atStartOfDay());
    product.setDescription("The characteristics of someone or something");
    product.setDiscountPercentage(new BigDecimal("2.3"));
    product.setId(1L);
    product.setImageUrl("https://example.org/example");
    product.setInitialQuantity(1);
    product.setModel(model);
    product.setModified(LocalDate.of(1970, 1, 1).atStartOfDay());
    product.setPrice(new BigDecimal("2.3"));
    product.setSpecification("Specification");
    product.setUuid(UUID.randomUUID());

    ArrayList<Product> productList = new ArrayList<>();
    productList.add(product);
    when(productRepository.findAll()).thenReturn(productList);
    List<ProductDTO> actualAllProductsWithDiscount = productServiceImpl.getAllProductsWithDiscount();
    verify(productDTO, atLeast(1)).getDiscountPrice();
    verify(productDTO).setCategories(Mockito.<Set<String>>any());
    verify(productDTO).setDiscountPrice(Mockito.<BigDecimal>any());
    verify(productDTO).setImageUrl(Mockito.<String>any());
    verify(productDTO).setManufacturer(Mockito.<String>any());
    verify(productDTO).setModel(Mockito.<String>any());
    verify(productDTO).setPrice(Mockito.<BigDecimal>any());
    verify(productDTO).setUuid(Mockito.<UUID>any());
    verify(modelMapper).map(Mockito.<Object>any(), Mockito.<Class<ProductDTO>>any());
    verify(productRepository).findAll();
    assertTrue(actualAllProductsWithDiscount.isEmpty());
    }

    /**
    * Method under test: {@link ProductServiceImpl#getAllProductsWithDiscount()}
    */
    @Test
    void testGetAllProductsWithDiscount5() {
    ProductDTO productDTO = mock(ProductDTO.class);
    when(productDTO.getDiscountPrice()).thenReturn(null);
    doNothing().when(productDTO).setCategories(Mockito.<Set<String>>any());
    doNothing().when(productDTO).setDiscountPrice(Mockito.<BigDecimal>any());
    doNothing().when(productDTO).setImageUrl(Mockito.<String>any());
    doNothing().when(productDTO).setManufacturer(Mockito.<String>any());
    doNothing().when(productDTO).setModel(Mockito.<String>any());
    doNothing().when(productDTO).setPrice(Mockito.<BigDecimal>any());
    doNothing().when(productDTO).setUuid(Mockito.<UUID>any());
    productDTO.setCategories(new HashSet<>());
    productDTO.setDiscountPrice(new BigDecimal("2.3"));
    productDTO.setImageUrl("https://example.org/example");
    productDTO.setManufacturer("Manufacturer");
    productDTO.setModel("Model");
    productDTO.setPrice(new BigDecimal("2.3"));
    productDTO.setUuid(UUID.randomUUID());
    when(modelMapper.map(Mockito.<Object>any(), Mockito.<Class<ProductDTO>>any())).thenReturn(productDTO);

    Manufacturer manufacturer = new Manufacturer();
    manufacturer.setCreated(LocalDate.of(1970, 1, 1).atStartOfDay());
    manufacturer.setDescription("The characteristics of someone or something");
    manufacturer.setId(1L);
    manufacturer.setImageUrl("https://example.org/example");
    manufacturer.setModels(new HashSet<>());
    manufacturer.setModified(LocalDate.of(1970, 1, 1).atStartOfDay());
    manufacturer.setName("Name");
    manufacturer.setUuid(UUID.randomUUID());

    Model model = new Model();
    model.setCreated(LocalDate.of(1970, 1, 1).atStartOfDay());
    model.setId(1L);
    model.setManufacturer(manufacturer);
    model.setModified(LocalDate.of(1970, 1, 1).atStartOfDay());
    model.setName("Name");
    model.setProducts(new HashSet<>());
    model.setUuid(UUID.randomUUID());

    Product product = new Product();
    product.setAvailableQuantity(1);
    product.setCategories(new HashSet<>());
    product.setCreated(LocalDate.of(1970, 1, 1).atStartOfDay());
    product.setDescription("The characteristics of someone or something");
    product.setDiscountPercentage(new BigDecimal("2.3"));
    product.setId(1L);
    product.setImageUrl("https://example.org/example");
    product.setInitialQuantity(1);
    product.setModel(model);
    product.setModified(LocalDate.of(1970, 1, 1).atStartOfDay());
    product.setPrice(new BigDecimal("2.3"));
    product.setSpecification("Specification");
    product.setUuid(UUID.randomUUID());

    ArrayList<Product> productList = new ArrayList<>();
    productList.add(product);
    when(productRepository.findAll()).thenReturn(productList);
    List<ProductDTO> actualAllProductsWithDiscount = productServiceImpl.getAllProductsWithDiscount();
    verify(productDTO, atLeast(1)).getDiscountPrice();
    verify(productDTO).setCategories(Mockito.<Set<String>>any());
    verify(productDTO).setDiscountPrice(Mockito.<BigDecimal>any());
    verify(productDTO).setImageUrl(Mockito.<String>any());
    verify(productDTO).setManufacturer(Mockito.<String>any());
    verify(productDTO).setModel(Mockito.<String>any());
    verify(productDTO).setPrice(Mockito.<BigDecimal>any());
    verify(productDTO).setUuid(Mockito.<UUID>any());
    verify(modelMapper).map(Mockito.<Object>any(), Mockito.<Class<ProductDTO>>any());
    verify(productRepository).findAll();
    assertTrue(actualAllProductsWithDiscount.isEmpty());
    }

    /**
    * Method under test: {@link ProductServiceImpl#getAllProductsWithDiscount()}
    */
    @Test
    void testGetAllProductsWithDiscount6() {
    ProductDTO productDTO = mock(ProductDTO.class);
    when(productDTO.getPrice()).thenReturn(new BigDecimal("2.3"));
    when(productDTO.getDiscountPrice()).thenReturn(new BigDecimal("2.3"));
    doNothing().when(productDTO).setCategories(Mockito.<Set<String>>any());
    doNothing().when(productDTO).setDiscountPrice(Mockito.<BigDecimal>any());
    doNothing().when(productDTO).setImageUrl(Mockito.<String>any());
    doNothing().when(productDTO).setManufacturer(Mockito.<String>any());
    doNothing().when(productDTO).setModel(Mockito.<String>any());
    doNothing().when(productDTO).setPrice(Mockito.<BigDecimal>any());
    doNothing().when(productDTO).setUuid(Mockito.<UUID>any());
    productDTO.setCategories(new HashSet<>());
    productDTO.setDiscountPrice(new BigDecimal("2.3"));
    productDTO.setImageUrl("https://example.org/example");
    productDTO.setManufacturer("Manufacturer");
    productDTO.setModel("Model");
    productDTO.setPrice(new BigDecimal("2.3"));
    productDTO.setUuid(UUID.randomUUID());
    when(modelMapper.map(Mockito.<Object>any(), Mockito.<Class<ProductDTO>>any())).thenReturn(productDTO);

    Manufacturer manufacturer = new Manufacturer();
    manufacturer.setCreated(LocalDate.of(1970, 1, 1).atStartOfDay());
    manufacturer.setDescription("The characteristics of someone or something");
    manufacturer.setId(1L);
    manufacturer.setImageUrl("https://example.org/example");
    manufacturer.setModels(new HashSet<>());
    manufacturer.setModified(LocalDate.of(1970, 1, 1).atStartOfDay());
    manufacturer.setName("Name");
    manufacturer.setUuid(UUID.randomUUID());

    Model model = new Model();
    model.setCreated(LocalDate.of(1970, 1, 1).atStartOfDay());
    model.setId(1L);
    model.setManufacturer(manufacturer);
    model.setModified(LocalDate.of(1970, 1, 1).atStartOfDay());
    model.setName("Name");
    model.setProducts(new HashSet<>());
    model.setUuid(UUID.randomUUID());

    Product product = new Product();
    product.setAvailableQuantity(1);
    product.setCategories(new HashSet<>());
    product.setCreated(LocalDate.of(1970, 1, 1).atStartOfDay());
    product.setDescription("The characteristics of someone or something");
    product.setDiscountPercentage(new BigDecimal("2.3"));
    product.setId(1L);
    product.setImageUrl("https://example.org/example");
    product.setInitialQuantity(1);
    product.setModel(model);
    product.setModified(LocalDate.of(1970, 1, 1).atStartOfDay());
    product.setPrice(new BigDecimal("2.3"));
    product.setSpecification("Specification");
    product.setUuid(UUID.randomUUID());

    Manufacturer manufacturer2 = new Manufacturer();
    manufacturer2.setCreated(LocalDate.of(1970, 1, 1).atStartOfDay());
    manufacturer2.setDescription("Description");
    manufacturer2.setId(2L);
    manufacturer2.setImageUrl("Image Url");
    manufacturer2.setModels(new HashSet<>());
    manufacturer2.setModified(LocalDate.of(1970, 1, 1).atStartOfDay());
    manufacturer2.setName("42");
    manufacturer2.setUuid(UUID.randomUUID());

    Model model2 = new Model();
    model2.setCreated(LocalDate.of(1970, 1, 1).atStartOfDay());
    model2.setId(2L);
    model2.setManufacturer(manufacturer2);
    model2.setModified(LocalDate.of(1970, 1, 1).atStartOfDay());
    model2.setName("42");
    model2.setProducts(new HashSet<>());
    model2.setUuid(UUID.randomUUID());

    Product product2 = new Product();
    product2.setAvailableQuantity(-1);
    product2.setCategories(new HashSet<>());
    product2.setCreated(LocalDate.of(1970, 1, 1).atStartOfDay());
    product2.setDescription("Description");
    product2.setDiscountPercentage(new BigDecimal("2.3"));
    product2.setId(2L);
    product2.setImageUrl("Image Url");
    product2.setInitialQuantity(-1);
    product2.setModel(model2);
    product2.setModified(LocalDate.of(1970, 1, 1).atStartOfDay());
    product2.setPrice(new BigDecimal("2.3"));
    product2.setSpecification("42");
    product2.setUuid(UUID.randomUUID());

    ArrayList<Product> productList = new ArrayList<>();
    productList.add(product2);
    productList.add(product);
    when(productRepository.findAll()).thenReturn(productList);
    List<ProductDTO> actualAllProductsWithDiscount = productServiceImpl.getAllProductsWithDiscount();
    verify(productDTO, atLeast(1)).getDiscountPrice();
    verify(productDTO, atLeast(1)).getPrice();
    verify(productDTO).setCategories(Mockito.<Set<String>>any());
    verify(productDTO, atLeast(1)).setDiscountPrice(Mockito.<BigDecimal>any());
    verify(productDTO).setImageUrl(Mockito.<String>any());
    verify(productDTO).setManufacturer(Mockito.<String>any());
    verify(productDTO).setModel(Mockito.<String>any());
    verify(productDTO).setPrice(Mockito.<BigDecimal>any());
    verify(productDTO).setUuid(Mockito.<UUID>any());
    verify(modelMapper, atLeast(1)).map(Mockito.<Object>any(), Mockito.<Class<ProductDTO>>any());
    verify(productRepository).findAll();
    assertEquals(2, actualAllProductsWithDiscount.size());
    }

    /**
    * Method under test: {@link ProductServiceImpl#getProductByUuid(UUID)}
    */
    @Test
    void testGetProductByUuid() {
        ProductDTO productDTO = new ProductDTO();
        productDTO.setCategories(new HashSet<>());
        productDTO.setDiscountPrice(new BigDecimal("2.3"));
        productDTO.setImageUrl("https://example.org/example");
        productDTO.setManufacturer("Manufacturer");
        productDTO.setModel("Model");
        productDTO.setPrice(new BigDecimal("2.3"));
        productDTO.setUuid(UUID.randomUUID());
        when(modelMapper.map(Mockito.<Object>any(), Mockito.<Class<ProductDTO>>any())).thenReturn(productDTO);

        Manufacturer manufacturer = new Manufacturer();
        manufacturer.setCreated(LocalDate.of(1970, 1, 1).atStartOfDay());
        manufacturer.setDescription("The characteristics of someone or something");
        manufacturer.setId(1L);
        manufacturer.setImageUrl("https://example.org/example");
        manufacturer.setModels(new HashSet<>());
        manufacturer.setModified(LocalDate.of(1970, 1, 1).atStartOfDay());
        manufacturer.setName("Name");
        manufacturer.setUuid(UUID.randomUUID());

        Model model = new Model();
        model.setCreated(LocalDate.of(1970, 1, 1).atStartOfDay());
        model.setId(1L);
        model.setManufacturer(manufacturer);
        model.setModified(LocalDate.of(1970, 1, 1).atStartOfDay());
        model.setName("Name");
        model.setProducts(new HashSet<>());
        model.setUuid(UUID.randomUUID());

        Product product = new Product();
        product.setAvailableQuantity(1);
        product.setCategories(new HashSet<>());
        product.setCreated(LocalDate.of(1970, 1, 1).atStartOfDay());
        product.setDescription("The characteristics of someone or something");
        product.setDiscountPercentage(new BigDecimal("2.3"));
        product.setId(1L);
        product.setImageUrl("https://example.org/example");
        product.setInitialQuantity(1);
        product.setModel(model);
        product.setModified(LocalDate.of(1970, 1, 1).atStartOfDay());
        product.setPrice(new BigDecimal("2.3"));
        product.setSpecification("Specification");
        product.setUuid(UUID.randomUUID());
        Optional<Product> ofResult = Optional.of(product);
        when(productRepository.findByUuid(Mockito.<UUID>any())).thenReturn(ofResult);
        ProductDTO actualProductByUuid = productServiceImpl.getProductByUuid(UUID.randomUUID());
        verify(productRepository).findByUuid(Mockito.<UUID>any());
        verify(modelMapper).map(Mockito.<Object>any(), Mockito.<Class<ProductDTO>>any());
        assertSame(productDTO, actualProductByUuid);
    }

    /**
    * Method under test:  {@link ProductServiceImpl#getProductByUuid(UUID)}
    */
    @Test
    void testGetProductByUuid2() {
        when(modelMapper.map(Mockito.<Object>any(), Mockito.<Class<ProductDTO>>any()))
        .thenThrow(new EntityNotFoundException("An error occurred"));

        Manufacturer manufacturer = new Manufacturer();
        manufacturer.setCreated(LocalDate.of(1970, 1, 1).atStartOfDay());
        manufacturer.setDescription("The characteristics of someone or something");
        manufacturer.setId(1L);
        manufacturer.setImageUrl("https://example.org/example");
        manufacturer.setModels(new HashSet<>());
        manufacturer.setModified(LocalDate.of(1970, 1, 1).atStartOfDay());
        manufacturer.setName("Name");
        manufacturer.setUuid(UUID.randomUUID());

        Model model = new Model();
        model.setCreated(LocalDate.of(1970, 1, 1).atStartOfDay());
        model.setId(1L);
        model.setManufacturer(manufacturer);
        model.setModified(LocalDate.of(1970, 1, 1).atStartOfDay());
        model.setName("Name");
        model.setProducts(new HashSet<>());
        model.setUuid(UUID.randomUUID());

        Product product = new Product();
        product.setAvailableQuantity(1);
        product.setCategories(new HashSet<>());
        product.setCreated(LocalDate.of(1970, 1, 1).atStartOfDay());
        product.setDescription("The characteristics of someone or something");
        product.setDiscountPercentage(new BigDecimal("2.3"));
        product.setId(1L);
        product.setImageUrl("https://example.org/example");
        product.setInitialQuantity(1);
        product.setModel(model);
        product.setModified(LocalDate.of(1970, 1, 1).atStartOfDay());
        product.setPrice(new BigDecimal("2.3"));
        product.setSpecification("Specification");
        product.setUuid(UUID.randomUUID());
        Optional<Product> ofResult = Optional.of(product);
        when(productRepository.findByUuid(Mockito.<UUID>any())).thenReturn(ofResult);
        assertThrows(EntityNotFoundException.class, () -> productServiceImpl.getProductByUuid(UUID.randomUUID()));
        verify(productRepository).findByUuid(Mockito.<UUID>any());
        verify(modelMapper).map(Mockito.<Object>any(), Mockito.<Class<ProductDTO>>any());
    }

    /**
    * Method under test: {@link ProductServiceImpl#getProductDetailsByUuid(UUID)}
    */
    @Test
    void testGetProductDetailsByUuid() {
        ProductDetailsDTO productDetailsDTO = new ProductDetailsDTO();
        productDetailsDTO.setCategories(new HashSet<>());
        productDetailsDTO.setDescription("The characteristics of someone or something");
        productDetailsDTO.setDiscountPrice(new BigDecimal("2.3"));
        productDetailsDTO.setImageUrl("https://example.org/example");
        productDetailsDTO.setManufacturer("Manufacturer");
        productDetailsDTO.setModel("Model");
        productDetailsDTO.setName("Name");
        productDetailsDTO.setPrice(new BigDecimal("2.3"));
        productDetailsDTO.setSpecification("Specification");
        productDetailsDTO.setUuid(UUID.randomUUID());
        when(modelMapper.map(Mockito.<Object>any(), Mockito.<Class<ProductDetailsDTO>>any())).thenReturn(productDetailsDTO);

        Manufacturer manufacturer = new Manufacturer();
        manufacturer.setCreated(LocalDate.of(1970, 1, 1).atStartOfDay());
        manufacturer.setDescription("The characteristics of someone or something");
        manufacturer.setId(1L);
        manufacturer.setImageUrl("https://example.org/example");
        manufacturer.setModels(new HashSet<>());
        manufacturer.setModified(LocalDate.of(1970, 1, 1).atStartOfDay());
        manufacturer.setName("Name");
        manufacturer.setUuid(UUID.randomUUID());

        Model model = new Model();
        model.setCreated(LocalDate.of(1970, 1, 1).atStartOfDay());
        model.setId(1L);
        model.setManufacturer(manufacturer);
        model.setModified(LocalDate.of(1970, 1, 1).atStartOfDay());
        model.setName("Name");
        model.setProducts(new HashSet<>());
        model.setUuid(UUID.randomUUID());

        Product product = new Product();
        product.setAvailableQuantity(1);
        product.setCategories(new HashSet<>());
        product.setCreated(LocalDate.of(1970, 1, 1).atStartOfDay());
        product.setDescription("The characteristics of someone or something");
        product.setDiscountPercentage(new BigDecimal("2.3"));
        product.setId(1L);
        product.setImageUrl("https://example.org/example");
        product.setInitialQuantity(1);
        product.setModel(model);
        product.setModified(LocalDate.of(1970, 1, 1).atStartOfDay());
        product.setPrice(new BigDecimal("2.3"));
        product.setSpecification("Specification");
        product.setUuid(UUID.randomUUID());
        Optional<Product> ofResult = Optional.of(product);
        when(productRepository.findByUuid(Mockito.<UUID>any())).thenReturn(ofResult);
        ProductDetailsDTO actualProductDetailsByUuid = productServiceImpl.getProductDetailsByUuid(UUID.randomUUID());
        verify(productRepository).findByUuid(Mockito.<UUID>any());
        verify(modelMapper).map(Mockito.<Object>any(), Mockito.<Class<ProductDetailsDTO>>any());
        BigDecimal expectedDiscountPrice = new BigDecimal("2.3");
        assertEquals(expectedDiscountPrice, actualProductDetailsByUuid.getDiscountPrice());
        assertSame(productDetailsDTO, actualProductDetailsByUuid);
    }

    /**
    * Method under test:  {@link ProductServiceImpl#getProductDetailsByUuid(UUID)}
    */
    @Test
    void testGetProductDetailsByUuid2() {
        when(modelMapper.map(Mockito.<Object>any(), Mockito.<Class<ProductDetailsDTO>>any()))
        .thenThrow(new EntityNotFoundException("An error occurred"));

        Manufacturer manufacturer = new Manufacturer();
        manufacturer.setCreated(LocalDate.of(1970, 1, 1).atStartOfDay());
        manufacturer.setDescription("The characteristics of someone or something");
        manufacturer.setId(1L);
        manufacturer.setImageUrl("https://example.org/example");
        manufacturer.setModels(new HashSet<>());
        manufacturer.setModified(LocalDate.of(1970, 1, 1).atStartOfDay());
        manufacturer.setName("Name");
        manufacturer.setUuid(UUID.randomUUID());

        Model model = new Model();
        model.setCreated(LocalDate.of(1970, 1, 1).atStartOfDay());
        model.setId(1L);
        model.setManufacturer(manufacturer);
        model.setModified(LocalDate.of(1970, 1, 1).atStartOfDay());
        model.setName("Name");
        model.setProducts(new HashSet<>());
        model.setUuid(UUID.randomUUID());

        Product product = new Product();
        product.setAvailableQuantity(1);
        product.setCategories(new HashSet<>());
        product.setCreated(LocalDate.of(1970, 1, 1).atStartOfDay());
        product.setDescription("The characteristics of someone or something");
        product.setDiscountPercentage(new BigDecimal("2.3"));
        product.setId(1L);
        product.setImageUrl("https://example.org/example");
        product.setInitialQuantity(1);
        product.setModel(model);
        product.setModified(LocalDate.of(1970, 1, 1).atStartOfDay());
        product.setPrice(new BigDecimal("2.3"));
        product.setSpecification("Specification");
        product.setUuid(UUID.randomUUID());
        Optional<Product> ofResult = Optional.of(product);
        when(productRepository.findByUuid(Mockito.<UUID>any())).thenReturn(ofResult);
        assertThrows(EntityNotFoundException.class, () -> productServiceImpl.getProductDetailsByUuid(UUID.randomUUID()));
        verify(productRepository).findByUuid(Mockito.<UUID>any());
        verify(modelMapper).map(Mockito.<Object>any(), Mockito.<Class<ProductDetailsDTO>>any());
    }

    /**
    * Method under test: {@link ProductServiceImpl#getProductDetailsByUuid(UUID)}
    */
    @Test
    void testGetProductDetailsByUuid3() {
        ProductDetailsDTO productDetailsDTO = mock(ProductDetailsDTO.class);
        when(productDetailsDTO.getDiscountPrice()).thenReturn(new BigDecimal("-2.3"));
        doNothing().when(productDetailsDTO).setCategories(Mockito.<Set<String>>any());
        doNothing().when(productDetailsDTO).setDiscountPrice(Mockito.<BigDecimal>any());
        doNothing().when(productDetailsDTO).setImageUrl(Mockito.<String>any());
        doNothing().when(productDetailsDTO).setManufacturer(Mockito.<String>any());
        doNothing().when(productDetailsDTO).setModel(Mockito.<String>any());
        doNothing().when(productDetailsDTO).setPrice(Mockito.<BigDecimal>any());
        doNothing().when(productDetailsDTO).setUuid(Mockito.<UUID>any());
        doNothing().when(productDetailsDTO).setDescription(Mockito.<String>any());
        doNothing().when(productDetailsDTO).setName(Mockito.<String>any());
        doNothing().when(productDetailsDTO).setSpecification(Mockito.<String>any());
        productDetailsDTO.setCategories(new HashSet<>());
        productDetailsDTO.setDescription("The characteristics of someone or something");
        productDetailsDTO.setDiscountPrice(new BigDecimal("2.3"));
        productDetailsDTO.setImageUrl("https://example.org/example");
        productDetailsDTO.setManufacturer("Manufacturer");
        productDetailsDTO.setModel("Model");
        productDetailsDTO.setName("Name");
        productDetailsDTO.setPrice(new BigDecimal("2.3"));
        productDetailsDTO.setSpecification("Specification");
        productDetailsDTO.setUuid(UUID.randomUUID());
        when(modelMapper.map(Mockito.<Object>any(), Mockito.<Class<ProductDetailsDTO>>any())).thenReturn(productDetailsDTO);

        Manufacturer manufacturer = new Manufacturer();
        manufacturer.setCreated(LocalDate.of(1970, 1, 1).atStartOfDay());
        manufacturer.setDescription("The characteristics of someone or something");
        manufacturer.setId(1L);
        manufacturer.setImageUrl("https://example.org/example");
        manufacturer.setModels(new HashSet<>());
        manufacturer.setModified(LocalDate.of(1970, 1, 1).atStartOfDay());
        manufacturer.setName("Name");
        manufacturer.setUuid(UUID.randomUUID());

        Model model = new Model();
        model.setCreated(LocalDate.of(1970, 1, 1).atStartOfDay());
        model.setId(1L);
        model.setManufacturer(manufacturer);
        model.setModified(LocalDate.of(1970, 1, 1).atStartOfDay());
        model.setName("Name");
        model.setProducts(new HashSet<>());
        model.setUuid(UUID.randomUUID());

        Product product = new Product();
        product.setAvailableQuantity(1);
        product.setCategories(new HashSet<>());
        product.setCreated(LocalDate.of(1970, 1, 1).atStartOfDay());
        product.setDescription("The characteristics of someone or something");
        product.setDiscountPercentage(new BigDecimal("2.3"));
        product.setId(1L);
        product.setImageUrl("https://example.org/example");
        product.setInitialQuantity(1);
        product.setModel(model);
        product.setModified(LocalDate.of(1970, 1, 1).atStartOfDay());
        product.setPrice(new BigDecimal("2.3"));
        product.setSpecification("Specification");
        product.setUuid(UUID.randomUUID());
        Optional<Product> ofResult = Optional.of(product);
        when(productRepository.findByUuid(Mockito.<UUID>any())).thenReturn(ofResult);
        productServiceImpl.getProductDetailsByUuid(UUID.randomUUID());
        verify(productDetailsDTO, atLeast(1)).getDiscountPrice();
        verify(productDetailsDTO).setCategories(Mockito.<Set<String>>any());
        verify(productDetailsDTO).setDiscountPrice(Mockito.<BigDecimal>any());
        verify(productDetailsDTO).setImageUrl(Mockito.<String>any());
        verify(productDetailsDTO).setManufacturer(Mockito.<String>any());
        verify(productDetailsDTO).setModel(Mockito.<String>any());
        verify(productDetailsDTO).setPrice(Mockito.<BigDecimal>any());
        verify(productDetailsDTO).setUuid(Mockito.<UUID>any());
        verify(productDetailsDTO).setDescription(Mockito.<String>any());
        verify(productDetailsDTO).setName(Mockito.<String>any());
        verify(productDetailsDTO).setSpecification(Mockito.<String>any());
        verify(productRepository).findByUuid(Mockito.<UUID>any());
        verify(modelMapper).map(Mockito.<Object>any(), Mockito.<Class<ProductDetailsDTO>>any());
    }

    /**
    * Method under test: {@link ProductServiceImpl#getProductDetailsByUuid(UUID)}
    */
    @Test
    void testGetProductDetailsByUuid4() {
        ProductDetailsDTO productDetailsDTO = mock(ProductDetailsDTO.class);
        when(productDetailsDTO.getDiscountPrice()).thenReturn(null);
        doNothing().when(productDetailsDTO).setCategories(Mockito.<Set<String>>any());
        doNothing().when(productDetailsDTO).setDiscountPrice(Mockito.<BigDecimal>any());
        doNothing().when(productDetailsDTO).setImageUrl(Mockito.<String>any());
        doNothing().when(productDetailsDTO).setManufacturer(Mockito.<String>any());
        doNothing().when(productDetailsDTO).setModel(Mockito.<String>any());
        doNothing().when(productDetailsDTO).setPrice(Mockito.<BigDecimal>any());
        doNothing().when(productDetailsDTO).setUuid(Mockito.<UUID>any());
        doNothing().when(productDetailsDTO).setDescription(Mockito.<String>any());
        doNothing().when(productDetailsDTO).setName(Mockito.<String>any());
        doNothing().when(productDetailsDTO).setSpecification(Mockito.<String>any());
        productDetailsDTO.setCategories(new HashSet<>());
        productDetailsDTO.setDescription("The characteristics of someone or something");
        productDetailsDTO.setDiscountPrice(new BigDecimal("2.3"));
        productDetailsDTO.setImageUrl("https://example.org/example");
        productDetailsDTO.setManufacturer("Manufacturer");
        productDetailsDTO.setModel("Model");
        productDetailsDTO.setName("Name");
        productDetailsDTO.setPrice(new BigDecimal("2.3"));
        productDetailsDTO.setSpecification("Specification");
        productDetailsDTO.setUuid(UUID.randomUUID());
        when(modelMapper.map(Mockito.<Object>any(), Mockito.<Class<ProductDetailsDTO>>any())).thenReturn(productDetailsDTO);

        Manufacturer manufacturer = new Manufacturer();
        manufacturer.setCreated(LocalDate.of(1970, 1, 1).atStartOfDay());
        manufacturer.setDescription("The characteristics of someone or something");
        manufacturer.setId(1L);
        manufacturer.setImageUrl("https://example.org/example");
        manufacturer.setModels(new HashSet<>());
        manufacturer.setModified(LocalDate.of(1970, 1, 1).atStartOfDay());
        manufacturer.setName("Name");
        manufacturer.setUuid(UUID.randomUUID());

        Model model = new Model();
        model.setCreated(LocalDate.of(1970, 1, 1).atStartOfDay());
        model.setId(1L);
        model.setManufacturer(manufacturer);
        model.setModified(LocalDate.of(1970, 1, 1).atStartOfDay());
        model.setName("Name");
        model.setProducts(new HashSet<>());
        model.setUuid(UUID.randomUUID());

        Product product = new Product();
        product.setAvailableQuantity(1);
        product.setCategories(new HashSet<>());
        product.setCreated(LocalDate.of(1970, 1, 1).atStartOfDay());
        product.setDescription("The characteristics of someone or something");
        product.setDiscountPercentage(new BigDecimal("2.3"));
        product.setId(1L);
        product.setImageUrl("https://example.org/example");
        product.setInitialQuantity(1);
        product.setModel(model);
        product.setModified(LocalDate.of(1970, 1, 1).atStartOfDay());
        product.setPrice(new BigDecimal("2.3"));
        product.setSpecification("Specification");
        product.setUuid(UUID.randomUUID());
        Optional<Product> ofResult = Optional.of(product);
        when(productRepository.findByUuid(Mockito.<UUID>any())).thenReturn(ofResult);
        productServiceImpl.getProductDetailsByUuid(UUID.randomUUID());
        verify(productDetailsDTO).getDiscountPrice();
        verify(productDetailsDTO).setCategories(Mockito.<Set<String>>any());
        verify(productDetailsDTO).setDiscountPrice(Mockito.<BigDecimal>any());
        verify(productDetailsDTO).setImageUrl(Mockito.<String>any());
        verify(productDetailsDTO).setManufacturer(Mockito.<String>any());
        verify(productDetailsDTO).setModel(Mockito.<String>any());
        verify(productDetailsDTO).setPrice(Mockito.<BigDecimal>any());
        verify(productDetailsDTO).setUuid(Mockito.<UUID>any());
        verify(productDetailsDTO).setDescription(Mockito.<String>any());
        verify(productDetailsDTO).setName(Mockito.<String>any());
        verify(productDetailsDTO).setSpecification(Mockito.<String>any());
        verify(productRepository).findByUuid(Mockito.<UUID>any());
        verify(modelMapper).map(Mockito.<Object>any(), Mockito.<Class<ProductDetailsDTO>>any());
    }

    /**
    * Method under test: {@link ProductServiceImpl#getProductDetailsByUuid(UUID)}
    */
    @Test
    void testGetProductDetailsByUuid5() {
        ProductDetailsDTO productDetailsDTO = mock(ProductDetailsDTO.class);
        doNothing().when(productDetailsDTO).setCategories(Mockito.<Set<String>>any());
        doNothing().when(productDetailsDTO).setDiscountPrice(Mockito.<BigDecimal>any());
        doNothing().when(productDetailsDTO).setImageUrl(Mockito.<String>any());
        doNothing().when(productDetailsDTO).setManufacturer(Mockito.<String>any());
        doNothing().when(productDetailsDTO).setModel(Mockito.<String>any());
        doNothing().when(productDetailsDTO).setPrice(Mockito.<BigDecimal>any());
        doNothing().when(productDetailsDTO).setUuid(Mockito.<UUID>any());
        doNothing().when(productDetailsDTO).setDescription(Mockito.<String>any());
        doNothing().when(productDetailsDTO).setName(Mockito.<String>any());
        doNothing().when(productDetailsDTO).setSpecification(Mockito.<String>any());
        productDetailsDTO.setCategories(new HashSet<>());
        productDetailsDTO.setDescription("The characteristics of someone or something");
        productDetailsDTO.setDiscountPrice(new BigDecimal("2.3"));
        productDetailsDTO.setImageUrl("https://example.org/example");
        productDetailsDTO.setManufacturer("Manufacturer");
        productDetailsDTO.setModel("Model");
        productDetailsDTO.setName("Name");
        productDetailsDTO.setPrice(new BigDecimal("2.3"));
        productDetailsDTO.setSpecification("Specification");
        productDetailsDTO.setUuid(UUID.randomUUID());
        when(modelMapper.map(Mockito.<Object>any(), Mockito.<Class<Object>>any())).thenReturn("Map");
        when(modelMapper.map(Mockito.<Object>any(), Mockito.<Class<ProductDetailsDTO>>any())).thenReturn(productDetailsDTO);
        Optional<Product> emptyResult = Optional.empty();
        when(productRepository.findByUuid(Mockito.<UUID>any())).thenReturn(emptyResult);
        assertThrows(EntityNotFoundException.class, () -> productServiceImpl.getProductDetailsByUuid(UUID.randomUUID()));
        verify(productDetailsDTO).setCategories(Mockito.<Set<String>>any());
        verify(productDetailsDTO).setDiscountPrice(Mockito.<BigDecimal>any());
        verify(productDetailsDTO).setImageUrl(Mockito.<String>any());
        verify(productDetailsDTO).setManufacturer(Mockito.<String>any());
        verify(productDetailsDTO).setModel(Mockito.<String>any());
        verify(productDetailsDTO).setPrice(Mockito.<BigDecimal>any());
        verify(productDetailsDTO).setUuid(Mockito.<UUID>any());
        verify(productDetailsDTO).setDescription(Mockito.<String>any());
        verify(productDetailsDTO).setName(Mockito.<String>any());
        verify(productDetailsDTO).setSpecification(Mockito.<String>any());
        verify(productRepository).findByUuid(Mockito.<UUID>any());
        verify(modelMapper).map(Mockito.<Object>any(), Mockito.<Class<Object>>any());
    }

}
