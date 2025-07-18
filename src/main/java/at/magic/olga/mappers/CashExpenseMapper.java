package at.magic.olga.mappers;

import at.magic.olga.dto.CashExpenseDto;
import at.magic.olga.models.CashExpense;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class CashExpenseMapper {

    public CashExpenseDto toDto(CashExpense expense) {
        if (expense == null) {
            return null;
        }
        CashExpenseDto dto = new CashExpenseDto();
        dto.setId(expense.getId());
        dto.setExpenseTime(expense.getExpenseTime());
        dto.setAmount(expense.getAmount());
        dto.setDescription(expense.getDescription());
        return dto;
    }

    public CashExpense toEntity(CashExpenseDto dto) {
        if (dto == null) {
            return null;
        }
        CashExpense expense = new CashExpense();
        expense.setId(dto.getId()); // важно для update
        expense.setExpenseTime(dto.getExpenseTime());
        expense.setAmount(dto.getAmount());
        expense.setDescription(dto.getDescription());
        return expense;
    }

    public List<CashExpenseDto> toDtoList(List<CashExpense> list) {
        return list == null ? null :
                list.stream().map(this::toDto).collect(Collectors.toList());
    }

    public List<CashExpense> toEntityList(List<CashExpenseDto> list) {
        return list == null ? null :
                list.stream().map(this::toEntity).collect(Collectors.toList());
    }
}
