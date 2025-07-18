package at.magic.olga.models.view;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;

import jakarta.persistence.Table;
import org.hibernate.annotations.Immutable;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * Weekly profit and loss overview.
 */
@Entity
@Immutable
@Table(name = "weekly_pl")
public class WeeklyProfitLoss {

    @Id
    private Long id; // ROW_NUMBER generated in view

    @Column(name = "week_start")
    private LocalDate weekStart;

    @Column(name = "sales_amount")
    private BigDecimal salesAmount;

    @Column(name = "card_expenses")
    private BigDecimal cardExpenses;

    @Column(name = "cash_expenses")
    private BigDecimal cashExpenses;

    @Column(name = "net_profit")
    private BigDecimal netProfit;

    // Getters
    public Long getId() {
        return id;
    }

    public LocalDate getWeekStart() {
        return weekStart;
    }

    public BigDecimal getSalesAmount() {
        return salesAmount;
    }

    public BigDecimal getCardExpenses() {
        return cardExpenses;
    }

    public BigDecimal getCashExpenses() {
        return cashExpenses;
    }

    public BigDecimal getNetProfit() {
        return netProfit;
    }
}
