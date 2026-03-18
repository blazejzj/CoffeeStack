package org.blazejzj.coffeestack.lesson.dto;

public record LessonDetails(
        LessonResponse information,
        String content,
        String module
) {

}
