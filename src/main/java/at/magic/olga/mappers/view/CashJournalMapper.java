package at.magic.olga.mappers.view;

import at.magic.olga.dto.view.CashJournalDto;
import at.magic.olga.models.view.YearCashJournal;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class CashJournalMapper {

    public CashJournalDto toDto(YearCashJournal journal) {
        if (journal == null) {
            return null;
        }
        CashJournalDto dto = new CashJournalDto();
        dto.setId(journal.getId());
        dto.setMonthNumber(journal.getMonthNumber());
        dto.setYear(journal.getYear());
        dto.setIncomeTotal(journal.getIncomeTotal());
        dto.setCardExpensesTotal(journal.getCardExpensesTotal());
        dto.setCashExpensesTotal(journal.getCashExpensesTotal());
        dto.setBalanceChange(journal.getBalanceChange());
        dto.setRunningTotal(journal.getRunningTotal());
        return dto;
    }

    public YearCashJournal toEntity(CashJournalDto dto) {
        if (dto == null) {
            return null;
        }
        YearCashJournal journal = new YearCashJournal();
        journal.setId(dto.getId());
        journal.setMonthNumber(dto.getMonthNumber());
        journal.setYear(dto.getYear());
        journal.setIncomeTotal(dto.getIncomeTotal());
        journal.setCardExpensesTotal(dto.getCardExpensesTotal());
        journal.setCashExpensesTotal(dto.getCashExpensesTotal());
        journal.setBalanceChange(dto.getBalanceChange());
        journal.setRunningTotal(dto.getRunningTotal());
        return journal;
    }

    public List<CashJournalDto> toDtoList(List<YearCashJournal> list) {
        return list == null ? null :
                list.stream().map(this::toDto).collect(Collectors.toList());
    }

    public List<YearCashJournal> toEntityList(List<CashJournalDto> list) {
        return list == null ? null :
                list.stream().map(this::toEntity).collect(Collectors.toList());
    }
}
