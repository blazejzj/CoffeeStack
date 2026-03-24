package org.blazejzj.coffeestack.exception;

public class UsernameAlreadyExistsException extends RuntimeException {
    public UsernameAlreadyExistsException(String message) {
        super("Username already taken: " + message);
    }
}
