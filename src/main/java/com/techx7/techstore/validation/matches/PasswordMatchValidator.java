package com.techx7.techstore.validation.matches;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import static com.techx7.techstore.validation.matches.EmailMatchValidator.isMatchingValid;

public class PasswordMatchValidator implements ConstraintValidator<PasswordMatch, Object> {

    private String first;
    private String second;
    private String message;

    @Override
    public void initialize(PasswordMatch constraintAnnotation) {
        this.first = constraintAnnotation.first();
        this.second = constraintAnnotation.second();
        this.message = constraintAnnotation.message();
    }

    @Override
    public boolean isValid(Object value, ConstraintValidatorContext context) {
        return isMatchingValid(value, context, first, second, message, second);
    }

}
