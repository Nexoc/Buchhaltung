package at.magic.olga.dto.view;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class AnnualSummaryDto {
    private Long id;
    private BigDecimal totalSales;
    private BigDecimal totalCardExpenses;
    private BigDecimal totalCashExpenses;
    private BigDecimal totalNetProfit;

}