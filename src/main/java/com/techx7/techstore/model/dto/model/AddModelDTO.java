package com.techx7.techstore.model.dto.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class AddModelDTO {

    @NotBlank(message = "Please enter a model")
    private String name;

    @NotNull(message = "Please choose a manufacturer")
    private Long manufacturer;

    public AddModelDTO() {}

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name.trim();
    }

    public Long getManufacturer() {
        return manufacturer;
    }

    public void setManufacturer(Long manufacturer) {
        this.manufacturer = manufacturer;
    }

}
