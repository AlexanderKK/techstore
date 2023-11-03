package com.techx7.techstore.validation.product;

import com.techx7.techstore.repository.ProductRepository;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.beans.factory.annotation.Autowired;

public class UniqueProductModelValidator implements ConstraintValidator<UniqueProductModel, Long> {

    private final ProductRepository productRepository;

    @Autowired
    public UniqueProductModelValidator(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @Override
    public boolean isValid(Long value, ConstraintValidatorContext context) {
        if(value == null) {
            return true;
        }

        return productRepository.findByModelId(value).isEmpty();
    }

}
