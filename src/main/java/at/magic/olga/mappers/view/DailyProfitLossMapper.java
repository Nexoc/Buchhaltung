package at.magic.olga.mappers.view;

import at.magic.olga.dto.view.DailyProfitLossDto;
import at.magic.olga.models.view.DailyProfitLoss;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class DailyProfitLossMapper {

    public DailyProfitLossDto toDto(DailyProfitLoss entity) {
        if (entity == null) {
            return null;
        }
        DailyProfitLossDto dto = new DailyProfitLossDto();
        dto.setId(entity.getId()); // добавлено
        dto.setDay(entity.getDay());
        dto.setSalesAmount(entity.getSalesAmount());
        dto.setCardExpenses(entity.getCardExpenses());
        dto.setCashExpenses(entity.getCashExpenses());
        dto.setNetProfit(entity.getNetProfit());
        dto.setDescription(entity.getDescription()); // добавлено, если нужно
        return dto;
    }

    public DailyProfitLoss toEntity(DailyProfitLossDto dto) {
        if (dto == null) {
            return null;
        }
        DailyProfitLoss entity = new DailyProfitLoss();
        entity.setId(dto.getId()); // добавлено
        entity.setDay(dto.getDay());
        entity.setSalesAmount(dto.getSalesAmount());
        entity.setCardExpenses(dto.getCardExpenses());
        entity.setCashExpenses(dto.getCashExpenses());
        entity.setNetProfit(dto.getNetProfit());
        entity.setDescription(dto.getDescription()); // добавлено, если нужно
        return entity;
    }

    public List<DailyProfitLossDto> toDtoList(List<DailyProfitLoss> list) {
        return list == null ? null :
                list.stream().map(this::toDto).collect(Collectors.toList());
    }

    public List<DailyProfitLoss> toEntityList(List<DailyProfitLossDto> list) {
        return list == null ? null :
                list.stream().map(this::toEntity).collect(Collectors.toList());
    }

}
