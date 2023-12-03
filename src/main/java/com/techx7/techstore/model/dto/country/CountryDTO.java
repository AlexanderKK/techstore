package com.techx7.techstore.model.dto.country;

import jakarta.validation.constraints.NotBlank;

public class CountryDTO {

    @NotBlank(message = "Please enter a name")
    private String name;

    public CountryDTO() {}

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name.trim();
    }

}
