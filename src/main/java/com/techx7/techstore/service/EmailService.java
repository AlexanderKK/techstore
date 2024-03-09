package com.techx7.techstore.service;

public interface EmailService {

    void sendRegistrationEmail(String userEmail, String username, String activationCode);

    void sendPasswordRecoveryEmail(String emailOrUsername);

}
