package com.techx7.techstore.service;

import com.techx7.techstore.model.dto.manufacturer.AddManufacturerDTO;
import com.techx7.techstore.model.dto.manufacturer.ManufacturerHomeDTO;

import java.util.List;
import java.util.UUID;

public interface ManufacturerService {

    void createManufacturer(AddManufacturerDTO addManufacturerDTO);

    List<ManufacturerHomeDTO> getAllManufacturers();

    void deleteAllManufacturers();

    void deleteManufacturerByUuid(UUID uuid);

}
