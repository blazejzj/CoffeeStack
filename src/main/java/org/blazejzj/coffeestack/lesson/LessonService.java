package org.blazejzj.coffeestack.lesson;

import org.blazejzj.coffeestack.exception.LessonNotFoundException;
import org.blazejzj.coffeestack.lesson.dto.LessonDetails;
import org.blazejzj.coffeestack.lesson.dto.LessonResponse;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Service
public class LessonService {

    private static final PathMatchingResourcePatternResolver resolver =
            new PathMatchingResourcePatternResolver();

    public List<LessonResponse> getAllLessons() throws IOException {
        // Return slug, title, excerpt, order

        Resource[] resources = resolver.getResources("classpath:content/**/*.md");
        List<String> slugs = extractSlugs(resources);

        List<LessonResponse> response = new ArrayList<>();
        for (String slug : slugs) {
            response.add(buildLessonResponse(slug));
        }

        response.sort(Comparator.comparingInt(LessonResponse::order));
        return response;
    }

    public LessonDetails getLessonBySlug(String slug) {
        // return slug, title, excerpt, order, module, content
        slug = normalizeSlug(slug);
        String lessonRaw = getAllLessonContent(slug);

        String frontmatter = extractFrontmatter(lessonRaw);
        String content = extractContent(lessonRaw);

        Map<String, String> values = extractFrontmatterValues(frontmatter);
        LessonResponse information = new LessonResponse(
                slug,
                values.get("title"),
                values.get("excerpt"),
                Integer.parseInt(values.get("order")),
                false // by default false, going to be modified in the controller
        );

        String module = slug.split("/")[0];

        return new LessonDetails(information, content, module);
    }

    public List<LessonResponse> getAllLessonsByModule(String module) throws IOException {
        // Return slug, title, excerpt, order for a specific module

        module = normalizeSlug(module);

        Resource[] resources = resolver.getResources("classpath:content/**/*.md");
        List<String> slugs = extractSlugs(resources);

        List<LessonResponse> response = new ArrayList<>();
        for (String slug : slugs) {
            if (slug.startsWith(module + "/")) {
                response.add(buildLessonResponse(slug));
            }
        }

        response.sort(Comparator.comparingInt(LessonResponse::order));
        return response;
    }

    public boolean slugExists(String slug) throws IOException {
        Resource[] resources = resolver.getResources("classpath:content/**/*.md");
        List<String> allSlugs = extractSlugs(resources);

        for (String s : allSlugs) {
            if (s.equalsIgnoreCase(slug)) return true;
        }
        return false;
    }

    private List<String> extractSlugs(Resource[] resources) throws IOException {
        List<String> slugs = new ArrayList<>();
        for (Resource resource : resources) {
            String path = resource.getURI().getPath();
            int index = path.indexOf("/content/");

            if (index != -1) {
                String slug = path.substring(index + "/content/".length());
                slug = slug.substring(0, slug.length() - 3);
                slugs.add(slug);
            }
        }
        return slugs;
    }

    private LessonResponse buildLessonResponse(String slug) {
        String lessonRaw = getAllLessonContent(slug);
        String frontmatter = extractFrontmatter(lessonRaw);

        Map<String, String> values = extractFrontmatterValues(frontmatter);

        // unsure on how else I could do it, but here I am hardcoding those values.
        // We assume these 3 values will ALWAYS be in the same order
        return new LessonResponse(
                slug,
                values.get("title"),
                values.get("excerpt"),
                Integer.parseInt(values.get("order")),
                false // by default false, going to be "modified" in the controller
        );
    }

    private Map<String, String> extractFrontmatterValues(String frontmatter) {
        // i = 0 -> title
        // i =  1 -> excerpt
        // i = 2 -> order
        String[] lines = frontmatter.split("\n");
        Map<String, String> values = new LinkedHashMap<>();

        for (String line : lines) {
            if (!line.contains(":")) {
                continue;
            }

            String[] parts = line.split(":", 2);
            values.put(parts[0].trim(), parts[1].trim());
        }

        validateFrontmatter(values);
        return values;
    }

    private void validateFrontmatter(Map<String, String> values) {
        if (!values.containsKey("title")) {
            throw new IllegalArgumentException("Missing required frontmatter field: title");
        }

        if (!values.containsKey("excerpt")) {
            throw new IllegalArgumentException("Missing required frontmatter field: excerpt");
        }

        if (!values.containsKey("order")) {
            throw new IllegalArgumentException("Missing required frontmatter field: order");
        }
    }

    private String extractFrontmatter(String lessonRaw) {
        String[] parts = lessonRaw.split("---", 3);

        if (parts.length < 3) {
            throw new IllegalArgumentException("Lesson file must contain frontmatter");
        }

        return parts[1];
    }

    private String extractContent(String lessonRaw) {
        String[] parts = lessonRaw.split("---", 3);

        if (parts.length < 3) {
            throw new IllegalArgumentException("Lesson file must contain frontmatter");
        }

        return parts[2].trim();
    }

    private static String getAllLessonContent(String slug) {
        slug = normalizeSlug(slug);
        String resourcePath = "classpath:content/" + slug + ".md";

        try {
            Resource resource = resolver.getResource(resourcePath);

            if (!resource.exists()) {
                throw new LessonNotFoundException("Lesson not found: " + slug);
            }

            try (InputStream stream = resource.getInputStream()) {
                return new String(stream.readAllBytes(), StandardCharsets.UTF_8);
            }
        } catch (IOException e) {
            throw new RuntimeException("Failed to read the lesson: " + slug, e);
        }
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
}