package com.techx7.techstore.service.impl;

import com.techx7.techstore.model.dto.GenderDTO;
import com.techx7.techstore.model.enums.GenderEnum;
import com.techx7.techstore.service.GenderService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.Locale;

@Service
public class GenderServiceImpl implements GenderService {

    @Override
    public List<GenderDTO> getAllGenders() {
        return Arrays.stream(GenderEnum.values())
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
