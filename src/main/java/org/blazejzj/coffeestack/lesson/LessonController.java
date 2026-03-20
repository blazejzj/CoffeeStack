package org.blazejzj.coffeestack.lesson;

import org.blazejzj.coffeestack.lesson.dto.LessonDetails;
import org.blazejzj.coffeestack.lesson.dto.LessonResponse;
import org.blazejzj.coffeestack.progress.ProgressService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("api/v1/lessons")
public class LessonController {

    private final LessonService lessonService;
    private final ProgressService progressService;

    public LessonController(LessonService lessonService, ProgressService progressService) {
        this.lessonService = lessonService;
        this.progressService = progressService;
    }

    @GetMapping()
    public ResponseEntity<List<LessonResponse>> getAllLessons(@RequestParam(name = "module", required = false) String module) throws IOException {
        List<LessonResponse> lessons;
        if (module != null) {
            lessons = lessonService.getAllLessonsByModule(module);
        }
        else {
            lessons = lessonService.getAllLessons();
        }

        // get all completed slugs only once
        List<String> completed = progressService.getCompletedLessons().slugs();
        // probably optional but going to convert to set for faster lookup
        Set<String> completedSet = new HashSet<>(completed);

        // and merge
        List<LessonResponse> result = lessons.stream().map((lesson) -> new LessonResponse(lesson.slug(), lesson.title(), lesson.excerpt(), lesson.order(), completedSet.contains(lesson.slug()))).toList();

        return ResponseEntity.ok().body(result);
    }

    @GetMapping("/by-slug")
    public ResponseEntity<LessonDetails> getLessonBySlug(@RequestParam("slug") String slug)  {
        LessonDetails lesson = lessonService.getLessonBySlug(slug);

        List<String> completed = progressService.getCompletedLessons().slugs();
        Set<String> completedSet = new HashSet<>(completed);

        LessonResponse information = lesson.information();
        LessonResponse updatedInformation = new LessonResponse(
                information.slug(),
                information.title(),
                information.excerpt(),
                information.order(),
                completedSet.contains(information.slug())
        );

        LessonResponse previousLesson = lesson.previousLesson() == null
                ? null
                : new LessonResponse(
                lesson.previousLesson().slug(),
                lesson.previousLesson().title(),
                lesson.previousLesson().excerpt(),
                lesson.previousLesson().order(),
                completedSet.contains(lesson.previousLesson().slug())
        );

        LessonResponse nextLesson = lesson.nextLesson() == null
                ? null
                : new LessonResponse(
                lesson.nextLesson().slug(),
                lesson.nextLesson().title(),
                lesson.nextLesson().excerpt(),
                lesson.nextLesson().order(),
                completedSet.contains(lesson.nextLesson().slug())
        );

        return ResponseEntity.ok().body(new LessonDetails(
                updatedInformation,
                lesson.content(),
                lesson.module(),
                previousLesson,
                nextLesson
        ));
    }
}