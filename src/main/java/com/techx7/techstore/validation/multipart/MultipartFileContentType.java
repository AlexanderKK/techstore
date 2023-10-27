package com.techx7.techstore.validation.multipart;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
@Constraint(validatedBy = MultipartFileContentTypeValidator.class)
public @interface MultipartFileContentType {

    String message() default "File type can only be .png!";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

}
