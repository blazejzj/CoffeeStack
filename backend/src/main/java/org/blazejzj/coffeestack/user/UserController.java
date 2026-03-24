package org.blazejzj.coffeestack.user;

import jakarta.validation.Valid;
import org.blazejzj.coffeestack.user.dto.*;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("{id}")
    public UserResponse getUserById(@PathVariable UUID id) {
        return userService.getUserById(id);
    }

    @GetMapping("/me")
    public UserResponse getMe() {
        return userService.getMe();
    }

    @PatchMapping("/me/password")
    public ResponseEntity<MessageResponse> changePassword(@Valid @RequestBody PasswordChangeRequest req) {
        return ResponseEntity.ok(userService.changePassword(req));
    }

    @PatchMapping("/me/email")
    public ResponseEntity<UserResponse> changeUsername(@Valid @RequestBody UsernameChangeRequest req) {
        return ResponseEntity.ok(userService.changeUsername(req));
    }

    @PatchMapping("/me/username")
    public ResponseEntity<UserResponse> changeEmail(@Valid @RequestBody EmailChangeRequest req) {
        return ResponseEntity.ok(userService.changeEmail(req));
    }
}
