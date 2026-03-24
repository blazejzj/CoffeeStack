package org.blazejzj.coffeestack.exception;

public class EmailAlreadyExistsException extends RuntimeException {
    public EmailAlreadyExistsException(String message) {
        super("Email already taken:" + message);
    }
}
