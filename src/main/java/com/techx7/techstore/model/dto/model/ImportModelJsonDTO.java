package com.techx7.techstore.model.dto.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class ImportModelJsonDTO {

    @NotBlank(message = "Please enter a model")
    private String name;

    @NotNull(message = "Please enter a manufacturer")
    private String manufacturer;

    public ImportModelJsonDTO() {}

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getManufacturer() {
        return manufacturer;
    }

    public void setManufacturer(String manufacturer) {
        this.manufacturer = manufacturer;
    }

}
