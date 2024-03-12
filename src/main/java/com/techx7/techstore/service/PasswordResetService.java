package com.techx7.techstore.service;

import com.techx7.techstore.model.dto.user.ResetPasswordDTO;

public interface PasswordResetService {

    String createPasswordResetCode(String userEmail);

    void resetPassword(ResetPasswordDTO resetPasswordDTO);

    boolean isResetCodeValid(String resetCode, String userEmail);

    boolean isUserPresent(String userEmail);

    void cleanUpExpiredPasswordResetLinks(int minutesUntilExpiration);

}
