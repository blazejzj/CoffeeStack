package org.blazejzj.coffeestack.progress;

import org.blazejzj.coffeestack.progress.models.Progress;
import org.blazejzj.coffeestack.progress.models.ProgressId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProgressRepository extends JpaRepository<Progress, ProgressId> {
}
