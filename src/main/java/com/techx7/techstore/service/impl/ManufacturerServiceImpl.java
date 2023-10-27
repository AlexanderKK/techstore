package com.techx7.techstore.service.impl;

import com.techx7.techstore.model.dto.manufacturer.AddManufacturerDTO;
import com.techx7.techstore.model.dto.manufacturer.ManufacturerHomeDTO;
import com.techx7.techstore.model.entity.Manufacturer;
import com.techx7.techstore.repository.ManufacturerRepository;
import com.techx7.techstore.service.ManufacturerService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

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
        manufacturer.setImageUrl(addManufacturerDTO.getImage().getOriginalFilename());

        manufacturerRepository.save(manufacturer);
    }

    @Override
    public List<ManufacturerHomeDTO> getAllManufacturers() {
        return manufacturerRepository.findAll()
                .stream()
                .map(manufacturer -> mapper.map(manufacturer, ManufacturerHomeDTO.class))
                .collect(Collectors.toList());
    }

    @Override
    public void deleteAllManufacturers() {
        manufacturerRepository.deleteAll();
    }

    @Override
    public void deleteManufacturerById(Long id) {
        manufacturerRepository.deleteById(id);
    }

}
