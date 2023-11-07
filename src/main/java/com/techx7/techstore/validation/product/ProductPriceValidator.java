package com.techx7.techstore.validation.product;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class ProductPriceValidator implements ConstraintValidator<ProductPrice, String> {

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if(value == null) {
            return true;
        }

        return value.matches("^\\d+[.,]*(?:[.,]\\d{0,2})?$");
    }

}
