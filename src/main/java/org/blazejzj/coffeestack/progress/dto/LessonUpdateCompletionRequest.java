package org.blazejzj.coffeestack.progress.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.hibernate.validator.constraints.Length;

public record LessonUpdateCompletionRequest(
        @NotBlank @NotNull @Length(max = 50)
        String slug
) {
}
