package com.techx7.techstore.service.impl;

import com.techx7.techstore.model.dto.manufacturer.AddManufacturerDTO;
import com.techx7.techstore.model.dto.manufacturer.ManufacturerDTO;
import com.techx7.techstore.model.dto.manufacturer.ManufacturerWithModelsDTO;
import com.techx7.techstore.model.entity.Manufacturer;
import com.techx7.techstore.repository.ManufacturerRepository;
import com.techx7.techstore.service.ManufacturerService;
import jakarta.transaction.Transactional;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class ManufacturerServiceImpl implements ManufacturerService {

    private final ModelMapper mapper;
    private final ManufacturerRepository manufacturerRepository;

    @Autowired
    public ManufacturerServiceImpl(ModelMapper mapper,
                                   ManufacturerRepository manufacturerRepository) {
        this.mapper = mapper;
        this.manufacturerRepository = manufacturerRepository;
    }

    @Override
    public void createManufacturer(AddManufacturerDTO addManufacturerDTO) {
        Manufacturer manufacturer = mapper.map(addManufacturerDTO, Manufacturer.class);

        manufacturerRepository.save(manufacturer);
    }

    @Override
    public List<ManufacturerDTO> getAllManufacturers() {
        return manufacturerRepository.findAll(Sort.by("name"))
                .stream()
                .map(manufacturer -> mapper.map(manufacturer, ManufacturerDTO.class))
                .toList();
    }

    @Override
    public void deleteAllManufacturers() {
        manufacturerRepository.deleteAll();
    }

    @Override
    @Transactional
    public void deleteManufacturerByUuid(UUID uuid) {
        manufacturerRepository.deleteByUuid(uuid);
    }

    @Override
    public List<ManufacturerWithModelsDTO> getManufacturersWithModelsDTO() {
        return manufacturerRepository.findAll(Sort.by("name"))
                .stream()
                .map(manufacturer -> mapper.map(manufacturer, ManufacturerWithModelsDTO.class))
                .toList();
    }

}
