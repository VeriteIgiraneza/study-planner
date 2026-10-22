    package com.veriteigiraneza.backend.user;

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

    // MockitoExtension creates the @Mock objects before each test.
    // No Spring, no database: this test runs in milliseconds.
    @ExtendWith(MockitoExtension.class)
    class UserServiceTest {

        // Fake versions of UserService's dependencies.
        @Mock
        private UserRepository userRepository;

        @Mock
        private PasswordEncoder passwordEncoder;

        // The real class we're testing, built with the fakes.
        private UserService userService;

        // Runs before each @Test, so every test starts fresh.
        @BeforeEach
        void setUp() {
            userService = new UserService(userRepository, passwordEncoder);
        }

        @Test
        void signUp_rejectsDuplicateEmail() {
            // Note the lowercase email: the service normalizes before checking.
            when(userRepository.existsByEmail("test@example.com")).thenReturn(true);
            SignUpRequest request =
                    new SignUpRequest("John", "Doe", "Test@Example.com", "password123");

            // Act + Assert: calling signUp should throw EmailInUseException.
            // The () -> ... is a lambda: a small piece of code passed as an argument,
            // so assertThatThrownBy can run it and catch the exception.
            assertThatThrownBy(() -> userService.signUp(request))
                    .isInstanceOf(EmailInUseException.class);

            // Also prove the user was never saved.
            verify(userRepository, never()).save(any());
        }

        @Test
        void signUp_normalizesEmailAndHashesPassword() {
            // Arrange (given to you, since thenAnswer is new):
            // Make the fake save() return whatever user it receives.
            when(userRepository.save(any(User.class)))
                    .thenAnswer(invocation -> invocation.getArgument(0));

            // Arrange the fake encoder returns a known "hash" for this password.
            when(passwordEncoder.encode("password123")).thenReturn("hashed-password");

            // Messy email with spaces and capitals, to test normalization.
            SignUpRequest request =
                    new SignUpRequest("John", "Doe", "  Test@Example.com  ", "password123");

            // Act: call the real method and keep the result.
            UserResponse response = userService.signUp(request);

            // Assert: the email came back trimmed and lowercased.
            assertThat(response.email()).isEqualTo("test@example.com");

            // And the service actually hashed the password.
            verify(passwordEncoder).encode("password123");
        }
    }