package com.techx7.techstore.service;

import com.techx7.techstore.model.dto.model.AddModelDTO;
import com.techx7.techstore.model.dto.model.ModelWithManufacturerDTO;
import com.techx7.techstore.model.entity.Model;

import java.util.List;
import java.util.UUID;

public interface ModelService {

    void createModel(AddModelDTO addModelDTO);

    List<ModelWithManufacturerDTO> getModelsWithManufacturers();

    void deleteAllModels();

    void deleteModelByUuid(UUID uuid);

    ModelWithManufacturerDTO getModelWithManufacturerByUuid(UUID uuid);

    void editModel(ModelWithManufacturerDTO modelWithManufacturerDTO);

    void clearModelsFromProducts(Model model);

}
