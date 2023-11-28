package com.techx7.techstore.service.impl;

import com.techx7.techstore.exception.EntityNotFoundException;
import com.techx7.techstore.exception.ManufacturerNotFoundException;
import com.techx7.techstore.model.dto.model.AddModelDTO;
import com.techx7.techstore.model.dto.model.ModelWithManufacturerDTO;
import com.techx7.techstore.model.entity.Manufacturer;
import com.techx7.techstore.model.entity.Model;
import com.techx7.techstore.repository.ManufacturerRepository;
import com.techx7.techstore.repository.ModelRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Sort;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ModelServiceImplTest {

    @Mock
    private ModelMapper mapper;

    @Mock
    private ModelRepository modelRepository;

    @Mock
    private ManufacturerRepository manufacturerRepository;

    @InjectMocks
    private ModelServiceImpl modelService;

    @Test
    void testCreateModelValidAddModelDTO() {
        // Arrange
        AddModelDTO addModelDTO = new AddModelDTO();
        Model model = new Model();
        when(mapper.map(addModelDTO, Model.class)).thenReturn(model);

        // Act
        modelService.createModel(addModelDTO);

        // Assert
        verify(modelRepository, times(1)).save(model);
    }

    @Test
    void testGetModelsWithManufacturersNoModels() {
        // Arrange
        when(modelRepository.findAll(Sort.by("manufacturerName", "name"))).thenReturn(new ArrayList<>());

        // Act
        List<ModelWithManufacturerDTO> result = modelService.getModelsWithManufacturers();

        // Assert
        assertTrue(result.isEmpty());
    }

    @Test
    void testGetModelsWithManufacturersExistingModels() {
        // Arrange
        List<Model> models = new ArrayList<>();
        models.add(new Model());
        when(modelRepository.findAll(Sort.by("manufacturerName", "name"))).thenReturn(models);
        when(mapper.map(any(Model.class), eq(ModelWithManufacturerDTO.class))).thenReturn(new ModelWithManufacturerDTO());

        // Act
        List<ModelWithManufacturerDTO> result = modelService.getModelsWithManufacturers();

        // Assert
        assertFalse(result.isEmpty());
        assertEquals(models.size(), result.size());
    }

    @Test
    void testDeleteAllModelsAllModelsDeleted() {
        // Act
        modelService.deleteAllModels();

        // Assert
        verify(modelRepository, times(1)).deleteAll();
    }

    @Test
    void testDeleteModelByUuidExistingUuidModelDeleted() {
        // Arrange
        UUID uuid = UUID.randomUUID();

        // Act
        modelService.deleteModelByUuid(uuid);

        // Assert
        verify(modelRepository, times(1)).deleteByUuid(uuid);
    }

    @Test
    void testGetModelWithManufacturerByUuidExistingUuid() {
        // Arrange
        UUID uuid = UUID.randomUUID();
        Model model = new Model();
        when(modelRepository.findByUuid(uuid)).thenReturn(Optional.of(model));
        when(mapper.map(model, ModelWithManufacturerDTO.class)).thenReturn(new ModelWithManufacturerDTO());

        // Act
        ModelWithManufacturerDTO result = modelService.getModelWithManufacturerByUuid(uuid);

        // Assert
        assertNotNull(result);
    }

    @Test
    void testGetModelWithManufacturerByUuidThrowEntityNotFoundException() {
        // Arrange
        UUID uuid = UUID.randomUUID();
        when(modelRepository.findByUuid(uuid)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(EntityNotFoundException.class, () -> modelService.getModelWithManufacturerByUuid(uuid));
    }

    @Test
    void testEditModelExistingModelWithManufacturerDTO() {
        // Arrange
        ModelWithManufacturerDTO modelWithManufacturerDTO = new ModelWithManufacturerDTO();
        Model model = new Model();
        Manufacturer manufacturer = new Manufacturer();
        when(modelRepository.findByUuid(modelWithManufacturerDTO.getUuid())).thenReturn(Optional.of(model));
        when(manufacturerRepository.findById(modelWithManufacturerDTO.getManufacturerId())).thenReturn(Optional.of(manufacturer));

        // Act
        modelService.editModel(modelWithManufacturerDTO);

        // Assert
        verify(modelRepository, times(1)).save(model);
    }

    @Test
    void testEditModelThrowEntityNotFoundException() {
        // Arrange
        ModelWithManufacturerDTO modelWithManufacturerDTO = new ModelWithManufacturerDTO();
        when(modelRepository.findByUuid(modelWithManufacturerDTO.getUuid())).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(EntityNotFoundException.class, () -> modelService.editModel(modelWithManufacturerDTO));
    }

    @Test
    void testEditModelThrowManufacturerNotFoundException() {
        // Arrange
        ModelWithManufacturerDTO modelWithManufacturerDTO = new ModelWithManufacturerDTO();
        Model model = new Model();
        when(modelRepository.findByUuid(modelWithManufacturerDTO.getUuid())).thenReturn(Optional.of(model));
        when(manufacturerRepository.findById(modelWithManufacturerDTO.getManufacturerId())).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(ManufacturerNotFoundException.class, () -> modelService.editModel(modelWithManufacturerDTO));
    }

}
