package com.techx7.techstore.service.impl;

import com.techx7.techstore.model.dto.country.CountryDTO;
import com.techx7.techstore.model.entity.Country;
import com.techx7.techstore.repository.CountryRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CountryServiceImplTest {

    @Mock
    private CountryRepository countryRepository;

    @Mock
    private ModelMapper modelMapper;

    @InjectMocks
    private CountryServiceImpl countryService;

    @Test
    void testGetAllCountriesWhenCountriesExist() {
        // Arrange
        Country country = new Country();
        country.setName("Test Country");

        List<Country> countryList = List.of(country);

        when(countryRepository.findAll()).thenReturn(countryList);
        when(modelMapper.map(country, CountryDTO.class)).thenReturn(new CountryDTO());

        // Act
        List<CountryDTO> result = countryService.getAllCountries();

        // Assert
        assertFalse(result.isEmpty());
        assertEquals(result.size(), countryList.size());
    }

    @Test
    void testGetAllCountriesWhenNoCountriesExist() {
        // Arrange
        when(countryRepository.findAll()).thenReturn(Collections.emptyList());

        // Act
        List<CountryDTO> result = countryService.getAllCountries();

        // Assert
        assertTrue(result.isEmpty());
    }

    @Test
    void testGetAllCountriesWhenCountriesNull() {
        // Arrange
        when(countryRepository.findAll()).thenReturn(new ArrayList<>());

        // Act
        List<CountryDTO> result = countryService.getAllCountries();

        // Assert
        assertTrue(result.isEmpty());
    }

}
