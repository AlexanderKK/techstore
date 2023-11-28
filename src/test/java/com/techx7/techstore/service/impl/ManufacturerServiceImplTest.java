package com.techx7.techstore.service.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.atLeast;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.techx7.techstore.exception.EntityNotFoundException;
import com.techx7.techstore.model.dto.manufacturer.AddManufacturerDTO;
import com.techx7.techstore.model.dto.manufacturer.ManufacturerDTO;
import com.techx7.techstore.model.dto.manufacturer.ManufacturerWithModelsDTO;
import com.techx7.techstore.model.entity.Manufacturer;
import com.techx7.techstore.model.entity.Model;
import com.techx7.techstore.model.multipart.MultipartFileImpl;
import com.techx7.techstore.repository.ManufacturerRepository;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Sort;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.web.multipart.MultipartFile;

@ContextConfiguration(classes = {ManufacturerServiceImpl.class})
@ExtendWith(SpringExtension.class)
class ManufacturerServiceImplTest {
    @MockBean
    private ManufacturerRepository manufacturerRepository;

    @Autowired
    private ManufacturerServiceImpl manufacturerServiceImpl;

    @MockBean
    private ModelMapper modelMapper;

    /**
     * Method under test:
     * {@link ManufacturerServiceImpl#createManufacturer(AddManufacturerDTO)}
     */
    @Test
    void testCreateManufacturer() throws IOException {
        Manufacturer manufacturer = new Manufacturer();
        manufacturer.setCreated(LocalDate.of(1970, 1, 1).atStartOfDay());
        manufacturer.setDescription("The characteristics of someone or something");
        manufacturer.setId(1L);
        manufacturer.setImageUrl("https://example.org/example");
        manufacturer.setModels(new HashSet<>());
        manufacturer.setModified(LocalDate.of(1970, 1, 1).atStartOfDay());
        manufacturer.setName("Name");
        manufacturer.setUuid(UUID.randomUUID());
        when(modelMapper.map(Mockito.<Object>any(), Mockito.<Class<Object>>any())).thenReturn(manufacturer);

        Manufacturer manufacturer2 = new Manufacturer();
        manufacturer2.setCreated(LocalDate.of(1970, 1, 1).atStartOfDay());
        manufacturer2.setDescription("The characteristics of someone or something");
        manufacturer2.setId(1L);
        manufacturer2.setImageUrl("https://example.org/example");
        manufacturer2.setModels(new HashSet<>());
        manufacturer2.setModified(LocalDate.of(1970, 1, 1).atStartOfDay());
        manufacturer2.setName("Name");
        manufacturer2.setUuid(UUID.randomUUID());
        when(manufacturerRepository.save(Mockito.<Manufacturer>any())).thenReturn(manufacturer2);
        AddManufacturerDTO addManufacturerDTO = mock(AddManufacturerDTO.class);
        when(addManufacturerDTO.getImage())
                .thenReturn(new MultipartFileImpl("Name", new ByteArrayInputStream(new byte[]{})));
        doNothing().when(addManufacturerDTO).setDescription(Mockito.<String>any());
        doNothing().when(addManufacturerDTO).setImage(Mockito.<MultipartFile>any());
        doNothing().when(addManufacturerDTO).setName(Mockito.<String>any());
        addManufacturerDTO.setDescription("The characteristics of someone or something");
        addManufacturerDTO.setImage(new MultipartFileImpl("Name", new ByteArrayInputStream("AXAXAXAX".getBytes("UTF-8"))));
        addManufacturerDTO.setName("Name");
        manufacturerServiceImpl.createManufacturer(addManufacturerDTO);
        verify(addManufacturerDTO).getImage();
        verify(addManufacturerDTO).setDescription(Mockito.<String>any());
        verify(addManufacturerDTO).setImage(Mockito.<MultipartFile>any());
        verify(addManufacturerDTO).setName(Mockito.<String>any());
        verify(modelMapper).map(Mockito.<Object>any(), Mockito.<Class<Object>>any());
        verify(manufacturerRepository).save(Mockito.<Manufacturer>any());
    }

    /**
     * Method under test:  {@link ManufacturerServiceImpl#getAllManufacturers()}
     */
    @Test
    void testGetAllManufacturers() {
        when(manufacturerRepository.findAll(Mockito.<Sort>any())).thenReturn(new ArrayList<>());
        List<ManufacturerDTO> actualAllManufacturers = manufacturerServiceImpl.getAllManufacturers();
        verify(manufacturerRepository).findAll(Mockito.<Sort>any());
        assertTrue(actualAllManufacturers.isEmpty());
    }

    /**
     * Method under test: {@link ManufacturerServiceImpl#getAllManufacturers()}
     */
    @Test
    void testGetAllManufacturers2() throws IOException {
        ManufacturerDTO manufacturerDTO = new ManufacturerDTO();
        manufacturerDTO.setCreated("Jan 1, 2020 8:00am GMT+0100");
        manufacturerDTO.setDescription("The characteristics of someone or something");
        manufacturerDTO.setId(1L);
        manufacturerDTO.setImage(new MultipartFileImpl("Name", new ByteArrayInputStream("AXAXAXAX".getBytes("UTF-8"))));
        manufacturerDTO.setImageUrl("https://example.org/example");
        manufacturerDTO.setModified("Jan 1, 2020 9:00am GMT+0100");
        manufacturerDTO.setName("Name");
        manufacturerDTO.setUuid(UUID.randomUUID());
        when(modelMapper.map(Mockito.<Object>any(), Mockito.<Class<ManufacturerDTO>>any())).thenReturn(manufacturerDTO);

        Manufacturer manufacturer = new Manufacturer();
        manufacturer.setCreated(LocalDate.of(1970, 1, 1).atStartOfDay());
        manufacturer.setDescription("The characteristics of someone or something");
        manufacturer.setId(1L);
        manufacturer.setImageUrl("https://example.org/example");
        manufacturer.setModels(new HashSet<>());
        manufacturer.setModified(LocalDate.of(1970, 1, 1).atStartOfDay());
        manufacturer.setName("name");
        manufacturer.setUuid(UUID.randomUUID());

        ArrayList<Manufacturer> manufacturerList = new ArrayList<>();
        manufacturerList.add(manufacturer);
        when(manufacturerRepository.findAll(Mockito.<Sort>any())).thenReturn(manufacturerList);
        List<ManufacturerDTO> actualAllManufacturers = manufacturerServiceImpl.getAllManufacturers();
        verify(modelMapper).map(Mockito.<Object>any(), Mockito.<Class<ManufacturerDTO>>any());
        verify(manufacturerRepository).findAll(Mockito.<Sort>any());
        assertEquals(1, actualAllManufacturers.size());
    }

    /**
     * Method under test: {@link ManufacturerServiceImpl#getAllManufacturers()}
     */
    @Test
    void testGetAllManufacturers3() throws IOException {
        ManufacturerDTO manufacturerDTO = new ManufacturerDTO();
        manufacturerDTO.setCreated("Jan 1, 2020 8:00am GMT+0100");
        manufacturerDTO.setDescription("The characteristics of someone or something");
        manufacturerDTO.setId(1L);
        manufacturerDTO.setImage(new MultipartFileImpl("Name", new ByteArrayInputStream("AXAXAXAX".getBytes("UTF-8"))));
        manufacturerDTO.setImageUrl("https://example.org/example");
        manufacturerDTO.setModified("Jan 1, 2020 9:00am GMT+0100");
        manufacturerDTO.setName("Name");
        manufacturerDTO.setUuid(UUID.randomUUID());
        when(modelMapper.map(Mockito.<Object>any(), Mockito.<Class<ManufacturerDTO>>any())).thenReturn(manufacturerDTO);

        Manufacturer manufacturer = new Manufacturer();
        manufacturer.setCreated(LocalDate.of(1970, 1, 1).atStartOfDay());
        manufacturer.setDescription("The characteristics of someone or something");
        manufacturer.setId(1L);
        manufacturer.setImageUrl("https://example.org/example");
        manufacturer.setModels(new HashSet<>());
        manufacturer.setModified(LocalDate.of(1970, 1, 1).atStartOfDay());
        manufacturer.setName("name");
        manufacturer.setUuid(UUID.randomUUID());

        Manufacturer manufacturer2 = new Manufacturer();
        manufacturer2.setCreated(LocalDate.of(1970, 1, 1).atStartOfDay());
        manufacturer2.setDescription("name");
        manufacturer2.setId(2L);
        manufacturer2.setImageUrl("name");
        manufacturer2.setModels(new HashSet<>());
        manufacturer2.setModified(LocalDate.of(1970, 1, 1).atStartOfDay());
        manufacturer2.setName("Name");
        manufacturer2.setUuid(UUID.randomUUID());

        ArrayList<Manufacturer> manufacturerList = new ArrayList<>();
        manufacturerList.add(manufacturer2);
        manufacturerList.add(manufacturer);
        when(manufacturerRepository.findAll(Mockito.<Sort>any())).thenReturn(manufacturerList);
        List<ManufacturerDTO> actualAllManufacturers = manufacturerServiceImpl.getAllManufacturers();
        verify(modelMapper, atLeast(1)).map(Mockito.<Object>any(), Mockito.<Class<ManufacturerDTO>>any());
        verify(manufacturerRepository).findAll(Mockito.<Sort>any());
        assertEquals(2, actualAllManufacturers.size());
    }

    /**
     * Method under test:  {@link ManufacturerServiceImpl#getAllManufacturers()}
     */
    @Test
    void testGetAllManufacturers4() {
        when(modelMapper.map(Mockito.<Object>any(), Mockito.<Class<ManufacturerDTO>>any()))
                .thenThrow(new EntityNotFoundException("An error occurred"));

        Manufacturer manufacturer = new Manufacturer();
        manufacturer.setCreated(LocalDate.of(1970, 1, 1).atStartOfDay());
        manufacturer.setDescription("The characteristics of someone or something");
        manufacturer.setId(1L);
        manufacturer.setImageUrl("https://example.org/example");
        manufacturer.setModels(new HashSet<>());
        manufacturer.setModified(LocalDate.of(1970, 1, 1).atStartOfDay());
        manufacturer.setName("name");
        manufacturer.setUuid(UUID.randomUUID());

        ArrayList<Manufacturer> manufacturerList = new ArrayList<>();
        manufacturerList.add(manufacturer);
        when(manufacturerRepository.findAll(Mockito.<Sort>any())).thenReturn(manufacturerList);
        assertThrows(EntityNotFoundException.class, () -> manufacturerServiceImpl.getAllManufacturers());
        verify(modelMapper).map(Mockito.<Object>any(), Mockito.<Class<ManufacturerDTO>>any());
        verify(manufacturerRepository).findAll(Mockito.<Sort>any());
    }

    /**
     * Method under test: {@link ManufacturerServiceImpl#deleteAllManufacturers()}
     */
    @Test
    void testDeleteAllManufacturers() {
        doNothing().when(manufacturerRepository).deleteAll();
        manufacturerServiceImpl.deleteAllManufacturers();
        verify(manufacturerRepository).deleteAll();
    }

    /**
     * Method under test: {@link ManufacturerServiceImpl#deleteAllManufacturers()}
     */
    @Test
    void testDeleteAllManufacturers2() {
        doThrow(new EntityNotFoundException("An error occurred")).when(manufacturerRepository).deleteAll();
        assertThrows(EntityNotFoundException.class, () -> manufacturerServiceImpl.deleteAllManufacturers());
        verify(manufacturerRepository).deleteAll();
    }

    /**
     * Method under test:
     * {@link ManufacturerServiceImpl#deleteManufacturerByUuid(UUID)}
     */
    @Test
    void testDeleteManufacturerByUuid() {
        doNothing().when(manufacturerRepository).deleteByUuid(Mockito.<UUID>any());
        manufacturerServiceImpl.deleteManufacturerByUuid(UUID.randomUUID());
        verify(manufacturerRepository).deleteByUuid(Mockito.<UUID>any());
    }

    /**
     * Method under test:
     * {@link ManufacturerServiceImpl#deleteManufacturerByUuid(UUID)}
     */
    @Test
    void testDeleteManufacturerByUuid2() {
        doThrow(new EntityNotFoundException("An error occurred")).when(manufacturerRepository)
                .deleteByUuid(Mockito.<UUID>any());
        assertThrows(EntityNotFoundException.class,
                () -> manufacturerServiceImpl.deleteManufacturerByUuid(UUID.randomUUID()));
        verify(manufacturerRepository).deleteByUuid(Mockito.<UUID>any());
    }

    /**
     * Method under test:  {@link ManufacturerServiceImpl#getManufacturersWithModelsDTO()}
     */
    @Test
    void testGetManufacturersWithModelsDTO() {
        when(manufacturerRepository.findAll(Mockito.<Sort>any())).thenReturn(new ArrayList<>());
        List<ManufacturerWithModelsDTO> actualManufacturersWithModelsDTO = manufacturerServiceImpl
                .getManufacturersWithModelsDTO();
        verify(manufacturerRepository).findAll(Mockito.<Sort>any());
        assertTrue(actualManufacturersWithModelsDTO.isEmpty());
    }

    /**
     * Method under test:
     * {@link ManufacturerServiceImpl#getManufacturersWithModelsDTO()}
     */
    @Test
    void testGetManufacturersWithModelsDTO2() {
        ManufacturerWithModelsDTO manufacturerWithModelsDTO = new ManufacturerWithModelsDTO();
        manufacturerWithModelsDTO.setModelDTOs(new HashSet<>());
        manufacturerWithModelsDTO.setName("Name");
        when(modelMapper.map(Mockito.<Object>any(), Mockito.<Class<ManufacturerWithModelsDTO>>any()))
                .thenReturn(manufacturerWithModelsDTO);

        Manufacturer manufacturer = new Manufacturer();
        manufacturer.setCreated(LocalDate.of(1970, 1, 1).atStartOfDay());
        manufacturer.setDescription("The characteristics of someone or something");
        manufacturer.setId(1L);
        manufacturer.setImageUrl("https://example.org/example");
        manufacturer.setModels(new HashSet<>());
        manufacturer.setModified(LocalDate.of(1970, 1, 1).atStartOfDay());
        manufacturer.setName("name");
        manufacturer.setUuid(UUID.randomUUID());

        ArrayList<Manufacturer> manufacturerList = new ArrayList<>();
        manufacturerList.add(manufacturer);
        when(manufacturerRepository.findAll(Mockito.<Sort>any())).thenReturn(manufacturerList);
        List<ManufacturerWithModelsDTO> actualManufacturersWithModelsDTO = manufacturerServiceImpl
                .getManufacturersWithModelsDTO();
        verify(modelMapper).map(Mockito.<Object>any(), Mockito.<Class<ManufacturerWithModelsDTO>>any());
        verify(manufacturerRepository).findAll(Mockito.<Sort>any());
        assertEquals(1, actualManufacturersWithModelsDTO.size());
    }

    /**
     * Method under test:
     * {@link ManufacturerServiceImpl#getManufacturersWithModelsDTO()}
     */
    @Test
    void testGetManufacturersWithModelsDTO3() {
        ManufacturerWithModelsDTO manufacturerWithModelsDTO = new ManufacturerWithModelsDTO();
        manufacturerWithModelsDTO.setModelDTOs(new HashSet<>());
        manufacturerWithModelsDTO.setName("Name");
        when(modelMapper.map(Mockito.<Object>any(), Mockito.<Class<ManufacturerWithModelsDTO>>any()))
                .thenReturn(manufacturerWithModelsDTO);

        Manufacturer manufacturer = new Manufacturer();
        manufacturer.setCreated(LocalDate.of(1970, 1, 1).atStartOfDay());
        manufacturer.setDescription("The characteristics of someone or something");
        manufacturer.setId(1L);
        manufacturer.setImageUrl("https://example.org/example");
        manufacturer.setModels(new HashSet<>());
        manufacturer.setModified(LocalDate.of(1970, 1, 1).atStartOfDay());
        manufacturer.setName("name");
        manufacturer.setUuid(UUID.randomUUID());

        Manufacturer manufacturer2 = new Manufacturer();
        manufacturer2.setCreated(LocalDate.of(1970, 1, 1).atStartOfDay());
        manufacturer2.setDescription("name");
        manufacturer2.setId(2L);
        manufacturer2.setImageUrl("name");
        manufacturer2.setModels(new HashSet<>());
        manufacturer2.setModified(LocalDate.of(1970, 1, 1).atStartOfDay());
        manufacturer2.setName("Name");
        manufacturer2.setUuid(UUID.randomUUID());

        ArrayList<Manufacturer> manufacturerList = new ArrayList<>();
        manufacturerList.add(manufacturer2);
        manufacturerList.add(manufacturer);
        when(manufacturerRepository.findAll(Mockito.<Sort>any())).thenReturn(manufacturerList);
        List<ManufacturerWithModelsDTO> actualManufacturersWithModelsDTO = manufacturerServiceImpl
                .getManufacturersWithModelsDTO();
        verify(modelMapper, atLeast(1)).map(Mockito.<Object>any(), Mockito.<Class<ManufacturerWithModelsDTO>>any());
        verify(manufacturerRepository).findAll(Mockito.<Sort>any());
        assertEquals(2, actualManufacturersWithModelsDTO.size());
    }

    /**
     * Method under test:  {@link ManufacturerServiceImpl#getManufacturersWithModelsDTO()}
     */
    @Test
    void testGetManufacturersWithModelsDTO4() {
        when(modelMapper.map(Mockito.<Object>any(), Mockito.<Class<ManufacturerWithModelsDTO>>any()))
                .thenThrow(new EntityNotFoundException("An error occurred"));

        Manufacturer manufacturer = new Manufacturer();
        manufacturer.setCreated(LocalDate.of(1970, 1, 1).atStartOfDay());
        manufacturer.setDescription("The characteristics of someone or something");
        manufacturer.setId(1L);
        manufacturer.setImageUrl("https://example.org/example");
        manufacturer.setModels(new HashSet<>());
        manufacturer.setModified(LocalDate.of(1970, 1, 1).atStartOfDay());
        manufacturer.setName("name");
        manufacturer.setUuid(UUID.randomUUID());

        ArrayList<Manufacturer> manufacturerList = new ArrayList<>();
        manufacturerList.add(manufacturer);
        when(manufacturerRepository.findAll(Mockito.<Sort>any())).thenReturn(manufacturerList);
        assertThrows(EntityNotFoundException.class, () -> manufacturerServiceImpl.getManufacturersWithModelsDTO());
        verify(modelMapper).map(Mockito.<Object>any(), Mockito.<Class<ManufacturerWithModelsDTO>>any());
        verify(manufacturerRepository).findAll(Mockito.<Sort>any());
    }

    /**
     * Method under test:
     * {@link ManufacturerServiceImpl#getManufacturerByUuid(UUID)}
     */
    @Test
    void testGetManufacturerByUuid() throws IOException {
        ManufacturerDTO manufacturerDTO = new ManufacturerDTO();
        manufacturerDTO.setCreated("Jan 1, 2020 8:00am GMT+0100");
        manufacturerDTO.setDescription("The characteristics of someone or something");
        manufacturerDTO.setId(1L);
        manufacturerDTO.setImage(new MultipartFileImpl("Name", new ByteArrayInputStream("AXAXAXAX".getBytes("UTF-8"))));
        manufacturerDTO.setImageUrl("https://example.org/example");
        manufacturerDTO.setModified("Jan 1, 2020 9:00am GMT+0100");
        manufacturerDTO.setName("Name");
        manufacturerDTO.setUuid(UUID.randomUUID());
        when(modelMapper.map(Mockito.<Object>any(), Mockito.<Class<ManufacturerDTO>>any())).thenReturn(manufacturerDTO);

        Manufacturer manufacturer = new Manufacturer();
        manufacturer.setCreated(LocalDate.of(1970, 1, 1).atStartOfDay());
        manufacturer.setDescription("The characteristics of someone or something");
        manufacturer.setId(1L);
        manufacturer.setImageUrl("https://example.org/example");
        manufacturer.setModels(new HashSet<>());
        manufacturer.setModified(LocalDate.of(1970, 1, 1).atStartOfDay());
        manufacturer.setName("Name");
        manufacturer.setUuid(UUID.randomUUID());
        Optional<Manufacturer> ofResult = Optional.of(manufacturer);
        when(manufacturerRepository.findByUuid(Mockito.<UUID>any())).thenReturn(ofResult);
        ManufacturerDTO actualManufacturerByUuid = manufacturerServiceImpl.getManufacturerByUuid(UUID.randomUUID());
        verify(manufacturerRepository).findByUuid(Mockito.<UUID>any());
        verify(modelMapper).map(Mockito.<Object>any(), Mockito.<Class<ManufacturerDTO>>any());
        assertSame(manufacturerDTO, actualManufacturerByUuid);
    }

    /**
     * Method under test:  {@link ManufacturerServiceImpl#getManufacturerByUuid(UUID)}
     */
    @Test
    void testGetManufacturerByUuid2() {
        when(modelMapper.map(Mockito.<Object>any(), Mockito.<Class<ManufacturerDTO>>any()))
                .thenThrow(new EntityNotFoundException("An error occurred"));

        Manufacturer manufacturer = new Manufacturer();
        manufacturer.setCreated(LocalDate.of(1970, 1, 1).atStartOfDay());
        manufacturer.setDescription("The characteristics of someone or something");
        manufacturer.setId(1L);
        manufacturer.setImageUrl("https://example.org/example");
        manufacturer.setModels(new HashSet<>());
        manufacturer.setModified(LocalDate.of(1970, 1, 1).atStartOfDay());
        manufacturer.setName("Name");
        manufacturer.setUuid(UUID.randomUUID());
        Optional<Manufacturer> ofResult = Optional.of(manufacturer);
        when(manufacturerRepository.findByUuid(Mockito.<UUID>any())).thenReturn(ofResult);
        assertThrows(EntityNotFoundException.class, () -> manufacturerServiceImpl.getManufacturerByUuid(UUID.randomUUID()));
        verify(manufacturerRepository).findByUuid(Mockito.<UUID>any());
        verify(modelMapper).map(Mockito.<Object>any(), Mockito.<Class<ManufacturerDTO>>any());
    }

    /**
     * Method under test:
     * {@link ManufacturerServiceImpl#getManufacturerByUuid(UUID)}
     */
    @Test
    void testGetManufacturerByUuid3() throws IOException {
        ManufacturerDTO manufacturerDTO = new ManufacturerDTO();
        manufacturerDTO.setCreated("Jan 1, 2020 8:00am GMT+0100");
        manufacturerDTO.setDescription("The characteristics of someone or something");
        manufacturerDTO.setId(1L);
        manufacturerDTO.setImage(new MultipartFileImpl("Name", new ByteArrayInputStream("AXAXAXAX".getBytes("UTF-8"))));
        manufacturerDTO.setImageUrl("https://example.org/example");
        manufacturerDTO.setModified("Jan 1, 2020 9:00am GMT+0100");
        manufacturerDTO.setName("Name");
        manufacturerDTO.setUuid(UUID.randomUUID());
        when(modelMapper.map(Mockito.<Object>any(), Mockito.<Class<Object>>any())).thenReturn("Map");
        Optional<Manufacturer> emptyResult = Optional.empty();
        when(manufacturerRepository.findByUuid(Mockito.<UUID>any())).thenReturn(emptyResult);
        assertThrows(EntityNotFoundException.class, () -> manufacturerServiceImpl.getManufacturerByUuid(UUID.randomUUID()));
        verify(manufacturerRepository).findByUuid(Mockito.<UUID>any());
    }

    /**
     * Method under test:
     * {@link ManufacturerServiceImpl#editManufacturer(ManufacturerDTO)}
     */
    @Test
    void testEditManufacturer() throws IOException {
        Manufacturer manufacturer = new Manufacturer();
        manufacturer.setCreated(LocalDate.of(1970, 1, 1).atStartOfDay());
        manufacturer.setDescription("The characteristics of someone or something");
        manufacturer.setId(1L);
        manufacturer.setImageUrl("https://example.org/example");
        manufacturer.setModels(new HashSet<>());
        manufacturer.setModified(LocalDate.of(1970, 1, 1).atStartOfDay());
        manufacturer.setName("Name");
        manufacturer.setUuid(UUID.randomUUID());
        Optional<Manufacturer> ofResult = Optional.of(manufacturer);

        Manufacturer manufacturer2 = new Manufacturer();
        manufacturer2.setCreated(LocalDate.of(1970, 1, 1).atStartOfDay());
        manufacturer2.setDescription("The characteristics of someone or something");
        manufacturer2.setId(1L);
        manufacturer2.setImageUrl("https://example.org/example");
        manufacturer2.setModels(new HashSet<>());
        manufacturer2.setModified(LocalDate.of(1970, 1, 1).atStartOfDay());
        manufacturer2.setName("Name");
        manufacturer2.setUuid(UUID.randomUUID());
        when(manufacturerRepository.save(Mockito.<Manufacturer>any())).thenReturn(manufacturer2);
        when(manufacturerRepository.findByUuid(Mockito.<UUID>any())).thenReturn(ofResult);
        ManufacturerDTO manufacturerDTO = mock(ManufacturerDTO.class);
        when(manufacturerDTO.getDescription()).thenReturn("The characteristics of someone or something");
        when(manufacturerDTO.getImageUrl()).thenReturn("https://example.org/example");
        when(manufacturerDTO.getName()).thenReturn("Name");
        when(manufacturerDTO.getUuid()).thenReturn(UUID.randomUUID());
        when(manufacturerDTO.getImage()).thenReturn(new MultipartFileImpl("Name", new ByteArrayInputStream(new byte[]{})));
        doNothing().when(manufacturerDTO).setCreated(Mockito.<String>any());
        doNothing().when(manufacturerDTO).setDescription(Mockito.<String>any());
        doNothing().when(manufacturerDTO).setId(Mockito.<Long>any());
        doNothing().when(manufacturerDTO).setImage(Mockito.<MultipartFile>any());
        doNothing().when(manufacturerDTO).setImageUrl(Mockito.<String>any());
        doNothing().when(manufacturerDTO).setModified(Mockito.<String>any());
        doNothing().when(manufacturerDTO).setName(Mockito.<String>any());
        doNothing().when(manufacturerDTO).setUuid(Mockito.<UUID>any());
        manufacturerDTO.setCreated("Jan 1, 2020 8:00am GMT+0100");
        manufacturerDTO.setDescription("The characteristics of someone or something");
        manufacturerDTO.setId(1L);
        manufacturerDTO.setImage(new MultipartFileImpl("Name", new ByteArrayInputStream("AXAXAXAX".getBytes("UTF-8"))));
        manufacturerDTO.setImageUrl("https://example.org/example");
        manufacturerDTO.setModified("Jan 1, 2020 9:00am GMT+0100");
        manufacturerDTO.setName("Name");
        manufacturerDTO.setUuid(UUID.randomUUID());
        manufacturerServiceImpl.editManufacturer(manufacturerDTO);
        verify(manufacturerDTO).getDescription();
        verify(manufacturerDTO).getImage();
        verify(manufacturerDTO).getImageUrl();
        verify(manufacturerDTO).getName();
        verify(manufacturerDTO).getUuid();
        verify(manufacturerDTO).setCreated(Mockito.<String>any());
        verify(manufacturerDTO).setDescription(Mockito.<String>any());
        verify(manufacturerDTO).setId(Mockito.<Long>any());
        verify(manufacturerDTO).setImage(Mockito.<MultipartFile>any());
        verify(manufacturerDTO).setImageUrl(Mockito.<String>any());
        verify(manufacturerDTO).setModified(Mockito.<String>any());
        verify(manufacturerDTO).setName(Mockito.<String>any());
        verify(manufacturerDTO).setUuid(Mockito.<UUID>any());
        verify(manufacturerRepository).findByUuid(Mockito.<UUID>any());
        verify(manufacturerRepository).save(Mockito.<Manufacturer>any());
    }

    /**
     * Method under test:
     * {@link ManufacturerServiceImpl#editManufacturer(ManufacturerDTO)}
     */
    @Test
    void testEditManufacturer2() throws IOException {
        ManufacturerDTO manufacturerDTO = mock(ManufacturerDTO.class);
        when(manufacturerDTO.getUuid()).thenThrow(new EntityNotFoundException("An error occurred"));
        when(manufacturerDTO.getImage()).thenReturn(new MultipartFileImpl("Name", new ByteArrayInputStream(new byte[]{})));
        doNothing().when(manufacturerDTO).setCreated(Mockito.<String>any());
        doNothing().when(manufacturerDTO).setDescription(Mockito.<String>any());
        doNothing().when(manufacturerDTO).setId(Mockito.<Long>any());
        doNothing().when(manufacturerDTO).setImage(Mockito.<MultipartFile>any());
        doNothing().when(manufacturerDTO).setImageUrl(Mockito.<String>any());
        doNothing().when(manufacturerDTO).setModified(Mockito.<String>any());
        doNothing().when(manufacturerDTO).setName(Mockito.<String>any());
        doNothing().when(manufacturerDTO).setUuid(Mockito.<UUID>any());
        manufacturerDTO.setCreated("Jan 1, 2020 8:00am GMT+0100");
        manufacturerDTO.setDescription("The characteristics of someone or something");
        manufacturerDTO.setId(1L);
        manufacturerDTO.setImage(new MultipartFileImpl("Name", new ByteArrayInputStream("AXAXAXAX".getBytes("UTF-8"))));
        manufacturerDTO.setImageUrl("https://example.org/example");
        manufacturerDTO.setModified("Jan 1, 2020 9:00am GMT+0100");
        manufacturerDTO.setName("Name");
        manufacturerDTO.setUuid(UUID.randomUUID());
        assertThrows(EntityNotFoundException.class, () -> manufacturerServiceImpl.editManufacturer(manufacturerDTO));
        verify(manufacturerDTO).getImage();
        verify(manufacturerDTO).getUuid();
        verify(manufacturerDTO).setCreated(Mockito.<String>any());
        verify(manufacturerDTO).setDescription(Mockito.<String>any());
        verify(manufacturerDTO).setId(Mockito.<Long>any());
        verify(manufacturerDTO).setImage(Mockito.<MultipartFile>any());
        verify(manufacturerDTO).setImageUrl(Mockito.<String>any());
        verify(manufacturerDTO).setModified(Mockito.<String>any());
        verify(manufacturerDTO).setName(Mockito.<String>any());
        verify(manufacturerDTO).setUuid(Mockito.<UUID>any());
    }

    /**
     * Method under test:
     * {@link ManufacturerServiceImpl#editManufacturer(ManufacturerDTO)}
     */
    @Test
    void testEditManufacturer3() throws IOException {
        Manufacturer manufacturer = mock(Manufacturer.class);
        doNothing().when(manufacturer).setId(Mockito.<Long>any());
        doNothing().when(manufacturer).setUuid(Mockito.<UUID>any());
        doNothing().when(manufacturer).editManufacturer(Mockito.<ManufacturerDTO>any());
        doNothing().when(manufacturer).setCreated(Mockito.<LocalDateTime>any());
        doNothing().when(manufacturer).setDescription(Mockito.<String>any());
        doNothing().when(manufacturer).setImageUrl(Mockito.<String>any());
        doNothing().when(manufacturer).setModels(Mockito.<Set<Model>>any());
        doNothing().when(manufacturer).setModified(Mockito.<LocalDateTime>any());
        doNothing().when(manufacturer).setName(Mockito.<String>any());
        manufacturer.setCreated(LocalDate.of(1970, 1, 1).atStartOfDay());
        manufacturer.setDescription("The characteristics of someone or something");
        manufacturer.setId(1L);
        manufacturer.setImageUrl("https://example.org/example");
        manufacturer.setModels(new HashSet<>());
        manufacturer.setModified(LocalDate.of(1970, 1, 1).atStartOfDay());
        manufacturer.setName("Name");
        manufacturer.setUuid(UUID.randomUUID());
        Optional<Manufacturer> ofResult = Optional.of(manufacturer);

        Manufacturer manufacturer2 = new Manufacturer();
        manufacturer2.setCreated(LocalDate.of(1970, 1, 1).atStartOfDay());
        manufacturer2.setDescription("The characteristics of someone or something");
        manufacturer2.setId(1L);
        manufacturer2.setImageUrl("https://example.org/example");
        manufacturer2.setModels(new HashSet<>());
        manufacturer2.setModified(LocalDate.of(1970, 1, 1).atStartOfDay());
        manufacturer2.setName("Name");
        manufacturer2.setUuid(UUID.randomUUID());
        when(manufacturerRepository.save(Mockito.<Manufacturer>any())).thenReturn(manufacturer2);
        when(manufacturerRepository.findByUuid(Mockito.<UUID>any())).thenReturn(ofResult);
        ManufacturerDTO manufacturerDTO = mock(ManufacturerDTO.class);
        when(manufacturerDTO.getUuid()).thenReturn(UUID.randomUUID());
        when(manufacturerDTO.getImage()).thenReturn(new MultipartFileImpl("Name", new ByteArrayInputStream(new byte[]{})));
        doNothing().when(manufacturerDTO).setCreated(Mockito.<String>any());
        doNothing().when(manufacturerDTO).setDescription(Mockito.<String>any());
        doNothing().when(manufacturerDTO).setId(Mockito.<Long>any());
        doNothing().when(manufacturerDTO).setImage(Mockito.<MultipartFile>any());
        doNothing().when(manufacturerDTO).setImageUrl(Mockito.<String>any());
        doNothing().when(manufacturerDTO).setModified(Mockito.<String>any());
        doNothing().when(manufacturerDTO).setName(Mockito.<String>any());
        doNothing().when(manufacturerDTO).setUuid(Mockito.<UUID>any());
        manufacturerDTO.setCreated("Jan 1, 2020 8:00am GMT+0100");
        manufacturerDTO.setDescription("The characteristics of someone or something");
        manufacturerDTO.setId(1L);
        manufacturerDTO.setImage(new MultipartFileImpl("Name", new ByteArrayInputStream("AXAXAXAX".getBytes("UTF-8"))));
        manufacturerDTO.setImageUrl("https://example.org/example");
        manufacturerDTO.setModified("Jan 1, 2020 9:00am GMT+0100");
        manufacturerDTO.setName("Name");
        manufacturerDTO.setUuid(UUID.randomUUID());
        manufacturerServiceImpl.editManufacturer(manufacturerDTO);
        verify(manufacturerDTO).getImage();
        verify(manufacturerDTO).getUuid();
        verify(manufacturerDTO).setCreated(Mockito.<String>any());
        verify(manufacturerDTO).setDescription(Mockito.<String>any());
        verify(manufacturerDTO).setId(Mockito.<Long>any());
        verify(manufacturerDTO).setImage(Mockito.<MultipartFile>any());
        verify(manufacturerDTO).setImageUrl(Mockito.<String>any());
        verify(manufacturerDTO).setModified(Mockito.<String>any());
        verify(manufacturerDTO).setName(Mockito.<String>any());
        verify(manufacturerDTO).setUuid(Mockito.<UUID>any());
        verify(manufacturer).setId(Mockito.<Long>any());
        verify(manufacturer).setUuid(Mockito.<UUID>any());
        verify(manufacturer).editManufacturer(Mockito.<ManufacturerDTO>any());
        verify(manufacturer).setCreated(Mockito.<LocalDateTime>any());
        verify(manufacturer).setDescription(Mockito.<String>any());
        verify(manufacturer).setImageUrl(Mockito.<String>any());
        verify(manufacturer).setModels(Mockito.<Set<Model>>any());
        verify(manufacturer).setModified(Mockito.<LocalDateTime>any());
        verify(manufacturer).setName(Mockito.<String>any());
        verify(manufacturerRepository).findByUuid(Mockito.<UUID>any());
        verify(manufacturerRepository).save(Mockito.<Manufacturer>any());
    }

    /**
     * Method under test:
     * {@link ManufacturerServiceImpl#editManufacturer(ManufacturerDTO)}
     */
    @Test
    void testEditManufacturer4() throws IOException {
        Optional<Manufacturer> emptyResult = Optional.empty();
        when(manufacturerRepository.findByUuid(Mockito.<UUID>any())).thenReturn(emptyResult);
        ManufacturerDTO manufacturerDTO = mock(ManufacturerDTO.class);
        when(manufacturerDTO.getUuid()).thenReturn(UUID.randomUUID());
        when(manufacturerDTO.getImage()).thenReturn(new MultipartFileImpl("Name", new ByteArrayInputStream(new byte[]{})));
        doNothing().when(manufacturerDTO).setCreated(Mockito.<String>any());
        doNothing().when(manufacturerDTO).setDescription(Mockito.<String>any());
        doNothing().when(manufacturerDTO).setId(Mockito.<Long>any());
        doNothing().when(manufacturerDTO).setImage(Mockito.<MultipartFile>any());
        doNothing().when(manufacturerDTO).setImageUrl(Mockito.<String>any());
        doNothing().when(manufacturerDTO).setModified(Mockito.<String>any());
        doNothing().when(manufacturerDTO).setName(Mockito.<String>any());
        doNothing().when(manufacturerDTO).setUuid(Mockito.<UUID>any());
        manufacturerDTO.setCreated("Jan 1, 2020 8:00am GMT+0100");
        manufacturerDTO.setDescription("The characteristics of someone or something");
        manufacturerDTO.setId(1L);
        manufacturerDTO.setImage(new MultipartFileImpl("Name", new ByteArrayInputStream("AXAXAXAX".getBytes("UTF-8"))));
        manufacturerDTO.setImageUrl("https://example.org/example");
        manufacturerDTO.setModified("Jan 1, 2020 9:00am GMT+0100");
        manufacturerDTO.setName("Name");
        manufacturerDTO.setUuid(UUID.randomUUID());
        assertThrows(EntityNotFoundException.class, () -> manufacturerServiceImpl.editManufacturer(manufacturerDTO));
        verify(manufacturerDTO).getImage();
        verify(manufacturerDTO).getUuid();
        verify(manufacturerDTO).setCreated(Mockito.<String>any());
        verify(manufacturerDTO).setDescription(Mockito.<String>any());
        verify(manufacturerDTO).setId(Mockito.<Long>any());
        verify(manufacturerDTO).setImage(Mockito.<MultipartFile>any());
        verify(manufacturerDTO).setImageUrl(Mockito.<String>any());
        verify(manufacturerDTO).setModified(Mockito.<String>any());
        verify(manufacturerDTO).setName(Mockito.<String>any());
        verify(manufacturerDTO).setUuid(Mockito.<UUID>any());
        verify(manufacturerRepository).findByUuid(Mockito.<UUID>any());
    }
}
