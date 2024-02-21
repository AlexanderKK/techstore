package com.techx7.techstore.validation.user;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
@Constraint(validatedBy = ExistingEmailValidator.class)
public @interface ExistingEmail {

    String message() default "User with this email does not exist";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

}
