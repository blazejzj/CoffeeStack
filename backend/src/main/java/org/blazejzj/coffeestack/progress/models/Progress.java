package org.blazejzj.coffeestack.progress.models;

import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;


@Entity(name = "progress")
@Getter @Setter
public class Progress {
    @EmbeddedId
    ProgressId progressId;

    @NotNull
    LocalDateTime completedAt;

    public Progress(ProgressId progressId, LocalDateTime completedAt) {
        this.progressId = progressId;
        this.completedAt = completedAt;
    }

    public Progress(){}
}
