package com.techx7.techstore.service.impl;

import com.techx7.techstore.exception.EntityNotFoundException;
import com.techx7.techstore.exception.PasswordResetCodeExpiredException;
import com.techx7.techstore.exception.PasswordResetUserNotExistingException;
import com.techx7.techstore.model.dto.user.ResetPasswordDTO;
import com.techx7.techstore.model.entity.PasswordResetCode;
import com.techx7.techstore.model.entity.User;
import com.techx7.techstore.repository.PasswordResetCodeRepository;
import com.techx7.techstore.repository.UserRepository;
import com.techx7.techstore.service.PasswordResetService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

import static com.techx7.techstore.constant.Messages.*;

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

    @Override
    public void resetPassword(ResetPasswordDTO resetPasswordDTO) {
        User user = userRepository.findByEmail(resetPasswordDTO.getEmail())
                .orElseThrow(() -> new PasswordResetUserNotExistingException(USER_WITH_THIS_EMAIL_NOT_PRESENT));

        passwordResetCodeRepository.findByPasswordResetCodeAndUser(resetPasswordDTO.getResetCode(), user)
                .orElseThrow(() -> new PasswordResetCodeExpiredException(PASSWORD_RESET_CODE_EXPIRED));

        String newRawPassword = resetPasswordDTO.getPassword();
        String newPassword = passwordEncoder.encode(newRawPassword);

        user.setPassword(newPassword);
        user.setModified(LocalDateTime.now());

        userRepository.save(user);
    }

    @Override
    public boolean isResetCodeValid(String resetCode, String userEmail) {
        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new EntityNotFoundException(String.format(ENTITY_NOT_FOUND, "User")));

        return passwordResetCodeRepository.findByPasswordResetCodeAndUser(resetCode, user).isPresent();
    }

    @Override
    public boolean isUserPresent(String userEmail) {
        return userRepository.findByEmail(userEmail).isPresent();
    }

}
