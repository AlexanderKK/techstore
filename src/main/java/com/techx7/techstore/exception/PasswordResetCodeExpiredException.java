package com.techx7.techstore.exception;

public class PasswordResetCodeExpiredException extends RuntimeException {

    public PasswordResetCodeExpiredException(String message) {
        super(message);
    }

}
