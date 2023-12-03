package com.techx7.techstore.model.dto.gender;

import com.techx7.techstore.model.enums.GenderEnum;
import jakarta.validation.constraints.NotBlank;

import java.util.Objects;

import static com.techx7.techstore.utils.StringUtils.capitalize;

public class GenderDTO {

    @NotBlank(message = "Please enter a gender")
    private String name;

    public GenderDTO() {}

    public GenderDTO(GenderEnum genderEnum) {
        this.name = capitalize(genderEnum.name());
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name.trim();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (o == null || getClass() != o.getClass()) return false;

        GenderDTO genderDTO = (GenderDTO) o;

        return Objects.equals(name, genderDTO.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }

}
