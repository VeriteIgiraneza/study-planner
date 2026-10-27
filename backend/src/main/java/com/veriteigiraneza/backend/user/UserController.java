package com.veriteigiraneza.backend.user;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    // GET /api/users/me: protected, so it only runs if the token was valid.
    // @AuthenticationPrincipal hands us verified token.
    @GetMapping("/me")
    public UserResponse me(@AuthenticationPrincipal Jwt jwt) {
        // The "subject" claim holds the user id we put in at login.
        Long userId = Long.valueOf(jwt.getSubject());
        return userService.getUser(userId);
    }
}