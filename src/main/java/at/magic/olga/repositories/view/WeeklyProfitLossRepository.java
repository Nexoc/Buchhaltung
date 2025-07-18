package at.magic.olga.repositories.view;

import at.magic.olga.models.view.WeeklyProfitLoss;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/** Repository for WeeklyProfitLoss view */
@Repository
public interface WeeklyProfitLossRepository extends JpaRepository<WeeklyProfitLoss, Long> {
}