package at.magic.olga.dto.view;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class YearlyProfitLossDto {
    private Integer year;
    private BigDecimal salesAmount;
    private BigDecimal cardExpenses;
    private BigDecimal cashExpenses;
    private BigDecimal netProfit;
    // getters and setters
}
