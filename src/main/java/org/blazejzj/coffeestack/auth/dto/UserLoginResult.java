package org.blazejzj.coffeestack.auth.dto;

import org.blazejzj.coffeestack.user.models.User;

public record UserLoginResult(
        User user,
        String token
) {
}
