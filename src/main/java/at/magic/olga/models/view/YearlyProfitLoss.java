package at.magic.olga.models.view;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import org.hibernate.annotations.Immutable;

import java.math.BigDecimal;

/**
 * Yearly profit and loss overview.
 */
@Entity
@Immutable
@Table(name = "yearly_pl")
public class YearlyProfitLoss {

    @Id
    private Integer year;

    @Column(name = "sales_amount")
    private BigDecimal salesAmount;

    @Column(name = "card_expenses")
    private BigDecimal cardExpenses;

    @Column(name = "cash_expenses")
    private BigDecimal cashExpenses;

    @Column(name = "net_profit")
    private BigDecimal netProfit;

    // Getters
    public Integer getYear() {
        return year;
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
