package com.techx7.techstore.model.dto.model;

import com.techx7.techstore.model.dto.manufacturer.ManufacturerDTO;
import jakarta.validation.constraints.NotNull;

public class ModelWithManufacturerDTO extends ModelDTO {

    private ManufacturerDTO manufacturerDTO;

    @NotNull(message = "Please choose a manufacturer")
    private Long manufacturerId;

    public ModelWithManufacturerDTO() {}

    public ManufacturerDTO getManufacturerDTO() {
        return manufacturerDTO;
    }

    public void setManufacturerDTO(ManufacturerDTO manufacturerDTO) {
        this.manufacturerDTO = manufacturerDTO;
    }

    public Long getManufacturerId() {
        return manufacturerId;
    }

    public void setManufacturerId(Long manufacturerId) {
        this.manufacturerId = manufacturerId;
    }

}
