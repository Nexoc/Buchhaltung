package at.magic.olga.models;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

/**
 * Entity representing a card expense (EC_von_TL).
 * Maps to the 'card_expenses' table in the database.
 */
@Entity
@Table(name = "card_expenses")
@Data
public class CardExpense {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "expense_time")
    private LocalDateTime expenseTime;

    private Double amount;
    private String description;
}

