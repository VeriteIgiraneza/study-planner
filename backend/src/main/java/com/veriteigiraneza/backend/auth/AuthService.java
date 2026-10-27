package com.veriteigiraneza.backend.auth;

import java.util.Locale;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.veriteigiraneza.backend.user.User;
import com.veriteigiraneza.backend.user.UserRepository;

@Service
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final TokenService tokenService;

    public AuthService(UserRepository userRepository,
                       PasswordEncoder passwordEncoder,
                       TokenService tokenService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.tokenService = tokenService;
    }

    // readOnly: this method only reads from the database.
    @Transactional(readOnly = true)
    public LoginResponse login(LoginRequest request) {
        // Same normalization as sign-up, so "Jane@Example.com " finds the account.
        String email = request.email().trim().toLowerCase(Locale.ROOT);

        // Unknown email same exception as a wrong password.
        User user = userRepository.findByEmail(email)
                .orElseThrow(InvalidCredentialsException::new);

        // matches() hashes the typed password with the salt stored inside the
        // hash and compares the results. The real password is never stored.
        if (!passwordEncoder.matches(request.password(), user.getPasswordHash())) {
            throw new InvalidCredentialsException();
        }

        String token = tokenService.createToken(user);
        return new LoginResponse(token, "Bearer", tokenService.getExpirationSeconds());
    }
}