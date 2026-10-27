package com.veriteigiraneza.backend.user;

// Thrown when a valid token points to a user that no longer exists.
public class UserNotFoundException extends RuntimeException {

    public UserNotFoundException(Long id) {
        super("User not found: " + id);
    }
}