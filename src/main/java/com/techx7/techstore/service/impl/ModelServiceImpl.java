package com.techx7.techstore.service.impl;

import com.techx7.techstore.model.dto.model.AddModelDTO;
import com.techx7.techstore.model.dto.model.ModelWithManufacturerDTO;
import com.techx7.techstore.model.entity.Model;
import com.techx7.techstore.repository.ManufacturerRepository;
import com.techx7.techstore.repository.ModelRepository;
import com.techx7.techstore.service.ModelService;
import jakarta.transaction.Transactional;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class ModelServiceImpl implements ModelService {

    private final ModelRepository modelRepository;
    private final ModelMapper mapper;

    @Autowired
    public ModelServiceImpl(ModelRepository modelRepository,
                            ManufacturerRepository manufacturerRepository,
                            ModelMapper mapper) {
        this.modelRepository = modelRepository;
        this.mapper = mapper;
    }

    @Override
    public void createModel(AddModelDTO addModelDTO) {
        Model model = mapper.map(addModelDTO, Model.class);

        modelRepository.save(model);
    }

    @Override
    public List<ModelWithManufacturerDTO> getModelsWithManufacturers() {
        return modelRepository.findAll(Sort.by("manufacturerName", "name"))
                .stream()
                .map(model -> mapper.map(model, ModelWithManufacturerDTO.class))
                .toList();
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
