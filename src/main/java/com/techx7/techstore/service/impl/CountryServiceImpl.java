package com.techx7.techstore.service.impl;

import com.techx7.techstore.model.dto.country.CountryDTO;
import com.techx7.techstore.repository.CountryRepository;
import com.techx7.techstore.service.CountryService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CountryServiceImpl implements CountryService {

    private final CountryRepository countryRepository;
    private final ModelMapper mapper;

    @Autowired
    public CountryServiceImpl(CountryRepository countryRepository,
                              ModelMapper mapper) {
        this.countryRepository = countryRepository;
        this.mapper = mapper;
    }

    @Override
    public List<CountryDTO> getAllCountries() {
        return countryRepository.findAll()
                .stream()
                .map(country -> mapper.map(country, CountryDTO.class))
                .toList();
    }

}
