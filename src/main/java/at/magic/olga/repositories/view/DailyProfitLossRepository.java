package at.magic.olga.repositories.view;

import at.magic.olga.models.view.DailyProfitLoss;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Repository for DailyProfitLoss view entities.
 */
@Repository
public interface DailyProfitLossRepository extends JpaRepository<DailyProfitLoss, Long> {
}
