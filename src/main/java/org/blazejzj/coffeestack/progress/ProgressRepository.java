package org.blazejzj.coffeestack.progress;

import org.blazejzj.coffeestack.progress.models.Progress;
import org.blazejzj.coffeestack.progress.models.ProgressId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface ProgressRepository extends JpaRepository<Progress, ProgressId> {
    List<Progress> findAllByProgressIdUserId(UUID userId);
    long countByProgressIdUserIdAndProgressIdLessonSlugStartingWith(UUID userId, String prefix);
}
