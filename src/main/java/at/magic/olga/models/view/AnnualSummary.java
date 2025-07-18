package at.magic.olga.models.view;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import org.hibernate.annotations.Immutable;

import java.math.BigDecimal;


/**
 * Aggregated summary across all years.
 */
@Entity
@Immutable
@Table(name = "annual_summary")
public class AnnualSummary {

    @Id
    private Long id; // You may map a synthetic ID or assign a constant

    @Column(name = "total_sales")
    private BigDecimal totalSales;

    @Column(name = "total_card_expenses")
    private BigDecimal totalCardExpenses;

    @Column(name = "total_cash_expenses")
    private BigDecimal totalCashExpenses;

    @Column(name = "total_net_profit")
    private BigDecimal totalNetProfit;

    // Getters (and setters if needed)
    public Long getId() {
        return id;
    }

    public BigDecimal getTotalSales() {
        return totalSales;
    }

    public BigDecimal getTotalCardExpenses() {
        return totalCardExpenses;
    }

    public BigDecimal getTotalCashExpenses() {
        return totalCashExpenses;
    }

    public BigDecimal getTotalNetProfit() {
        return totalNetProfit;
    }
}