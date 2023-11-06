package com.techx7.techstore.validation.multipart;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.web.multipart.MultipartFile;

import static com.techx7.techstore.util.StringUtils.isNullOrEmpty;

public class MultipartFileContentTypeValidator implements ConstraintValidator<MultipartFileContentType, MultipartFile> {

    @Override
    public boolean isValid(MultipartFile value, ConstraintValidatorContext context) {
        if(isNullOrEmpty(value.getOriginalFilename())) {
            return true;
        }

        return value.getContentType().equals("image/png") || value.getContentType().equals("image/jpeg");
    }

}
