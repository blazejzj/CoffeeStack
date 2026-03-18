package org.blazejzj.coffeestack.exception;

public class LessonNotFoundException extends RuntimeException {
    public LessonNotFoundException(String message) {
        super(message);
    }
}
