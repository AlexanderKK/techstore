package com.techx7.techstore.model.dto.model;

import com.techx7.techstore.model.dto.manufacturer.ManufacturerHomeDTO;
import com.techx7.techstore.model.dto.model.ModelHomeDTO;
import jakarta.validation.constraints.NotNull;

public class ModelsWithManufacturersDTO extends ModelHomeDTO {

    @NotNull
    private ManufacturerHomeDTO manufacturerDTO;

    public ModelsWithManufacturersDTO() {}

    public ManufacturerHomeDTO getManufacturerDTO() {
        return manufacturerDTO;
    }

    public void setManufacturerDTO(ManufacturerHomeDTO manufacturerDTO) {
        this.manufacturerDTO = manufacturerDTO;
    }

}
