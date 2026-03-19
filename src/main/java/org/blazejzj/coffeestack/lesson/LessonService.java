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
import java.util.Arrays;
import java.util.List;

@Service
public class LessonService {

    private static final PathMatchingResourcePatternResolver resolver =
            new PathMatchingResourcePatternResolver();

    public List<LessonResponse> getAllSlugs() throws IOException {
        // Return slug, title, excerpt, order

        Resource[] resources = resolver.getResources("classpath:content/**/*.md");
        List<String> slugs = extractSlugs(resources);

        List<LessonResponse> response = new ArrayList<>();
        for (String slug : slugs) {
            response.add(buildLessonResponse(slug));
        }

        return response;
    }

    public LessonDetails getLessonBySlug(String slug) throws IOException {
        // return slug, title, excerpt, order, module, content
        String lessonRaw = getAllLessonContent(slug);

        String frontmatter = lessonRaw.split("---", 3)[1];
        String content = lessonRaw.split("---", 3)[2];

        String[] values = extractFrontmatterValues(frontmatter);
        LessonResponse information = new LessonResponse(slug, values[0], values[1], Integer.parseInt(values[2]));

        String module = slug.split("/")[0];

        return new LessonDetails(information, content, module);
    }

    public List<LessonResponse> getAllSlugsByModule(String module) throws IOException {
        // Return slug, title, excerpt, order for a specific module

        Resource[] resources = resolver.getResources("classpath:content/" + module + "/*.md");
        List<String> slugs = extractSlugs(resources);

        List<LessonResponse> response = new ArrayList<>();
        for (String slug : slugs) {
            response.add(buildLessonResponse(slug));
        }

        return response;
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
        String frontmatter = getAllLessonContent(slug).split("---", 3)[1];

        String[] values = extractFrontmatterValues(frontmatter);

        // unsure on how else I could do it, but here I am hardcoding those values.
        // We assume these 3 values will ALWAYS be in the same order
        return new LessonResponse(slug, values[0], values[1], Integer.parseInt(values[2]));
    }

    private String[] extractFrontmatterValues(String frontmatter) {
        // i = 0 -> title
        // i =  1 -> excerpt
        // i = 2 -> order
        String[] lines = frontmatter.split("\n");

        return Arrays.stream(lines)
                .filter(line -> line.contains(":"))
                .map(line -> line.split(":", 2)[1].trim())
                .toArray(String[]::new);
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