package com.techx7.techstore.validation.password;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
@Constraint(validatedBy = PasswordPresentValidator.class)
public @interface PasswordPresent {

    String message() default "Incorrect password";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

}
