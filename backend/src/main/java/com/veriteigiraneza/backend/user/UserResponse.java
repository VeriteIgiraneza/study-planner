package com.veriteigiraneza.backend.user;

// What the API sends back after sign-up.
// Notice there's no password or passwordHash field. The hash should never
// leave the server, even though it can't be reversed.
public record UserResponse(
        Long id,
        String firstName,
        String lastName,
        String email
) {

    // TODO 6: Write a static factory method named "from" that takes a User
    // and returns a UserResponse built from the User's getters.
    //
    // Research: "static factory method" in Java. Why do teams often prefer
    // UserResponse.from(user) over building the record inline in every controller?

    public static UserResponse from(User user) {
        return new UserResponse(
                user.getId(),
                user.getFirstName(),
                user.getLastName(),
                user.getEmail()
        );
    }
}