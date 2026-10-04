package com.veriteigiraneza.backend.auth;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.veriteigiraneza.backend.user.SignUpRequest;
import com.veriteigiraneza.backend.user.UserResponse;
import com.veriteigiraneza.backend.user.UserService;

import jakarta.validation.Valid;

// @RestController: this class handles HTTP requests and returns JSON.
// @RequestMapping: every endpoint in this class starts with /api/auth.
@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final UserService userService;

    public AuthController(UserService userService) {
        this.userService = userService;
    }

    // POST /api/auth/signup
    // @Valid runs the validation rules on SignUpRequest; invalid input returns 400.
    // @ResponseStatus(CREATED) returns 201 because a new resource (a user) was created.
    
    @PostMapping("/signup")
    @ResponseStatus(HttpStatus.CREATED)
    public UserResponse signUp(@Valid @RequestBody SignUpRequest request) {
        return userService.signUp(request);
    }
}