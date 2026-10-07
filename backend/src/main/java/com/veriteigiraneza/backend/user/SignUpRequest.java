package com.veriteigiraneza.backend.user;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

// DTO for the sign-up JSON. Kept separate from the User entity so clients
// can never set fields like id or passwordHash. Validation rules here match
// the database column limits; max 72 on password because BCrypt ignores longer input.
public record SignUpRequest(

        @NotBlank
        @Size(max = 100)
        String firstName,

        @NotBlank
        @Size(max = 100)
        String lastName,

        @NotBlank
        @Email
        @Size(max = 255)
        String email,

        @NotBlank
        @Size(min = 8, max = 72)
        String password
) {
}