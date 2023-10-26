package com.techx7.techstore.service.impl;

import com.techx7.techstore.model.dto.AddManufacturerDTO;
import com.techx7.techstore.model.entity.Manufacturer;
import com.techx7.techstore.repository.ManufacturerRepository;
import com.techx7.techstore.service.ManufacturerService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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

}
