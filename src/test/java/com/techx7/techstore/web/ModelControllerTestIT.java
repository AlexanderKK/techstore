package com.techx7.techstore.web;

import com.techx7.techstore.model.dto.model.AddModelDTO;
import com.techx7.techstore.model.dto.model.ModelWithManufacturerDTO;
import com.techx7.techstore.model.entity.Manufacturer;
import com.techx7.techstore.model.entity.Model;
import com.techx7.techstore.repository.ModelRepository;
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

import java.util.UUID;

import static com.techx7.techstore.constant.Messages.ENTITY_NOT_FOUND;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class ModelControllerTestIT {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ModelRepository modelRepository;

    @Autowired
    private TestData testData;

    @Autowired
    private ModelMapper mapper;

    private Model existingModel;

    private UUID existingModelUuid;

    @BeforeEach
    void setUp() {
        testData.cleanAllTestData();

        existingModel = testData.createAndSaveModel();

        existingModelUuid = existingModel.getUuid();
    }

    @AfterEach
    void tearDown() {
        testData.cleanAllTestData();
    }

    @Test
    @WithMockUser(username = "test-manager", roles = {"USER", "MANAGER"})
    void testGetModelsWhenCalled() throws Exception {
        mockMvc.perform(get("/models"))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("manufacturers", "models"))
                .andExpect(view().name("models"));
    }

    @Test
    @WithMockUser(username = "test-manager", roles = {"USER", "MANAGER"})
    void testAddModelWhenCalledWithValidInput() throws Exception {
        Manufacturer manufacturer = existingModel.getManufacturer();

        AddModelDTO addModelDTO = new AddModelDTO();

        addModelDTO.setName("test-model");
        addModelDTO.setManufacturer(manufacturer.getId());

        mockMvc.perform(post("/models/add")
                        .flashAttr("addModelDTO", addModelDTO)
                        .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/models"));
    }

    @Test
    @WithMockUser(username = "test-manager", roles = {"USER", "MANAGER"})
    void testGetModelWhenCalledWithValidUUID() throws Exception {
        mockMvc.perform(get("/models/edit/{uuid}", existingModelUuid))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("manufacturers", "modelToEdit"))
                .andExpect(view().name("model-edit"));
    }

    @Test
    @WithMockUser(username = "test-manager", roles = {"USER", "MANAGER"})
    void testEditModelWhenCalledWithValidInput() throws Exception {
        ModelWithManufacturerDTO modelWithManufacturerDTO = mapper.map(existingModel, ModelWithManufacturerDTO.class);

        mockMvc.perform(patch("/models/edit")
                        .flashAttr("modelWithManufacturerDTO", modelWithManufacturerDTO)
                        .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/models"));
    }

    @Test
    @WithMockUser(username = "test-manager", roles = {"USER", "MANAGER"})
    void testDeleteModelWhenCalledWithValidUUID() throws Exception {
        mockMvc.perform(delete("/models/delete/{uuid}", existingModelUuid)
                        .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/models"));
    }

    @Test
    @WithMockUser(username = "test-manager", roles = {"USER", "MANAGER"})
    void testDeleteAllModelsWhenCalled() throws Exception {
        mockMvc.perform(delete("/models/delete-all")
                        .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/models"));

        assertEquals(0, modelRepository.count());
    }

    @Test
    @WithMockUser(username = "test-manager", roles = {"USER", "MANAGER"})
    void testHandleModelErrorWhenCalled() throws Exception {
        mockMvc.perform(get("/models/edit/{uuid}", existingModelUuid))
                .andExpect(status().isOk())
                .andExpect(view().name("model-edit"));

        UUID uuid = UUID.randomUUID();

        mockMvc.perform(get("/models/edit/{uuid}", uuid))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/models"));
    }

    @Test
    @WithMockUser(username = "test-manager", roles = {"USER", "MANAGER"})
    void testHandleManufacturerErrorWhenCalled() throws Exception {
        ModelWithManufacturerDTO modelWithManufacturerDTO = mapper.map(existingModel, ModelWithManufacturerDTO.class);

        mockMvc.perform(patch("/models/edit")
                        .flashAttr("modelWithManufacturerDTO", modelWithManufacturerDTO)
                        .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/models"));

        modelWithManufacturerDTO.setManufacturerId(0L);
        UUID uuid = modelWithManufacturerDTO.getUuid();

        mockMvc.perform(patch("/models/edit")
                        .flashAttr("modelWithManufacturerDTO", modelWithManufacturerDTO)
                        .with(csrf()))
                .andExpect(redirectedUrl("/models/edit/" + uuid))
                .andExpect(flash().attribute("manufacturerError", String.format(ENTITY_NOT_FOUND, "Manufacturer")));
    }

}
