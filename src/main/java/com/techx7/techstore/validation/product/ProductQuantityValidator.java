package com.techx7.techstore.validation.product;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import static com.techx7.techstore.utils.StringUtils.isNullOrEmpty;

public class ProductQuantityValidator implements ConstraintValidator<ProductQuantity, String> {

    private long min;
    private long max;

    @Override
    public void initialize(ProductQuantity constraintAnnotation) {
        this.min = constraintAnnotation.min();
        this.max = constraintAnnotation.max();

        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        String errorMessage = generateErrorMessage(value);
        if(errorMessage == null) {
            return true;
        }

        context.disableDefaultConstraintViolation();
        context.buildConstraintViolationWithTemplate(errorMessage).addConstraintViolation();

        return false;
    }

    private String generateErrorMessage(String value) {
        if(isNullOrEmpty(value)) {
            return null;
        }

        if(!value.matches( "^\\d{1,3}$")) {
            return "Please enter a valid quantity (i.e. 10)";
        }

        Integer intValue = Integer.parseInt(value);
        if(intValue < min || intValue > max) {
            return String.format("Please enter a quantity between %s and %s", min, max);
        }

        return null;
    }

}
