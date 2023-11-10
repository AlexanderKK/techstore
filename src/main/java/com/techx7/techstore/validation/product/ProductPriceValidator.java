package com.techx7.techstore.validation.product;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.math.BigDecimal;

public class ProductPriceValidator implements ConstraintValidator<ProductPrice, String> {

    private long min;
    private long max;

    @Override
    public void initialize(ProductPrice constraintAnnotation) {
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
        if(value.isBlank()) {
            return "Please enter a price";
        }

        if(!value.matches("^\\d+\\.*(?:\\.\\d{0,2})?$")) {
            return "Please enter a valid price (i.e. 1500.00)";
        }

        BigDecimal decimalPrice = new BigDecimal(value);
        if(decimalPrice.compareTo(BigDecimal.valueOf(min)) < 0 || decimalPrice.compareTo(BigDecimal.valueOf(max)) > 0) {
            return String.format("Please enter a price between %s and %s", min, max);
        }

        return null;
    }

}
