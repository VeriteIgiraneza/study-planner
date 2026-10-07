package com.veriteigiraneza.backend.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

// @Configuration tells Spring this class defines objects ("beans")
// that Spring creates once and shares across the app.
@Configuration
public class PasswordConfig {

    // TODO 1: Write a method annotated with @Bean that returns a PasswordEncoder
    // backed by BCrypt.
    //
    // Research questions (answer them in your own words before coding):
    //   a) What is a Spring bean, and why create the encoder as a bean
    //      instead of writing "new BCryptPasswordEncoder()" wherever we need it?
    //   b) Why should the method's return type be the PasswordEncoder interface
    //      rather than the BCryptPasswordEncoder class?
    //
    // Real-job note: teams depend on interfaces so the implementation can be
    // swapped later (for example, to Argon2) without changing the code that uses it.

    @Bean
    public PasswordEncoder passwordEncoder() {
        // BCrypt automatically salts each password and is slow on purpose.
        return new BCryptPasswordEncoder();
    }
}