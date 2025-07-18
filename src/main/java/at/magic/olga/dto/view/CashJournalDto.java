package at.magic.olga.dto.view;


import lombok.Data;

/**
 * DTO для CashJournal (ежемесячный отчёт) с описаниями для OpenAPI.
 */
@Data
public class CashJournalDto {

    private Integer id;
    private Integer monthNumber;
    private Integer year;
    private Double incomeTotal;
    private Double cardExpensesTotal;
    private Double cashExpensesTotal;
    private Double balanceChange;
    private Double runningTotal;
}
