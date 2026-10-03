package com.veriteigiraneza.backend.user;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

// TODO: import the validation annotations you choose.
// Hint: they live in jakarta.validation.constraints.

// DTO (Data Transfer Object): the exact shape of the JSON the mobile app sends.
// We use this instead of the User entity so a client can never set fields
// like id or passwordHash directly. That's a common security practice.
//
// This is a Java record: an immutable data class where Java generates the
// constructor, accessors, equals, hashCode, and toString for you.
// Research: "Java record classes" (Oracle docs).
public record SignUpRequest(

        // TODO 2: Required, not blank, max 100 characters
        // (should match the column length in your Liquibase file).
        @NotBlank
        @Size(max = 100)
        String firstName,

        // TODO 3: Same rules as firstName.
        @NotBlank
        @Size(max = 100)
        String lastName,

        // TODO 4: Required, valid email format, max 255 characters.
        @NotBlank
        @Email
        @Size(max = 255)
        String email,

        // TODO 5: Required, minimum 8 characters.
        // Also add a maximum of 72. Research why: BCrypt only uses
        // the first 72 bytes of a password.
        //
        // Research question: what's the difference between
        // @NotNull, @NotEmpty, and @NotBlank? Which fits a name field, and why?
        @NotBlank
        @Size(min = 8, max = 72)
        String password
) {
}