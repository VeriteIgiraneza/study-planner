package com.veriteigiraneza.backend.auth;

// Returned after a successful login.
// tokenType is "Bearer": the app sends "Authorization: Bearer <accessToken>".
public record LoginResponse(
        String accessToken,
        String tokenType,
        long expiresIn
) {
}