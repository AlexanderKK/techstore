package com.techx7.techstore.service.impl;

import com.techx7.techstore.model.dto.gender.GenderDTO;
import com.techx7.techstore.model.enums.GenderEnum;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class GenderServiceImplTest {

    @InjectMocks
    private GenderServiceImpl genderService;

    @Test
    void testGetAllGenders() {
        List<GenderDTO> expectedGenders = Arrays.asList(
                new GenderDTO(GenderEnum.MALE),
                new GenderDTO(GenderEnum.FEMALE),
                new GenderDTO(GenderEnum.OTHER)
        );

        List<GenderDTO> actualGenders = genderService.getAllGenders();

        assertEquals(expectedGenders.size(), actualGenders.size());

        assertEquals(expectedGenders, actualGenders);
    }

    @Test
    void testIsGenderValidWithValidGender() {
        String validGender = "MALE";

        boolean isValid = genderService.isGenderValid(validGender);

        assertTrue(isValid);
    }

    @Test
    void testIsGenderValidWithInvalidGender() {
        String invalidGender = "UNKNOWN";

        boolean isValid = genderService.isGenderValid(invalidGender);

        assertFalse(isValid);
    }

}
