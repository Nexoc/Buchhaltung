package at.magic.olga.mappers;

import at.magic.olga.dto.InstructionDto;
import at.magic.olga.models.Instruction;
import at.magic.olga.models.Product;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class InstructionMapper {

    public InstructionDto toDto(Instruction instr) {
        if (instr == null) {
            return null;
        }
        InstructionDto dto = new InstructionDto();
        dto.setId(instr.getId());
        dto.setFilename(instr.getFilename());
        dto.setDescription(instr.getDescription());
        Product p = instr.getProduct();
        dto.setProductId(p != null ? p.getId() : null);
        return dto;
    }

    public Instruction toEntity(InstructionDto dto) {
        if (dto == null) {
            return null;
        }
        Instruction instr = new Instruction();
        instr.setId(dto.getId());
        instr.setFilename(dto.getFilename());
        instr.setDescription(dto.getDescription());
        if (dto.getProductId() != null) {
            Product p = new Product();
            p.setId(dto.getProductId());
            instr.setProduct(p);
        }
        return instr;
    }

    public List<InstructionDto> toDtoList(List<Instruction> list) {
        return list == null ? null :
                list.stream().map(this::toDto).collect(Collectors.toList());
    }

    public List<Instruction> toEntityList(List<InstructionDto> list) {
        return list == null ? null :
                list.stream().map(this::toEntity).collect(Collectors.toList());
    }
}
