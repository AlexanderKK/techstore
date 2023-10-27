package com.techx7.techstore.service;

import com.techx7.techstore.model.dto.model.ModelsWithManufacturersDTO;
import com.techx7.techstore.model.dto.model.AddModelDTO;

import java.util.List;
import java.util.UUID;

public interface ModelService {

    void createModel(AddModelDTO addModelDTO);

    List<ModelsWithManufacturersDTO> getModelsWithManufacturers();

    void deleteAllModels();

    void deleteModelByUuid(UUID uuid);

}
