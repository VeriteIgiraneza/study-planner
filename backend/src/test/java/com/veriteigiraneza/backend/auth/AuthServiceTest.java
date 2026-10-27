package com.veriteigiraneza.backend.auth;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import static org.mockito.ArgumentMatchers.any;
import org.mockito.Mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.veriteigiraneza.backend.user.User;
import com.veriteigiraneza.backend.user.UserRepository;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private TokenService tokenService;

    private AuthService authService;

    @BeforeEach
    void setUp() {
        authService = new AuthService(userRepository, passwordEncoder, tokenService);
    }

    @Test
    void login_returnsTokenForValidCredentials() {
        User user = new User("Jane", "Doe", "jane@example.com", "stored-hash");
        when(userRepository.findByEmail("jane@example.com")).thenReturn(Optional.of(user));
        when(passwordEncoder.matches("password123", "stored-hash")).thenReturn(true);
        when(tokenService.createToken(user)).thenReturn("token-value");
        when(tokenService.getExpirationSeconds()).thenReturn(3600L);

        // Messy email: login should normalize it just like sign-up does.
        LoginResponse response =
                authService.login(new LoginRequest("  Jane@Example.com ", "password123"));

        assertThat(response.accessToken()).isEqualTo("token-value");
        assertThat(response.tokenType()).isEqualTo("Bearer");
        assertThat(response.expiresIn()).isEqualTo(3600L);
    }

    @Test
    void login_rejectsWrongPassword() {
        User user = new User("Jane", "Doe", "jane@example.com", "stored-hash");
        when(userRepository.findByEmail("jane@example.com")).thenReturn(Optional.of(user));
        when(passwordEncoder.matches("wrong-password", "stored-hash")).thenReturn(false);

        assertThatThrownBy(() ->
                authService.login(new LoginRequest("jane@example.com", "wrong-password")))
                .isInstanceOf(InvalidCredentialsException.class);

        // No token should ever be created for a failed login.
        verify(tokenService, never()).createToken(any());
    }

    @Test
    void login_rejectsUnknownEmail() {
        when(userRepository.findByEmail("nobody@example.com")).thenReturn(Optional.empty());

        assertThatThrownBy(() ->
                authService.login(new LoginRequest("nobody@example.com", "password123")))
                .isInstanceOf(InvalidCredentialsException.class);

        verify(tokenService, never()).createToken(any());
    }
}