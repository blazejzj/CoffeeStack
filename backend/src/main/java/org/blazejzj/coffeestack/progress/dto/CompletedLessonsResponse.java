package org.blazejzj.coffeestack.progress.dto;

import java.util.List;

public record CompletedLessonsResponse(
    List<String> slugs
    )
{}
