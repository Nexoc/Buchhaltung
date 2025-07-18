package at.magic.olga.dto.view;

import lombok.Data;
import java.time.LocalDate;

/**
 * DTO для отчёта о прибыли и убытках за день.
 */
@Data
public class DailyProfitLossDto {

    private Long id; // добавлено

    private LocalDate day;

    private Double salesAmount;

    private Double cardExpenses;

    private Double cashExpenses;

    private Double netProfit;

    private String description; // если есть в модели
}
