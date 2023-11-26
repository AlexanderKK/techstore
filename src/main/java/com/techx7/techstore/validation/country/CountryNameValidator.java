package com.techx7.techstore.validation.country;

import com.techx7.techstore.repository.CountryRepository;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.beans.factory.annotation.Autowired;

import static com.techx7.techstore.utils.StringUtils.isNullOrEmpty;

public class CountryNameValidator implements ConstraintValidator<CountryName, String> {

    private final CountryRepository countryRepository;

    @Autowired
    public CountryNameValidator(CountryRepository countryRepository) {
        this.countryRepository = countryRepository;
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if(isNullOrEmpty(value)) {
            return true;
        }

        return countryRepository.findByName(value).isPresent();
    }

}
