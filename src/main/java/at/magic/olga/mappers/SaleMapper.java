package at.magic.olga.mappers;

import at.magic.olga.dto.SaleDto;
import at.magic.olga.models.Product;
import at.magic.olga.models.Sale;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class SaleMapper {

    public SaleDto toDto(Sale sale) {
        if (sale == null) {
            return null;
        }
        SaleDto dto = new SaleDto();
        dto.setId(sale.getId());
        dto.setProductId(sale.getProduct() != null ? sale.getProduct().getId() : null);
        dto.setSaleTime(sale.getSaleTime());
        dto.setQuantity(sale.getQuantity());
        dto.setPaymentMethod(sale.getPaymentMethod());
        dto.setComment(sale.getComment());
        return dto;
    }

    public Sale toEntity(SaleDto dto) {
        if (dto == null) {
            return null;
        }
        Sale sale = new Sale();

        if (dto.getProductId() != null) {
            Product p = new Product();
            p.setId(dto.getProductId());
            sale.setProduct(p);
        }

        sale.setSaleTime(dto.getSaleTime()); // ← ОБЯЗАТЕЛЬНО добавить эту строку
        sale.setQuantity(dto.getQuantity());
        sale.setPaymentMethod(dto.getPaymentMethod());
        sale.setComment(dto.getComment());

        return sale;
    }


    public List<SaleDto> toDtoList(List<Sale> list) {
        return list == null ? null :
                list.stream().map(this::toDto).collect(Collectors.toList());
    }

    public List<Sale> toEntityList(List<SaleDto> list) {
        return list == null ? null :
                list.stream().map(this::toEntity).collect(Collectors.toList());
    }
}
