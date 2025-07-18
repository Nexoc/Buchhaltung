package at.magic.olga.repositories;

import at.magic.olga.models.CashExpense;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;

/**
 * Repository for CashExpense entities.
 */
@Repository
public interface CashExpenseRepository extends JpaRepository<CashExpense, Long> {
}
