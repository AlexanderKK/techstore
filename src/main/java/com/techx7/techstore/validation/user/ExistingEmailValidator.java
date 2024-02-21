package com.techx7.techstore.validation.user;

import com.techx7.techstore.repository.UserRepository;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.beans.factory.annotation.Autowired;

import static com.techx7.techstore.utils.StringUtils.isNullOrEmpty;

public class ExistingEmailValidator implements ConstraintValidator<ExistingEmail, String> {

    private final UserRepository userRepository;

    @Autowired
    public ExistingEmailValidator(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if(isNullOrEmpty(value)) {
            return true;
        }

        return userRepository.findByEmail(value).isPresent();
    }

}
