package org.blazejzj.coffeestack.user;

import org.blazejzj.coffeestack.user.dto.PasswordChangeRequest;
import org.blazejzj.coffeestack.user.dto.UserInfoChangeResponse;
import org.blazejzj.coffeestack.user.dto.UserResponse;
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

    // TODO: Change password endpoint (Remember updated at)
    @PatchMapping("/me/password")
    public UserInfoChangeResponse changePassword(@Valid @RequestBody PasswordChangeRequest req) {
        return userService.changePassword(req);
    }

    // TODO: Change email endpoint (Remember updated at)
    // TODO: Change username endpoint (Remember updated at)

}
