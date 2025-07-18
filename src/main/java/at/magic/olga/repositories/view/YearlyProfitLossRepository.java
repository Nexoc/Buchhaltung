package at.magic.olga.repositories.view;


import at.magic.olga.models.view.YearlyProfitLoss;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/** Repository for YearlyProfitLoss view */
@Repository
public interface YearlyProfitLossRepository extends JpaRepository<YearlyProfitLoss, Integer> {
}