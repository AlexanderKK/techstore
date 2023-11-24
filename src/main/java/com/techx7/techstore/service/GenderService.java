package com.techx7.techstore.service;

import com.techx7.techstore.model.dto.GenderDTO;

import java.util.List;

public interface GenderService {

    List<GenderDTO> getAllGenders();

    boolean isGenderValid(String genderName);

}
