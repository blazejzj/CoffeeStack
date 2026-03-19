package org.blazejzj.coffeestack.progress;

import jakarta.validation.Valid;
import org.blazejzj.coffeestack.progress.dto.LessonUpdateCompletionRequest;
import org.blazejzj.coffeestack.progress.dto.LessonUpdateCompletionResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
@RequestMapping("/api/v1/progress")
public class ProgressController {

    private final ProgressService progressService;

    public ProgressController(ProgressService progressService) {
        this.progressService = progressService;
    }

    @PostMapping("/complete")
    public ResponseEntity<LessonUpdateCompletionResponse> updateLessonCompletion(@Valid @RequestBody LessonUpdateCompletionRequest req) throws IOException {
        // result is either "deleted" or "added" dependeing if instance was found or not

        if (!progressService.updateLessonCompletion(req)) {
            return ResponseEntity.ok(new LessonUpdateCompletionResponse("Lesson successfully unfinished"));
        }
        return ResponseEntity.ok(new LessonUpdateCompletionResponse("Lesson successfully finished"));
    }

}
