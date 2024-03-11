package com.techx7.techstore.validation.matches;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.PropertyAccessorFactory;

import java.util.Objects;

public class EmailMatchValidator implements ConstraintValidator<EmailMatch, Object> {

    private String first;
    private String second;
    private String message;

    @Override
    public void initialize(EmailMatch constraintAnnotation) {
        this.first = constraintAnnotation.first();
        this.second = constraintAnnotation.second();
        this.message = constraintAnnotation.message();
    }

    @Override
    public boolean isValid(Object value, ConstraintValidatorContext context) {
        return isMatchingValid(value, context, first, second, message, first);
    }

    static boolean isMatchingValid(Object value, ConstraintValidatorContext context, String first, String second, String message, String propertyNode) {
        BeanWrapper beanWrapper = PropertyAccessorFactory.forBeanPropertyAccess(value);

        Object firstPropertyValue = beanWrapper.getPropertyValue(first);
        Object secondPropertyValue = beanWrapper.getPropertyValue(second);

        boolean isValid = Objects.equals(firstPropertyValue, secondPropertyValue);
        if(!isValid) {
            context
                    .buildConstraintViolationWithTemplate(message)
                    .addPropertyNode(propertyNode)
                    .addConstraintViolation()
                    .disableDefaultConstraintViolation();
        }

        return isValid;
    }

}
