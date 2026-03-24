package org.blazejzj.coffeestack.exception;

import java.util.UUID;

public class UserNotFoundException extends RuntimeException {
    public UserNotFoundException(UUID uuid) {
        super("User not found: " + uuid.toString());
    }
}
