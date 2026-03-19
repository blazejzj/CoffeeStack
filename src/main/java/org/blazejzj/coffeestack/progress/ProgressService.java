package org.blazejzj.coffeestack.progress;

import org.blazejzj.coffeestack.exception.LessonNotFoundException;
import org.blazejzj.coffeestack.exception.UnauthorizedException;
import org.blazejzj.coffeestack.lesson.LessonService;
import org.blazejzj.coffeestack.progress.dto.LessonUpdateCompletionRequest;
import org.blazejzj.coffeestack.progress.models.Progress;
import org.blazejzj.coffeestack.progress.models.ProgressId;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.UUID;

@Service
public class ProgressService {

    private final ProgressRepository progressRepository;
    private final LessonService lessonService;

    public ProgressService(ProgressRepository progressRepository, LessonService lessonService) {
        this.progressRepository = progressRepository;
        this.lessonService = lessonService;
    }
    
    public boolean updateLessonCompletion(LessonUpdateCompletionRequest req) throws IOException {
        UUID userId = getUserPrincipal();
        String slug = normalizeSlug(req.slug());
        if (!lessonService.slugExists(slug)) {
            throw new LessonNotFoundException("Lesson not found: " + slug);
        }

        ProgressId instanceId = new ProgressId(slug, userId);

        // if entry found we delete it (unfinish the lesson)
        if (progressRepository.existsById(instanceId)) {
            progressRepository.deleteById(instanceId);
            return false;
        }

        // if entry not found we add it, meaning the lesson has been finished
        progressRepository.save(new Progress(instanceId, LocalDateTime.now()));
        return true;
    }

    private static String normalizeSlug(String slug) {
        if (slug == null || slug.isBlank()) {
            throw new IllegalArgumentException("Slug cannot be blank");
        }

        slug = slug.trim();

        if (slug.startsWith("/")) {
            slug = slug.substring(1);
        }

        if (slug.endsWith(".md")) {
            slug = slug.substring(0, slug.length() - 3);
        }

        return slug;
    }

    private static UUID getUserPrincipal() {
        Authentication authentication = SecurityContextHolder
                .getContext()
                .getAuthentication();

        if (authentication == null || authentication.getPrincipal() == null) {
            throw new UnauthorizedException();
        }

        return (UUID) authentication.getPrincipal();
    }
}
