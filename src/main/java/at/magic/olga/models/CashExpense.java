package at.magic.olga.models;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

/**
 * Entity representing a cash expense (Kassabewegungen).
 * Maps to the 'cash_expenses' table in the database.
 */
@Entity
@Table(name = "cash_expenses")
@Data
public class CashExpense {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "expense_time", nullable = false)
    private LocalDateTime expenseTime;

    private Double amount;

    private String description;
}

