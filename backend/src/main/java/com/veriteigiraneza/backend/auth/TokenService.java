package com.veriteigiraneza.backend.auth;

import java.time.Duration;
import java.time.Instant;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.oauth2.jwt.JwsHeader;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.stereotype.Service;

import com.veriteigiraneza.backend.user.User;

// Builds and signs JWTs for logged-in users.
@Service
public class TokenService {

    private final JwtEncoder jwtEncoder;
    private final Duration expiration;

    public TokenService(JwtEncoder jwtEncoder,
                        @Value("${app.jwt.expiration-minutes}") long expirationMinutes) {
        this.jwtEncoder = jwtEncoder;
        this.expiration = Duration.ofMinutes(expirationMinutes);
    }

    public String createToken(User user) {
        Instant now = Instant.now();

        // Claims = the facts inside the token's payload. Readable by anyone,
        // so nothing sensitive goes here: just who the user is and the time limits.
        JwtClaimsSet claims = JwtClaimsSet.builder()
                .issuer("study-planner")                 // who created the token
                .issuedAt(now)                           // when it was created
                .expiresAt(now.plus(expiration))         // when it stops working
                .subject(user.getId().toString())        // which user it belongs to
                .build();

        // The header must name the algorithm, matching the decoder (HS256).
        JwsHeader header = JwsHeader.with(MacAlgorithm.HS256).build();

        return jwtEncoder.encode(JwtEncoderParameters.from(header, claims)).getTokenValue();
    }

    // Sent to the client so the app knows when it must log in again.
    public long getExpirationSeconds() {
        return expiration.toSeconds();
    }
}