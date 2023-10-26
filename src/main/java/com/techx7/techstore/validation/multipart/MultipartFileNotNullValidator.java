package com.techx7.techstore.validation.multipart;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.web.multipart.MultipartFile;

import java.util.Objects;

public class MultipartFileNotNullValidator implements ConstraintValidator<MultipartFileNotNull, MultipartFile> {

    @Override
    public boolean isValid(MultipartFile value, ConstraintValidatorContext context) {
        return !Objects.requireNonNull(value.getOriginalFilename()).isEmpty();
    }

}
