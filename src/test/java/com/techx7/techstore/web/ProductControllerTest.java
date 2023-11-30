package com.techx7.techstore.web;

import com.techx7.techstore.exception.EntityNotFoundException;
import com.techx7.techstore.model.dto.product.AddProductDTO;
import com.techx7.techstore.model.dto.product.ProductDetailsDTO;
import com.techx7.techstore.model.entity.Product;
import com.techx7.techstore.service.CategoryService;
import com.techx7.techstore.service.ManufacturerService;
import com.techx7.techstore.service.ProductService;
import com.techx7.techstore.testUtils.TestData;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.validation.BindingResult;

import java.util.UUID;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class ProductControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ProductService productService;

    @Autowired
    private ManufacturerService manufacturerService;

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private TestData testData;

    @Autowired
    private ModelMapper mapper;
    private Product existingProduct;

    @BeforeEach
    void setUp() {
        testData.cleanAllTestData();

        existingProduct = testData.createAndSaveProduct();
    }

    @AfterEach
    void tearDown() {
        testData.cleanAllTestData();
    }

    @Test
    void testGetProductsWhenCalledWithValidParameters() throws Exception {
        mockMvc.perform(get("/products")
                        .param("page", "1")
                        .param("size", "4"))
                .andExpect(status().isOk())
                .andExpect(view().name("products"))
                .andExpect(model().attributeExists("products", "productPage", "pageNumbers"));
    }

    @Test
    @WithMockUser(username = "test-admin", roles = {"USER", "MANAGER"})
    void testManageProductWhenCalled() throws Exception {
        testData.cleanAllTestData();

        testData.createAndSaveManufacturer();
        testData.createAndSaveCategories();

        mockMvc.perform(get("/products/add"))
                .andExpect(status().isOk())
                .andExpect(view().name("product-add"))
                .andExpect(model().attributeExists("categories", "manufacturers", "addProductDTO"));
    }

    @Test
    void testAddProductWhenCalledWithValidParameters() throws Exception {
        AddProductDTO addProductDTO = new AddProductDTO();
        BindingResult bindingResult = mock(BindingResult.class);
        when(bindingResult.hasErrors()).thenReturn(false);

        mockMvc.perform(post("/products/add")
                        .flashAttr("addProductDTO", addProductDTO)
                        .flashAttr("org.springframework.validation.BindingResult.addProductDTO", bindingResult))
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/products"));

        verify(productService).createProduct(any(AddProductDTO.class));
    }

    @Test
    void testProductDetailsWhenCalledWithValidUUIDThenReturnCorrectViewNameAndModelAttribute() throws Exception {
        ProductDetailsDTO productDetailsDTO = new ProductDetailsDTO();
        when(productService.getProductDetailsByUuid(any(UUID.class))).thenReturn(productDetailsDTO);

        mockMvc.perform(get("/products/detail/{uuid}", UUID.randomUUID()))
                .andExpect(status().isOk())
                .andExpect(view().name("product-details"))
                .andExpect(model().attributeExists("product"));

        verify(productService).getProductDetailsByUuid(any(UUID.class));
    }

    @Test
    void testHandleModelErrorWhenCalledThenReturnCorrectViewName() throws Exception {
        EntityNotFoundException exception = new EntityNotFoundException("Entity not found");

        mockMvc.perform(get("/products/handleModelError").flashAttr("exception", exception))
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/products/add"));
    }

}
