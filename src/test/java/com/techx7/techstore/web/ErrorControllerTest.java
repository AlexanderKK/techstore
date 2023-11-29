package com.techx7.techstore.web;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

@WebMvcTest(ErrorController.class)
class ErrorControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    @WithMockUser(username = "test-user")
    void testErrorNoJavascriptWhenCalled() throws Exception {
        // Act & Assert
        mockMvc.perform(get("/error/no-javascript"))
                .andExpect(status().isOk())
                .andExpect(view().name("error/no-javascript"));
    }

    @Test
    @WithMockUser(username = "test-user")
    void testForbiddenWhenCalled() throws Exception {
        // Act & Assert
        mockMvc.perform(get("/error/forbidden"))
                .andExpect(status().isOk())
                .andExpect(view().name("cart"));
    }

}
