package com.veriteigiraneza.backend.user;

// Thrown when someone signs up with an email that's already registered.
// It extends RuntimeException (an "unchecked" exception), so methods don't
// have to declare it with "throws". In Part 3 we'll turn it into a 409 Conflict.
public class EmailInUseException extends RuntimeException {

    public EmailInUseException(String email) {
        super("Email is already registered: " + email);
    }
}