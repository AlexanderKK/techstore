package com.techx7.techstore.exception;

public class PasswordResetUserNotExistingException extends RuntimeException {

    public PasswordResetUserNotExistingException(String message) {
        super(message);
    }

}
