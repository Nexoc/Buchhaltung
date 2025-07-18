package at.magic.olga.repositories.view;

import at.magic.olga.models.view.YearCashJournal;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Repository for CashJournal view entities.
 */
@Repository
public interface CashJournalRepository extends JpaRepository<YearCashJournal, Integer> {
    List<YearCashJournal> findByYear(Integer year);
    List<YearCashJournal> findByYearAndMonthNumber(Integer year, Integer monthNumber);
}
