package org.blazejzj.coffeestack.progress;

import jakarta.validation.Valid;
import org.blazejzj.coffeestack.lesson.dto.ResumeLessonResponse;
import org.blazejzj.coffeestack.progress.dto.*;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@RequestMapping("/api/v1/progress")
public class ProgressController {

    private final ProgressService progressService;

    public ProgressController(ProgressService progressService) {
        this.progressService = progressService;
    }

    @GetMapping()
    public ResponseEntity<CompletedLessonsResponse> getCompletedLessons() {
        return ResponseEntity.ok().body(progressService.getCompletedLessons());
    }

    @GetMapping("/check")
    public ResponseEntity<LessonIsCompletedResponse> isLessonCompleted(@RequestParam(name = "slug") String slug) {
        return ResponseEntity.ok().body(new LessonIsCompletedResponse(progressService.isLessonCompleted(slug)));
    }

    @GetMapping("/summary")
    public ResponseEntity<ProgressSummaryResponse> getProgressSummary(@RequestParam(name = "module", required = false) String module) throws IOException {
        if (module == null) {
            return ResponseEntity.ok().body(progressService.getTotalProgressSummary());
        }
        return ResponseEntity.ok().body(progressService.getModuleProgressSummary(module));
    }

    @PostMapping("/complete")
    public ResponseEntity<LessonUpdateCompletionResponse> updateLessonCompletion(@Valid @RequestBody LessonUpdateCompletionRequest req) throws IOException {
        // result is either "deleted" or "added" dependeing if instance was found or not

        if (!progressService.updateLessonCompletion(req)) {
            return ResponseEntity.ok(new LessonUpdateCompletionResponse("Lesson successfully unfinished"));
        }
        return ResponseEntity.ok(new LessonUpdateCompletionResponse("Lesson successfully finished"));
    }

    @GetMapping("/resume")
    public ResponseEntity<ResumeLessonResponse> getResumeLesson() throws IOException {
        return ResponseEntity.ok().body(progressService.getResumeLesson());
    }
}
