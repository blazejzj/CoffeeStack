package org.blazejzj.coffeestack.lesson.dto;

public record LessonResponse(
        String slug,
        String title,
        String excerpt,
        int order,
        boolean completed
) {
}
