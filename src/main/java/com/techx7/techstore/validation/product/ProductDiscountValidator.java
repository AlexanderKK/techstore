package com.techx7.techstore.validation.product;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.math.BigDecimal;

public class ProductDiscountValidator implements ConstraintValidator<ProductDiscount, String> {

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if(value == null || value.trim().isEmpty()) {
            return true;
        }

        boolean matches = value.matches("^\\d+[.,]*(?:[.,]\\d+)?$");

        if(matches) {
            BigDecimal decimal = new BigDecimal(value);

            if(decimal.compareTo(BigDecimal.ONE) >= 0 && decimal.compareTo(BigDecimal.valueOf(100)) <= 0) {
                return true;
            }
        }

        return false;
    }

}
