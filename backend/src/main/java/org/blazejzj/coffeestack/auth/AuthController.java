package org.blazejzj.coffeestack.auth;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.blazejzj.coffeestack.auth.dto.UserLoginRequest;
import org.blazejzj.coffeestack.auth.dto.UserLoginResult;
import org.blazejzj.coffeestack.auth.dto.UserLogoutResponse;
import org.blazejzj.coffeestack.auth.dto.UserRegisterRequest;
import org.blazejzj.coffeestack.user.dto.UserResponse;
import org.springframework.http.*;
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
    public ResponseEntity<UserResponse> loginUser(@Valid @RequestBody UserLoginRequest req) {
        UserLoginResult result = authService.loginUser(req);

        ResponseCookie cookie = ResponseCookie.from("token", result.token())
                .httpOnly(true)
                .secure(false) // note for now we don't do secure. Use true for prod
                .path("/")
                .maxAge(604800)
                .build();

        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, cookie.toString())
                .body(new UserResponse(
                        result.user().getId(),
                        result.user().getUsername(),
                        result.user().getEmail(),
                        result.user().getCreatedAt(),
                        result.user().getUpdatedAt()
                ));    }

    @PostMapping("/logout")
    public ResponseEntity<UserLogoutResponse> logoutUser(HttpServletRequest req, HttpServletResponse res) {
        UserLogoutResponse data = authService.logoutUser(req, res);
        return ResponseEntity.ok().body(data);
    }
}
