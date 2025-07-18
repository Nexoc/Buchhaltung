package at.magic.olga.models.view;

import org.hibernate.annotations.Immutable;
import jakarta.persistence.*;
import lombok.Data;

/**
 * Read-only entity mapping to the database view 'cash_journal'.
 * Represents a monthly financial overview (year, month, income, card expenses, cash expenses, balance change).
 */
@Entity
@Immutable
@Table(name = "cash_journal")
@Data
public class YearCashJournal {
    @Id
    private Integer id;
    @Column(name = "month_number")
    private Integer monthNumber;  // 1 = January, …, 12 = December
    @Column(name = "year")
    private Integer year;
    @Column(name = "income_total")
    private Double incomeTotal;
    @Column(name = "card_expenses_total")
    private Double cardExpensesTotal;
    @Column(name = "cash_expenses_total")
    private Double cashExpensesTotal;
    @Column(name = "balance_change")
    private Double balanceChange;
    @Column(name = "running_total")
    private Double runningTotal;
}
