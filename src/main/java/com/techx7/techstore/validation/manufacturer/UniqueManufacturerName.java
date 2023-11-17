package com.techx7.techstore.validation.manufacturer;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
@Constraint(validatedBy = UniqueManufacturerNameValidator.class)
public @interface UniqueManufacturerName {

    String message() default "Manufacturer already exists";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

}
