package at.magic.olga.repositories.view;

import at.magic.olga.models.view.AnnualSummary;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/** Repository for AnnualSummary view */
@Repository
public interface AnnualSummaryRepository extends JpaRepository<AnnualSummary, Long> {
}