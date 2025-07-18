package at.magic.olga.mappers.view;
import at.magic.olga.dto.view.WeeklyProfitLossDto;
import at.magic.olga.models.view.WeeklyProfitLoss;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Mapper for WeeklyProfitLoss view entity
 */
@Component
public class WeeklyProfitLossMapper {
    public WeeklyProfitLossDto toDto(WeeklyProfitLoss entity) {
        if (entity == null) {
            return null;
        }
        WeeklyProfitLossDto dto = new WeeklyProfitLossDto();
        dto.setId(entity.getId());
        dto.setWeekStart(entity.getWeekStart());
        dto.setSalesAmount(entity.getSalesAmount());
        dto.setCardExpenses(entity.getCardExpenses());
        dto.setCashExpenses(entity.getCashExpenses());
        dto.setNetProfit(entity.getNetProfit());
        return dto;
    }

    public List<WeeklyProfitLossDto> toDtoList(List<WeeklyProfitLoss> list) {
        return list == null ? null : list.stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }
}