package org.blazejzj.coffeestack.progress.models;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.UUID;

@Embeddable
@Getter @Setter
public class ProgressId implements Serializable {
    @Column(name = "lesson_slug")
    String lessonSlug;

    @Column(name = "user_id")
    UUID userId;

    public ProgressId(String lessonSlug, UUID userId) {
        this.lessonSlug = lessonSlug;
        this.userId = userId;
    }

    public ProgressId() {}
}
