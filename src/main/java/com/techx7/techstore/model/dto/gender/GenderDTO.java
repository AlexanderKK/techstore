package com.techx7.techstore.model.dto.gender;

import com.techx7.techstore.model.enums.GenderEnum;

import static com.techx7.techstore.util.StringUtils.capitalize;

public class GenderDTO {

    private String name;

    public GenderDTO() {}

    public GenderDTO(GenderEnum genderEnum) {
        this.name = capitalize(genderEnum.name());
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

}
