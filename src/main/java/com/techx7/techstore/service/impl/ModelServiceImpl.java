package com.techx7.techstore.service.impl;

import com.techx7.techstore.model.dto.manufacturer.ManufacturerHomeDTO;
import com.techx7.techstore.model.dto.model.ModelsWithManufacturersDTO;
import com.techx7.techstore.model.dto.model.AddModelDTO;
import com.techx7.techstore.model.entity.Manufacturer;
import com.techx7.techstore.model.entity.Model;
import com.techx7.techstore.repository.ManufacturerRepository;
import com.techx7.techstore.repository.ModelRepository;
import com.techx7.techstore.service.ModelService;
import jakarta.transaction.Transactional;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class ModelServiceImpl implements ModelService {

    private final ModelRepository modelRepository;
    private final ManufacturerRepository manufacturerRepository;
    private final ModelMapper mapper;

    @Autowired
    public ModelServiceImpl(ModelRepository modelRepository,
                            ManufacturerRepository manufacturerRepository,
                            ModelMapper mapper) {
        this.modelRepository = modelRepository;
        this.manufacturerRepository = manufacturerRepository;
        this.mapper = mapper;
    }

    @Override
    public void createModel(AddModelDTO addModelDTO) {
        Model model = mapper.map(addModelDTO, Model.class);

        Optional<Manufacturer> manufacturer = manufacturerRepository.findById(addModelDTO.getManufacturer());

        if(manufacturer.isPresent()) {
            model.setManufacturer(manufacturer.get());
            modelRepository.save(model);
        }

        OffsetDateTime odt = OffsetDateTime.now( ZoneOffset.UTC );
    }

    @Override
    public List<ModelsWithManufacturersDTO> getModelsWithManufacturers() {
        List<ModelsWithManufacturersDTO> modelDTOs = new ArrayList<>();

        for (Model model : modelRepository.findAll(Sort.by("manufacturerName", "name"))) {
            ModelsWithManufacturersDTO modelDTO = mapper.map(model, ModelsWithManufacturersDTO.class);

            ManufacturerHomeDTO manufacturerDTO = mapper.map(model.getManufacturer(), ManufacturerHomeDTO.class);
            modelDTO.setManufacturerDTO(manufacturerDTO);

            modelDTOs.add(modelDTO);
        }

        return modelDTOs;
    }

    @Override
    public void deleteAllModels() {
        modelRepository.deleteAll();
    }

    @Override
    @Transactional
    public void deleteModelByUuid(UUID uuid) {
        modelRepository.deleteByUuid(uuid);
    }

}
