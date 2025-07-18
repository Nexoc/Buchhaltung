package at.magic.olga.mappers.view;

import at.magic.olga.dto.view.YearlyProfitLossDto;
import at.magic.olga.models.view.YearlyProfitLoss;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Mapper for YearlyProfitLoss view entity
 */
@Component
public class YearlyProfitLossMapper {
    public YearlyProfitLossDto toDto(YearlyProfitLoss entity) {
        if (entity == null) {
            return null;
        }
        YearlyProfitLossDto dto = new YearlyProfitLossDto();
        dto.setYear(entity.getYear());
        dto.setSalesAmount(entity.getSalesAmount());
        dto.setCardExpenses(entity.getCardExpenses());
        dto.setCashExpenses(entity.getCashExpenses());
        dto.setNetProfit(entity.getNetProfit());
        return dto;
    }

    public List<YearlyProfitLossDto> toDtoList(List<YearlyProfitLoss> list) {
        return list == null ? null : list.stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }
}