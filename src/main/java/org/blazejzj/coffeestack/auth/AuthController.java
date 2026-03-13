package org.blazejzj.coffeestack.auth;

import jakarta.validation.Valid;
import org.blazejzj.coffeestack.auth.dto.UserLoginRequest;
import org.blazejzj.coffeestack.auth.dto.UserRegisterRequest;
import org.blazejzj.coffeestack.user.dto.UserResponse;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/register")
    public UserResponse registerUser(@Valid @RequestBody UserRegisterRequest req) {
        return authService.registerUser(req);
    }

    @PostMapping("/login")
    public UserResponse loginUser(@Valid @RequestBody UserLoginRequest req) {
        return authService.loginUser(req);
    }
}
