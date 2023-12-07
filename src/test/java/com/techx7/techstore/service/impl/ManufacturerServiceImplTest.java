package com.techx7.techstore.service.impl;

import com.techx7.techstore.exception.EntityNotFoundException;
import com.techx7.techstore.model.dto.manufacturer.AddManufacturerDTO;
import com.techx7.techstore.model.dto.manufacturer.ManufacturerDTO;
import com.techx7.techstore.model.dto.manufacturer.ManufacturerWithModelsDTO;
import com.techx7.techstore.model.entity.Manufacturer;
import com.techx7.techstore.repository.ManufacturerRepository;
import com.techx7.techstore.repository.ModelRepository;
import com.techx7.techstore.service.CloudinaryService;
import com.techx7.techstore.service.ModelService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Sort;
import org.springframework.mock.web.MockMultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static com.techx7.techstore.testUtils.TestData.createMultipartFile;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ManufacturerServiceImplTest {

    @Mock
    private ModelMapper mapper;

    @Mock
    private ManufacturerRepository manufacturerRepository;

    @Mock
    private ModelService modelService;

    @Mock
    private ModelRepository modelRepository;

    @Mock
    private CloudinaryService cloudinaryService;

    @InjectMocks
    private ManufacturerServiceImpl manufacturerService;

    @Test
    void testCreateManufacturerValidManufacturerCreated() throws IOException {
        AddManufacturerDTO addManufacturerDTO = new AddManufacturerDTO();
        addManufacturerDTO.setName("Test Manufacturer");

        MockMultipartFile image = createMultipartFile();
        addManufacturerDTO.setImage(image);

        Manufacturer manufacturer = new Manufacturer();
        manufacturer.setName("Test Manufacturer");

        when(mapper.map(addManufacturerDTO, Manufacturer.class)).thenReturn(manufacturer);
        when(cloudinaryService.uploadFile(image, "Manufacturer", "Test Manufacturer")).thenReturn("image-url");

        // Act
        manufacturerService.createManufacturer(addManufacturerDTO);

        // Assert
        verify(manufacturerRepository).save(manufacturer);

        assertEquals("image-url", manufacturer.getImageUrl());
    }

    @Test
    void testGetAllManufacturersNoManufacturers() {
        // Arrange
        when(manufacturerRepository.findAll(Sort.by("name"))).thenReturn(new ArrayList<>());

        // Act
        List<ManufacturerDTO> result = manufacturerService.getAllManufacturers();

        // Assert
        assertTrue(result.isEmpty());
    }

    @Test
    void testGetAllManufacturersExistingManufacturers() {
        // Arrange
        List<Manufacturer> manufacturers = new ArrayList<>();

        manufacturers.add(new Manufacturer());

        when(manufacturerRepository.findAll(Sort.by("name"))).thenReturn(manufacturers);
        when(mapper.map(any(Manufacturer.class), eq(ManufacturerDTO.class))).thenReturn(new ManufacturerDTO());

        // Act
        List<ManufacturerDTO> result = manufacturerService.getAllManufacturers();

        // Assert
        assertFalse(result.isEmpty());
        assertEquals(manufacturers.size(), result.size());
    }

    @Test
    void testDeleteAllManufacturersAllManufacturersDeleted() {
        // Act
        manufacturerService.deleteAllManufacturers();

        // Assert
        verify(manufacturerRepository, times(1)).deleteAll();
    }

    @Test
    void testDeleteManufacturerByUuidExistingUuidManufacturerDeleted() {
        // Arrange
        UUID uuid = UUID.randomUUID();

        Manufacturer manufacturer = new Manufacturer();
        manufacturer.setId(1L);

        when(manufacturerRepository.findByUuid(uuid)).thenReturn(Optional.of(manufacturer));

        // Act
        manufacturerService.deleteManufacturerByUuid(uuid);

        // Assert
        verify(manufacturerRepository).deleteByUuid(uuid);
    }

    @Test
    void testGetManufacturersWithModelsDTONoManufacturers() {
        // Arrange
        when(manufacturerRepository.findAll(Sort.by("name"))).thenReturn(new ArrayList<>());

        // Act
        List<ManufacturerWithModelsDTO> result = manufacturerService.getManufacturersWithModelsDTO();

        // Assert
        assertTrue(result.isEmpty());
    }

    @Test
    void testGetManufacturersWithModelsDTOExistingManufacturers() {
        // Arrange
        List<Manufacturer> manufacturers = new ArrayList<>();
        manufacturers.add(new Manufacturer());
        when(manufacturerRepository.findAll(Sort.by("name"))).thenReturn(manufacturers);
        when(mapper.map(any(Manufacturer.class), eq(ManufacturerWithModelsDTO.class))).thenReturn(new ManufacturerWithModelsDTO());

        // Act
        List<ManufacturerWithModelsDTO> result = manufacturerService.getManufacturersWithModelsDTO();
        // Assert
        assertFalse(result.isEmpty());
        assertEquals(manufacturers.size(), result.size());
    }

    @Test
    void testGetManufacturerByUuidExistingUuid() {
        // Arrange
        UUID uuid = UUID.randomUUID();
        Manufacturer manufacturer = new Manufacturer();
        when(manufacturerRepository.findByUuid(uuid)).thenReturn(Optional.of(manufacturer));
        when(mapper.map(manufacturer, ManufacturerDTO.class)).thenReturn(new ManufacturerDTO());

        // Act
        ManufacturerDTO result = manufacturerService.getManufacturerByUuid(uuid);

        // Assert
        assertNotNull(result);
    }

    @Test
    void testGetManufacturerByUuidThrowEntityNotFoundException() {
        // Arrange
        UUID uuid = UUID.randomUUID();
        when(manufacturerRepository.findByUuid(uuid)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(EntityNotFoundException.class, () -> manufacturerService.getManufacturerByUuid(uuid));
    }

    @Test
    void testEditManufacturerExistingManufacturer() throws IOException {
        // Arrange
        ManufacturerDTO manufacturerDTO = new ManufacturerDTO();
        manufacturerDTO.setImage(createMultipartFile());

        Manufacturer manufacturer = new Manufacturer();
        when(manufacturerRepository.findByUuid(manufacturerDTO.getUuid())).thenReturn(Optional.of(manufacturer));

        // Act
        manufacturerService.editManufacturer(manufacturerDTO);

        // Assert
        verify(manufacturerRepository, times(1)).save(manufacturer);
    }

}
