package com.veriteigiraneza.backend.user;

import java.util.Locale;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

// @Service marks this as a Spring bean that holds business logic.
@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    // Constructor injection: Spring sees this constructor and automatically
    // passes in the UserRepository and the PasswordEncoder bean from PasswordConfig.
    // Fields are final, so they can't be changed after the object is created.
    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    // @Transactional: everything in this method runs as one database transaction.
    // If anything fails partway through, nothing is saved.
    @Transactional
    public UserResponse signUp(SignUpRequest request) {

                // Normalize so "Test@Example.com " and "test@example.com" are the same account.
        // Locale.ROOT keeps the result the same regardless of the server's language settings.
        String email = request.email().trim().toLowerCase(Locale.ROOT);

        // Guard clause: stop early if the email is already registered.
        if (userRepository.existsByEmail(email)) {
            throw new EmailInUseException(email);
        }

        // Only the BCrypt hash is stored, never the plain password.
        String passwordHash = passwordEncoder.encode(request.password());

        User user = new User(
                request.firstName().trim(),
                request.lastName().trim(),
                email,
                passwordHash
        );

        // save() returns the user with its database generated id.
        User savedUser = userRepository.save(user);
        return UserResponse.from(savedUser);
    }
}