package com.techx7.techstore.service;

public interface PasswordResetService {

    String createPasswordResetCode(String userEmail);

}
