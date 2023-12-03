package com.techx7.techstore.model.dto.manufacturer;

import com.techx7.techstore.model.dto.model.ModelDTO;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.Set;

public class ManufacturerWithModelsDTO {

    @NotBlank
    private String name;

    @NotNull
    private Set<ModelDTO> models;

    public ManufacturerWithModelsDTO() {}

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name.trim();
    }

    public Set<ModelDTO> getModels() {
        return models;
    }

    public void setModelDTOs(Set<ModelDTO> models) {
        this.models = models;
    }

}
