package com.techx7.techstore.web;

import com.techx7.techstore.exception.EntityNotFoundException;
import com.techx7.techstore.model.dto.category.CategoryDTO;
import com.techx7.techstore.service.CategoryService;
import com.techx7.techstore.service.interceptors.RegistrationInterceptor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.util.Collections;
import java.util.List;
import java.util.UUID;

import static com.techx7.techstore.testUtils.TestData.createMultipartFile;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(CategoryController.class)
class CategoryControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CategoryService categoryService;

    @MockBean
    private RegistrationInterceptor registrationInterceptor;

    @Test
    @WithMockUser(username = "test-manager", roles = {"USER", "MANAGER"})
    void testManageCategoryWhenCategoriesExist() throws Exception {
        List<CategoryDTO> categoryDTOs = List.of(new CategoryDTO());

        when(categoryService.getAllCategories()).thenReturn(categoryDTOs);

        MvcResult mvcResult = mockMvc.perform(get("/categories"))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("categories", "addCategoryDTO"))
                .andReturn();

        verify(categoryService).getAllCategories();

        assertThat(mvcResult.getModelAndView().getModel().get("categories")).isEqualTo(categoryDTOs);
    }

    @Test
    @WithMockUser(username = "test-manager", roles = {"USER", "MANAGER"})
    void testManageCategoryWhenNoCategories() throws Exception {
        when(categoryService.getAllCategories()).thenReturn(Collections.emptyList());

        MvcResult mvcResult = mockMvc.perform(get("/categories"))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("categories", "addCategoryDTO"))
                .andReturn();

        verify(categoryService).getAllCategories();

        assertThat(mvcResult.getModelAndView().getModel().get("categories")).isEqualTo(Collections.emptyList());
    }

    @Test
    @WithMockUser(username = "test-manager", roles = {"USER", "MANAGER"})
    void testGetCategoryWhenUUIDIsValid() throws Exception {
        UUID uuid = UUID.randomUUID();

        CategoryDTO categoryDTO = new CategoryDTO();

        categoryDTO.setUuid(uuid);
        when(categoryService.getCategoryByUuid(uuid)).thenReturn(categoryDTO);

        mockMvc.perform(get("/categories/edit/{uuid}", uuid))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("categoryToEdit"));

        verify(categoryService).getCategoryByUuid(uuid);
    }

    @Test
    @WithMockUser(username = "test-manager", roles = {"USER", "MANAGER"})
    void testGetCategoryWhenUUIDIsNotFound() throws Exception {
        UUID uuid = UUID.randomUUID();

        when(categoryService.getCategoryByUuid(uuid)).thenThrow(EntityNotFoundException.class);

        mockMvc.perform(get("/categories/edit/{uuid}", uuid))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/categories"));

        verify(categoryService).getCategoryByUuid(uuid);
    }

    @Test
    @WithMockUser(username = "test-manager", roles = {"USER", "MANAGER"})
    void testEditCategoryWhenDTOIsValid() throws Exception {
        CategoryDTO categoryDTO = new CategoryDTO();

        categoryDTO.setUuid(UUID.randomUUID());
        categoryDTO.setImage(createMultipartFile());
        categoryDTO.setImageUrl("test.png");
        categoryDTO.setName("Electronics");
        categoryDTO.setDescription("Various electronic devices");

        mockMvc.perform(patch("/categories/edit")
                        .flashAttr("categoryDTO", categoryDTO)
                        .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/categories"));

        verify(categoryService).editCategory(categoryDTO);
    }

    @Test
    @WithMockUser(username = "test-manager", roles = {"USER", "MANAGER"})
    void testEditCategoryWhenDTOIsInvalid() throws Exception {
        CategoryDTO categoryDTO = new CategoryDTO();

        categoryDTO.setUuid(UUID.randomUUID());
        categoryDTO.setName("");

        mockMvc.perform(patch("/categories/edit")
                        .flashAttr("categoryDTO", categoryDTO)
                        .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/categories/edit/" + categoryDTO.getUuid()));

        verify(categoryService, never()).editCategory(any(CategoryDTO.class));
    }

    @Test
    @WithMockUser(username = "test-manager", roles = {"USER", "MANAGER"})
    void testDeleteCategoryWhenUUIDIsValid() throws Exception {
        UUID uuid = UUID.randomUUID();

        mockMvc.perform(delete("/categories/delete/{uuid}", uuid)
                        .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/categories"));

        verify(categoryService).deleteCategoryByUuid(uuid);
    }

    @Test
    @WithMockUser(username = "test-manager", roles = {"USER", "MANAGER"})
    void testDeleteAllCategories() throws Exception {
        mockMvc.perform(delete("/categories/delete-all")
                        .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/categories"));

        verify(categoryService).deleteAllCategories();
    }

}
