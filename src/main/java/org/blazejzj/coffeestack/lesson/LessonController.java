package org.blazejzj.coffeestack.lesson;

import org.blazejzj.coffeestack.lesson.dto.LessonDetails;
import org.blazejzj.coffeestack.lesson.dto.LessonResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("api/v1/lessons")
public class LessonController {

    private final LessonService lessonService;

    public LessonController(LessonService lessonService) {
        this.lessonService = lessonService;
    }

    @GetMapping()
    public ResponseEntity<List<LessonResponse>> getAllLessons(@RequestParam(name = "module", required = false) String module) throws IOException {
        if (module != null) {
            return ResponseEntity.ok(lessonService.getAllLessonsByModule(module));
        }
        return ResponseEntity.ok(lessonService.getAllLessons());

    }

    @GetMapping("/by-slug")
    public ResponseEntity<LessonDetails> getLessonBySlug(@RequestParam("slug") String slug) {
        return ResponseEntity.ok(lessonService.getLessonBySlug(slug));
    }
}