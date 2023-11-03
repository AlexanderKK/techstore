package com.techx7.techstore.service;

import com.techx7.techstore.model.dto.manufacturer.AddManufacturerDTO;
import com.techx7.techstore.model.dto.manufacturer.ManufacturerDTO;
import com.techx7.techstore.model.dto.manufacturer.ManufacturerWithModelsDTO;

import java.util.List;
import java.util.UUID;

public interface ManufacturerService {

    void createManufacturer(AddManufacturerDTO addManufacturerDTO);

    List<ManufacturerDTO> getAllManufacturers();

    void deleteAllManufacturers();

    void deleteManufacturerByUuid(UUID uuid);

    List<ManufacturerWithModelsDTO> getManufacturersWithModelsDTO();

}
