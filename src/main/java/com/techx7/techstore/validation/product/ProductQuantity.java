package com.techx7.techstore.validation.product;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
@Constraint(validatedBy = ProductQuantityValidator.class)
public @interface ProductQuantity {

    long min() default 1;

    long max() default 1000;

    String message() default "Invalid quantity";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

}
