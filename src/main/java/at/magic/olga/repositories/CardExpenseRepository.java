package at.magic.olga.repositories;

import at.magic.olga.models.CardExpense;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;

/**
 * Repository for CardExpense entities.
 */
@Repository
public interface CardExpenseRepository extends JpaRepository<CardExpense, Long> {
}
