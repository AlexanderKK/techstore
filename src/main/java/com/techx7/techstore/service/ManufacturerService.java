package com.techx7.techstore.service;

import com.techx7.techstore.model.dto.manufacturer.AddManufacturerDTO;
import com.techx7.techstore.model.dto.manufacturer.ManufacturerDTO;
import com.techx7.techstore.model.dto.manufacturer.ManufacturerWithModelsDTO;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

public interface ManufacturerService {

    void createManufacturer(AddManufacturerDTO addManufacturerDTO) throws IOException;

    List<ManufacturerDTO> getAllManufacturers();

    void deleteAllManufacturers();

    void deleteManufacturerByUuid(UUID uuid);

    List<ManufacturerWithModelsDTO> getManufacturersWithModelsDTO();

    void editManufacturer(ManufacturerDTO manufacturerDTO) throws IOException;

    ManufacturerDTO getManufacturerByUuid(UUID uuid);

}
