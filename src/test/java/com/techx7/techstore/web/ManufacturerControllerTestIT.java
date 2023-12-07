package com.techx7.techstore.web;

import com.techx7.techstore.model.dto.manufacturer.AddManufacturerDTO;
import com.techx7.techstore.model.dto.manufacturer.ManufacturerDTO;
import com.techx7.techstore.model.entity.Manufacturer;
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
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.UUID;

import static com.techx7.techstore.testUtils.TestData.createValidMultipartImage;
import static org.hamcrest.Matchers.hasSize;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class ManufacturerControllerTestIT {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private TestData testData;

    @Autowired
    private ModelMapper mapper;

    private AddManufacturerDTO validAddManufacturerDTO;

    private UUID uuid;

    private MultipartFile validImage;

    @BeforeEach
    void setUp() throws IOException {
        testData.cleanAllTestData();

        validAddManufacturerDTO = new AddManufacturerDTO();

        validImage = createValidMultipartImage();

        validAddManufacturerDTO.setImage(validImage);

        validAddManufacturerDTO.setName("test-manufacturer");

        uuid = UUID.randomUUID();
    }

    @AfterEach
    void tearDown() {
        testData.cleanAllTestData();
    }

    @Test
    @WithMockUser(username = "test-manager", roles = {"USER", "MANAGER"})
    void testManageManufacturerWhenManufacturersExist() throws Exception {
        testData.createAndSaveManufacturer();

        mockMvc.perform(get("/manufacturers"))
                .andExpect(status().isOk())
                .andExpect(view().name("manufacturers"))
                .andExpect(model().attribute("manufacturers", hasSize(1)));
    }

    @Test
    @WithMockUser(username = "test-manager", roles = {"USER", "MANAGER"})
    void testManageManufacturerWhenNoManufacturers() throws Exception {
        mockMvc.perform(get("/manufacturers"))
                .andExpect(status().isOk())
                .andExpect(view().name("manufacturers"))
                .andExpect(model().attribute("manufacturers", hasSize(0)));
    }

    @Test
    @WithMockUser(username = "test-manager", roles = {"USER", "MANAGER"})
    void testAddManufacturerWithValidDTO() throws Exception {
        mockMvc.perform(post("/manufacturers/add")
                        .flashAttr("addManufacturerDTO", validAddManufacturerDTO)
                        .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/manufacturers"));
    }

    @Test
    @WithMockUser(username = "test-manager", roles = {"USER", "MANAGER"})
    void testAddManufacturerWithInvalidDTO() throws Exception {
        mockMvc.perform(post("/manufacturers/add")
                        .flashAttr("addManufacturerDTO", new AddManufacturerDTO())
                        .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/manufacturers"));
    }

    @Test
    @WithMockUser(username = "test-manager", roles = {"USER", "MANAGER"})
    void testGetManufacturerWhenManufacturerExists() throws Exception {
        Manufacturer manufacturer = testData.createAndSaveManufacturer();

        UUID existingUuid = manufacturer.getUuid();

        mockMvc.perform(get("/manufacturers/edit/{uuid}", existingUuid))
                .andExpect(status().isOk())
                .andExpect(view().name("manufacturer-edit"))
                .andExpect(model().attributeExists("manufacturerToEdit"));
    }

    @Test
    @WithMockUser(username = "test-manager", roles = {"USER", "MANAGER"})
    void testGetManufacturerWhenManufacturerDoesNotExist() throws Exception {
        mockMvc.perform(get("/manufacturers/edit/{uuid}", uuid))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/manufacturers"));
    }


    @Test
    @WithMockUser(username = "test-manager", roles = {"USER", "MANAGER"})
    void testEditManufacturerWithValidDTO() throws Exception {
        Manufacturer manufacturer = testData.createAndSaveManufacturer();

        ManufacturerDTO manufacturerDTO = mapper.map(manufacturer, ManufacturerDTO.class);
        manufacturerDTO.setImage(validImage);

        mockMvc.perform(patch("/manufacturers/edit")
                        .flashAttr("manufacturerDTO", manufacturerDTO)
                        .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/manufacturers"));
    }

    @Test
    @WithMockUser(username = "test-manager", roles = {"USER", "MANAGER"})
    void testEditManufacturerWithInvalidDTO() throws Exception {
        ManufacturerDTO invalidManufacturerDTO = new ManufacturerDTO();
        invalidManufacturerDTO.setUuid(uuid);

        mockMvc.perform(patch("/manufacturers/edit")
                        .flashAttr("manufacturerDTO", invalidManufacturerDTO)
                        .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/manufacturers/edit/" + invalidManufacturerDTO.getUuid()));
    }

    @Test
    @WithMockUser(username = "test-manager", roles = {"USER", "MANAGER"})
    void testDeleteManufacturer() throws Exception {
        Manufacturer manufacturer = testData.createAndSaveManufacturer();

        UUID existingUuid = manufacturer.getUuid();

        mockMvc.perform(delete("/manufacturers/delete/{uuid}", existingUuid)
                        .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/manufacturers"));
    }

    @Test
    @WithMockUser(username = "test-manager", roles = {"USER", "MANAGER"})
    void testDeleteAllManufacturers() throws Exception {
        mockMvc.perform(delete("/manufacturers/delete-all")
                        .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/manufacturers"));
    }

}
