package at.magic.olga.mappers;

import at.magic.olga.dto.CardExpenseDto;
import at.magic.olga.models.CardExpense;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class CardExpenseMapper {

    public CardExpenseDto toDto(CardExpense expense) {
        if (expense == null) return null;
        CardExpenseDto dto = new CardExpenseDto();
        dto.setId(expense.getId());
        dto.setExpenseTime(expense.getExpenseTime());
        dto.setAmount(expense.getAmount());
        dto.setDescription(expense.getDescription());
        return dto;
    }

    public CardExpense toEntity(CardExpenseDto dto) {
        if (dto == null) return null;
        CardExpense expense = new CardExpense();
        expense.setId(dto.getId());
        expense.setExpenseTime(dto.getExpenseTime());
        expense.setAmount(dto.getAmount());
        expense.setDescription(dto.getDescription());
        return expense;
    }

    public List<CardExpenseDto> toDtoList(List<CardExpense> list) {
        return list == null ? null :
                list.stream().map(this::toDto).collect(Collectors.toList());
    }

    public List<CardExpense> toEntityList(List<CardExpenseDto> list) {
        return list == null ? null :
                list.stream().map(this::toEntity).collect(Collectors.toList());
    }
}

