package com.techx7.techstore.validation.manufacturer;

import com.techx7.techstore.repository.ManufacturerRepository;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.beans.factory.annotation.Autowired;

public class UniqueManufacturerNameValidator implements ConstraintValidator<UniqueManufacturerName, String> {

    private final ManufacturerRepository manufacturerRepository;

    @Autowired
    public UniqueManufacturerNameValidator(ManufacturerRepository manufacturerRepository) {
        this.manufacturerRepository = manufacturerRepository;
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if(value == null) {
            return true;
        }

        return manufacturerRepository.findByName(value).isEmpty();
    }

}
