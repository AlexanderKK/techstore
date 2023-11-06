package com.techx7.techstore.validation.product;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
@Constraint(validatedBy = ProductDiscountValidator.class)
public @interface ProductDiscount {

    String message() default "Invalid format!";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

}
