package com.techx7.techstore.validation.product;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.math.BigDecimal;

public class ProductPriceValidator implements ConstraintValidator<ProductPrice, String> {

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if(value == null) {
            return true;
        }

        boolean matches = value.matches("^\\d+[.,]*(?:[.,]\\d+)?$");

        return matches;
    }

}
