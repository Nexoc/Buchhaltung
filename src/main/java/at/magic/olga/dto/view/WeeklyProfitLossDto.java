package at.magic.olga.dto.view;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
public class WeeklyProfitLossDto {
    private Long id;
    private LocalDate weekStart;
    private BigDecimal salesAmount;
    private BigDecimal cardExpenses;
    private BigDecimal cashExpenses;
    private BigDecimal netProfit;
    // getters and setters
}
