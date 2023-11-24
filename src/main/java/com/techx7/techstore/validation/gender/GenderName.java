package com.techx7.techstore.validation.gender;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
@Constraint(validatedBy = GenderNameValidator.class)
public @interface GenderName {

    String message() default "This gender does not exist";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

}
