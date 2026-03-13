package org.blazejzj.coffeestack.exception;

public class InvalidCredentialsException extends RuntimeException {
    public InvalidCredentialsException() {
        super("Invalid username/email and password");
    }
}
