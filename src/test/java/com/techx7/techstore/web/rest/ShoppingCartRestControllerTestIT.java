package com.techx7.techstore.web.rest;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.techx7.techstore.model.entity.*;
import com.techx7.techstore.model.session.TechStoreUserDetails;
import com.techx7.techstore.repository.*;
import com.techx7.techstore.service.CartService;
import com.techx7.techstore.service.impl.TechstoreUserDetailsServiceImpl;
import com.techx7.techstore.testUtils.TestData;
import org.antlr.v4.runtime.misc.LogManager;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.math.BigDecimal;
import java.security.Principal;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import static com.techx7.techstore.testUtils.TestData.createRole;
import static com.techx7.techstore.testUtils.TestData.createUser;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class ShoppingCartRestControllerTestIT {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private CartService cartService;

    @Autowired
    private ManufacturerRepository manufacturerRepository;

    @Autowired
    private ModelRepository modelRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private ProductRepository productRepository;

    private UserDetailsService techstoreUserDetailsService;

    private UserDetails techStoreUserDetails;

    @Autowired
    private TestData testData;

    @BeforeEach
    void setUp() {
        techstoreUserDetailsService = new TechstoreUserDetailsServiceImpl(userRepository);
    }

    @AfterEach
    void tearDown() {

    }

    @Test
    void testLoadCartItems() throws Exception {
        mockMvc.perform(
                MockMvcRequestBuilders.get("/cart/load")
                        .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(status().isOk());
    }

    @Test
    void testPrincipalNotFoundWhenAddingToCart() throws Exception {
        mockMvc.perform(
                MockMvcRequestBuilders.get("/cart/add")
                        .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(status().isUnauthorized());
    }

    @Test
    void addToCartPrincipalNotFound() throws Exception {
        UUID productUuid = UUID.randomUUID();
        Integer quantity = 1;

        mockMvc.perform(
                MockMvcRequestBuilders
                        .post("/cart/add/{productUuid}/{quantity}", productUuid, quantity)
                        .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser(username = "TestUser", roles = "USER")
    void addToCart() throws Exception {
        UUID productUuid = UUID.randomUUID();
        Integer quantity = 1;

        mockMvc.perform(
                MockMvcRequestBuilders
                        .post("/cart/add/{productUuid}/{quantity}", productUuid, quantity)
                        .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(status().isOk());
    }

    @Test
    void updateQuantityPrincipalNotFound() throws Exception {
        UUID productUuid = UUID.randomUUID();
        Integer quantity = 3;

        mockMvc.perform(
                MockMvcRequestBuilders
                        .post("/cart/update/{productUuid}/{quantity}", productUuid, quantity)
                        .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser(username = "TestUser", roles = "USER")
    void updateQuantity() throws Exception {
        UUID productUuid = UUID.randomUUID();
        Integer quantity = 3;

        mockMvc.perform(
                MockMvcRequestBuilders
                        .post("/cart/update/{productUuid}/{quantity}", productUuid, quantity)
                        .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(status().isOk());
    }

    @Test
    void removeFromCartPrincipalNotFound() throws Exception {
        UUID productUuid = UUID.randomUUID();

        mockMvc.perform(
                MockMvcRequestBuilders
                        .post("/cart/remove/{productUuid}", productUuid)
                        .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser(username = "TestUser", roles = "USER")
    void removeFromCart() throws Exception {
        UUID productUuid = UUID.randomUUID();

        mockMvc.perform(
                MockMvcRequestBuilders
                        .post("/cart/remove/{productUuid}", productUuid)
                        .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(status().isOk());
    }

    @Test
    void handleProductQuantityError() {
    }

    @Test
    void handlePrincipalError() {
    }

    @Test
    void testHandlePrincipalError() {

    }

    private Product createAndSaveProduct() {
        // TODO: Create product

        Product product = new Product();

        // Set model
        Model model = createAndSaveModel();
        product.setModel(model);

        // Set categories
        Set<Category> categories = createAndSaveCategories();
        product.setCategories(categories);

        // Set other product characteristics
        product.setImageUrl("test.png");

        product.setPrice(BigDecimal.TEN);

        product.setInitialQuantity(15);

        product.setAvailableQuantity(15);

        product.setSpecification("Test Specification");

        return productRepository.save(product);
    }

    private Set<Category> createAndSaveCategories() {
        Category category1 = new Category();
        category1.setImageUrl("category1.png");
        category1.setName("Category1");

        Category category2 = new Category();
        category2.setImageUrl("category2.png");
        category2.setName("Category2");

        Set<Category> categories = Set.of(category1, category2);

        return new HashSet<>(categoryRepository.saveAll(categories));
    }

    private Model createAndSaveModel() {
        Model model = new Model();

        model.setName("TestModel");
        model.setManufacturer(createAndSaveManufacturer());

        return modelRepository.save(model);
    }

    private Manufacturer createAndSaveManufacturer() {
        Manufacturer manufacturer = new Manufacturer();

        manufacturer.setImageUrl("manufacturer.png");
        manufacturer.setName("TestManufacturer");

        return manufacturerRepository.save(manufacturer);
    }

}
