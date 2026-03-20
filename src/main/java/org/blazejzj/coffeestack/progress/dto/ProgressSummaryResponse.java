package org.blazejzj.coffeestack.progress.dto;

public record ProgressSummaryResponse(
        String module, // its going to be null for total progress
        int completedLessons,
        int totalLessons,
        double percentage
) {
}
