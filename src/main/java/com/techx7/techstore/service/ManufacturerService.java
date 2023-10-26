package com.techx7.techstore.service;

import com.techx7.techstore.model.dto.manufacturer.AddManufacturerDTO;
import com.techx7.techstore.model.dto.manufacturer.ManufacturerHomeDTO;

import java.util.List;

public interface ManufacturerService {

    void createManufacturer(AddManufacturerDTO addManufacturerDTO);

    List<ManufacturerHomeDTO> getAllManufacturers();

}
