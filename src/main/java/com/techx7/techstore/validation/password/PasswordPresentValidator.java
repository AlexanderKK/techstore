package com.techx7.techstore.validation.password;

import com.techx7.techstore.exception.EntityNotFoundException;
import com.techx7.techstore.model.entity.User;
import com.techx7.techstore.repository.UserRepository;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.security.Principal;

import static com.techx7.techstore.constant.Messages.USER_NOT_LOGGED;

public class PasswordPresentValidator implements ConstraintValidator<PasswordPresent, String> {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public PasswordPresentValidator(UserRepository userRepository,
                                    PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void initialize(PasswordPresent constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if(value == null) {
            return true;
        }

//        User user = userRepository.findByUsername(principal.getName())
//                .orElseThrow(() -> new EntityNotFoundException(USER_NOT_LOGGED));
//
//        return passwordEncoder.matches(value, user.getPassword());

        return false;
    }

}
