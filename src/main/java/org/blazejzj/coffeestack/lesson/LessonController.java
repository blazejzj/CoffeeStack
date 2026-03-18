package org.blazejzj.coffeestack.lesson;

import org.blazejzj.coffeestack.lesson.dto.LessonDetails;
import org.blazejzj.coffeestack.lesson.dto.LessonResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

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
    public ResponseEntity<List<LessonResponse>> getAllLessons(@RequestParam(name = "module", required = false) String type) throws IOException {
        if (type != null) {
            return ResponseEntity.ok(lessonService.getAllSlugsByModule(type));
        }
        return ResponseEntity.ok(lessonService.getAllSlugs());

    }

    @GetMapping("/by-slug")
    public ResponseEntity<LessonDetails> getLessonBySlug(@RequestParam("slug") String slug) throws IOException {
        return ResponseEntity.ok(lessonService.getLessonBySlug(slug));
    }
}
