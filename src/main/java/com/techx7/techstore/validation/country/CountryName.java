package com.techx7.techstore.validation.country;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
@Constraint(validatedBy = CountryNameValidator.class)
public @interface CountryName {

    String message() default "This country does not exist";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

}
