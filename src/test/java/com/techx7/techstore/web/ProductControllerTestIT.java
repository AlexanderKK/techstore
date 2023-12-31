package com.techx7.techstore.web;

import com.techx7.techstore.model.dto.product.AddProductDTO;
import com.techx7.techstore.model.entity.Category;
import com.techx7.techstore.model.entity.Model;
import com.techx7.techstore.model.entity.Product;
import com.techx7.techstore.testUtils.TestData;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import static com.techx7.techstore.testUtils.TestData.createMultipartFile;
import static com.techx7.techstore.testUtils.TestData.createValidMultipartImage;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class ProductControllerTestIT {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private TestData testData;

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
        mockMvc.perform(get("/products"))
                .andExpect(status().isOk())
                .andExpect(view().name("products/products"))
                .andExpect(model().attributeExists("products"));
    }

    @Test
    @WithMockUser(username = "test-manager", roles = {"USER", "MANAGER"})
    void testManageProductWhenCalled() throws Exception {
        testData.cleanAllTestData();

        testData.createAndSaveManufacturer();
        testData.createAndSaveCategories();

        mockMvc.perform(get("/products/add"))
                .andExpect(status().isOk())
                .andExpect(view().name("products/product-add"))
                .andExpect(model().attributeExists("categories", "manufacturers", "addProductDTO"));
    }

    @Test
    @WithMockUser(username = "test-manager", roles = {"USER", "MANAGER"})
    void testAddProductWhenCalledWithValidParameters() throws Exception {
        testData.cleanAllTestData();

        AddProductDTO addProductDTO = createAddProductDTO();
        addProductDTO.setImage(createValidMultipartImage());

        mockMvc.perform(post("/products/add")
                        .flashAttr("addProductDTO", addProductDTO)
                        .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/products"));
    }

    @Test
    void testProductDetailsWhenCalledWithValidUuid() throws Exception {
        UUID existingUuid = existingProduct.getUuid();

        mockMvc.perform(get("/products/detail/{uuid}", existingUuid))
                .andExpect(status().isOk())
                .andExpect(view().name("products/product-details"))
                .andExpect(model().attributeExists("product"));
    }

    @Test
    void testHandleModelErrorWhenCalled() throws Exception {
        UUID uuid = UUID.randomUUID();

        mockMvc.perform(get("/products/detail/{uuid}", uuid))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/products"));
    }

    private AddProductDTO createAddProductDTO() {
        AddProductDTO addProductDTO = new AddProductDTO();

        Model model = testData.createAndSaveModel();

        addProductDTO.setModel(model.getId());

        List<Category> categories = testData.createAndSaveCategories();

        String categoriesString =
                categories.stream()
                        .map(Category::getId)
                        .map(String::valueOf)
                        .collect(Collectors.joining(","));

        addProductDTO.setCategories(categoriesString);

        addProductDTO.setImage(createMultipartFile());

        addProductDTO.setPrice("1000");

        addProductDTO.setInitialQuantity("15");

        addProductDTO.setSpecification("Test Specification");

        return addProductDTO;
    }

}
