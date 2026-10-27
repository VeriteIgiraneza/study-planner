package com.veriteigiraneza.backend.auth;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

// The JSON the app sends to log in.
public record LoginRequest(
        @NotBlank
        @Email
        String email,

        @NotBlank
        String password
) {
}