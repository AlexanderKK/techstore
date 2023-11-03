package com.techx7.techstore.model.dto.model;

import com.techx7.techstore.model.dto.manufacturer.ManufacturerDTO;
import jakarta.validation.constraints.NotNull;

public class ModelsWithManufacturersDTO extends ModelDTO {

    @NotNull
    private ManufacturerDTO manufacturerDTO;

    public ModelsWithManufacturersDTO() {}

    public ManufacturerDTO getManufacturerDTO() {
        return manufacturerDTO;
    }

    public void setManufacturerDTO(ManufacturerDTO manufacturerDTO) {
        this.manufacturerDTO = manufacturerDTO;
    }

}
