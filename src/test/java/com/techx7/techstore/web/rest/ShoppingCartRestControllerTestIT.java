package com.techx7.techstore.web.rest;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.UUID;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class ShoppingCartRestControllerTestIT {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void testLoadCartItems() throws Exception {
        mockMvc.perform(
                MockMvcRequestBuilders.get("/cart/load")
                        .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(status().isOk());
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

}
