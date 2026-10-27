package com.veriteigiraneza.backend.auth;

// Thrown for BOTH unknown email and wrong password, with the same message,
// so attackers can't use login to discover which emails are registered.
public class InvalidCredentialsException extends RuntimeException {

    public InvalidCredentialsException() {
        super("Invalid email or password");
    }
}