package com.techx7.techstore.validation.multipart;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.web.multipart.MultipartFile;

public class MultipartFileMaxSizeValidator implements ConstraintValidator<MultipartFileMaxSize, MultipartFile> {

    @Override
    public boolean isValid(MultipartFile value, ConstraintValidatorContext context) {
        return value.getSize() <= 2097152;
    }

}
