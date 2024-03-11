package com.techx7.techstore.validation.matches;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = PasswordMatchValidator.class)
public @interface PasswordMatch {

    String first();

    String second();

    String message() default "Passwords should match!";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

}
