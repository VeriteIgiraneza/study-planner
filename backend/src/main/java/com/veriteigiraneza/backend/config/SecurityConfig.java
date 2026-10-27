package com.veriteigiraneza.backend.config;

import java.nio.charset.StandardCharsets;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtEncoder;
import org.springframework.security.web.SecurityFilterChain;

import com.nimbusds.jose.jwk.source.ImmutableSecret;

@Configuration
public class SecurityConfig {

    // The security rules applied to every HTTP request.
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                // CSRF protection defends cookie-based sessions. This API uses
                // Bearer tokens in a header instead of cookies, so it doesn't apply.
                .csrf(csrf -> csrf.disable())

                // Stateless: the server never stores a login session.
                // Every request must prove who it is with its own token.
                .sessionManagement(session ->
                        session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))

                .authorizeHttpRequests(auth -> auth
                        // Public: you can't have a token before signing up or logging in.
                        .requestMatchers(HttpMethod.POST, "/api/auth/signup", "/api/auth/login").permitAll()
                        // Spring's error page must stay reachable, or errors turn into 401s.
                        .requestMatchers("/error").permitAll()
                        // Everything else requires a valid token.
                        .anyRequest().authenticated())

                // Read "Authorization: Bearer <token>" on each request and verify it
                // with the JwtDecoder bean below. Invalid or missing token -> 401.
                .oauth2ResourceServer(oauth2 -> oauth2.jwt(Customizer.withDefaults()));

        return http.build();
    }

    // The secret key used both to sign tokens and to verify them (HMAC-SHA256).
    @Bean
    public SecretKey jwtSecretKey(@Value("${app.jwt.secret}") String secret) {
        byte[] keyBytes = secret.getBytes(StandardCharsets.UTF_8);

        // HS256 needs a key of at least 256 bits (32 bytes). Fail fast at startup
        // instead of running with a weak key.
        if (keyBytes.length < 32) {
            throw new IllegalStateException("JWT_SECRET must be at least 32 characters long");
        }
        return new SecretKeySpec(keyBytes, "HmacSHA256");
    }

    // Creates (signs) tokens at login.
    @Bean
    public JwtEncoder jwtEncoder(SecretKey jwtSecretKey) {
        return new NimbusJwtEncoder(new ImmutableSecret<>(jwtSecretKey));
    }

    // Verifies tokens on incoming requests: checks the signature and expiration.
    @Bean
    public JwtDecoder jwtDecoder(SecretKey jwtSecretKey) {
        return NimbusJwtDecoder.withSecretKey(jwtSecretKey)
                .macAlgorithm(MacAlgorithm.HS256)
                .build();
    }
}