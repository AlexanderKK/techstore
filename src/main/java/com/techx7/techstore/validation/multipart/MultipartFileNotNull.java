package com.techx7.techstore.validation.multipart;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
@Constraint(validatedBy = MultipartFileNotNullValidator.class)
public @interface MultipartFileNotNull {

    String message() default "You must choose an image!";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

}
