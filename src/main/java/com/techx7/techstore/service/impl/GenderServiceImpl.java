package com.techx7.techstore.service.impl;

import com.techx7.techstore.model.dto.gender.GenderDTO;
import com.techx7.techstore.model.enums.GenderEnum;
import com.techx7.techstore.service.GenderService;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.Locale;

@Service
public class GenderServiceImpl implements GenderService {

    @Override
    public List<GenderDTO> getAllGenders() {
        return toGenderDTOs(List.of(GenderEnum.values()));
    }

    public List<GenderDTO> toGenderDTOs(List<GenderEnum> genderValues) {
        return genderValues.stream()
                .map(GenderDTO::new)
                .toList();
    }

    @Override
    public boolean isGenderValid(String genderName) {
        return Arrays.stream(GenderEnum.values())
                .map(GenderEnum::name)
                .anyMatch(currentGenderName -> currentGenderName.equals(genderName.toUpperCase(Locale.getDefault())));
    }

}
