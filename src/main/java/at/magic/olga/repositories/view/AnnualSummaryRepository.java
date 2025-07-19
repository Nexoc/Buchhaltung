package at.magic.olga.repositories.view;

import at.magic.olga.models.view.AnnualSummary;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

/** Repository for AnnualSummary view */
@Repository
public interface AnnualSummaryRepository extends JpaRepository<AnnualSummary, Long> {
    /**
     * У вас, видимо, всего одна запись в этой таблице–представлении.
     * Этот метод просто вернёт её.
     */
    @Query("select s from AnnualSummary s")
    AnnualSummary findFirst();

    List<AnnualSummary> findAll();
}