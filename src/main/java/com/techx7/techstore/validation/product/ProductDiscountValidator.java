package com.techx7.techstore.validation.product;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.math.BigDecimal;

import static com.techx7.techstore.utils.StringUtils.isNullOrEmpty;

public class ProductDiscountValidator implements ConstraintValidator<ProductDiscount, String> {

    private long min;
    private long max;

    @Override
    public void initialize(ProductDiscount constraintAnnotation) {
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

        if(!value.matches("^\\d+\\.*(?:\\.\\d{0,2})?$")) {
            return "Please enter a valid discount percentage (i.e. 25.00)";
        }

        BigDecimal decimalPrice = new BigDecimal(value);
        if(decimalPrice.compareTo(BigDecimal.valueOf(min)) < 0 || decimalPrice.compareTo(BigDecimal.valueOf(max)) > 0) {
            return String.format("Please enter a discount between %s and %s", min, max);
        }

        return null;
    }

}
