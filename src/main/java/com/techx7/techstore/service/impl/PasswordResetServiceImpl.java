package com.techx7.techstore.service.impl;

import com.techx7.techstore.exception.EntityNotFoundException;
import com.techx7.techstore.model.entity.PasswordResetCode;
import com.techx7.techstore.model.entity.User;
import com.techx7.techstore.repository.PasswordResetCodeRepository;
import com.techx7.techstore.repository.UserRepository;
import com.techx7.techstore.service.PasswordResetService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import static com.techx7.techstore.constant.Messages.ENTITY_NOT_FOUND;

@Service
public class PasswordResetServiceImpl implements PasswordResetService {

    private final UserRepository userRepository;
    private final PasswordResetCodeRepository passwordResetCodeRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public PasswordResetServiceImpl(UserRepository userRepository,
                                    PasswordResetCodeRepository passwordResetCodeRepository,
                                    PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordResetCodeRepository = passwordResetCodeRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public String createPasswordResetCode(String userEmail) {
        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new EntityNotFoundException(String.format(ENTITY_NOT_FOUND, "User")));

        String passwordResetCode = passwordEncoder.encode(String.valueOf(user.getUuid()));

        PasswordResetCode passwordResetCodeEntity = new PasswordResetCode(passwordResetCode, user);
        passwordResetCodeRepository.save(passwordResetCodeEntity);

        return passwordResetCode;
    }

}
