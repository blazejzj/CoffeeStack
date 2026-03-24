package org.blazejzj.coffeestack.exception;

import java.util.List;

public record ApiError(
    String message,
    List<String> details
) { }
