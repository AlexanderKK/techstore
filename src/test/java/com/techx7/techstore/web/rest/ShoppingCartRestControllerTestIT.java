package com.techx7.techstore.web.rest;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.techx7.techstore.model.entity.Product;
import com.techx7.techstore.service.CartService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.UUID;

@SpringBootTest
@AutoConfigureMockMvc
class ShoppingCartRestControllerTestIT {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private CartService cartService;

    @BeforeEach
    void setUp() {

    }

    @AfterEach
    void tearDown() {

    }

    @Test
    void testLoadCartItems() throws Exception {
        UUID productUuid = UUID.randomUUID();
        Integer quantity = 0;

        mockMvc.perform(
                MockMvcRequestBuilders.get("/cart/load/{productUuid}/{quantity}", productUuid, quantity)
                        .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    void addToCart() {
    }

    @Test
    void updateQuantity() {
    }

    @Test
    void removeFromCart() {
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

    private Product createProduct() {
        // TODO: Create product

        Product product = new Product();

        return product;
    }

}