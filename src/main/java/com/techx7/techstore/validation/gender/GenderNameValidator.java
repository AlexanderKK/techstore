package com.techx7.techstore.validation.gender;

import com.techx7.techstore.service.GenderService;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.beans.factory.annotation.Autowired;

import static com.techx7.techstore.utils.StringUtils.isNullOrEmpty;

public class GenderNameValidator implements ConstraintValidator<GenderName, String> {

    private final GenderService genderService;

    @Autowired
    public GenderNameValidator(GenderService genderService) {
        this.genderService = genderService;
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if(isNullOrEmpty(value)) {
            return true;
        }

        return genderService.isGenderValid(value);
    }

}
