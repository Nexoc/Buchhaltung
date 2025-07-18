package at.magic.olga.mappers.view;

import at.magic.olga.dto.view.AnnualSummaryDto;
import at.magic.olga.models.view.AnnualSummary;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Mapper for AnnualSummary view entity
 */
@Component
public class AnnualSummaryMapper {
    public AnnualSummaryDto toDto(AnnualSummary entity) {
        if (entity == null) {
            return null;
        }
        AnnualSummaryDto dto = new AnnualSummaryDto();
        dto.setTotalSales(entity.getTotalSales());
        dto.setTotalCardExpenses(entity.getTotalCardExpenses());
        dto.setTotalCashExpenses(entity.getTotalCashExpenses());
        dto.setTotalNetProfit(entity.getTotalNetProfit());
        return dto;
    }

    public List<AnnualSummaryDto> toDtoList(List<AnnualSummary> list) {
        return list == null ? null : list.stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }
}