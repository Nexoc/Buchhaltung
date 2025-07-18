package at.magic.olga.mappers;

import at.magic.olga.dto.ProductDto;
import at.magic.olga.models.Category;
import at.magic.olga.models.Instruction;
import at.magic.olga.models.Product;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class ProductMapper {

    private final InstructionMapper instructionMapper;

    public ProductMapper(InstructionMapper instructionMapper) {
        this.instructionMapper = instructionMapper;
    }

    /**
     * Converts Product entity to ProductDto.
     */
    public ProductDto toDto(Product product) {
        if (product == null) {
            return null;
        }

        ProductDto dto = new ProductDto();
        dto.setId(product.getId());
        dto.setName(product.getName());
        dto.setPrice(product.getPrice());
        dto.setStock(product.getStock());

        Category cat = product.getCategory();
        dto.setCategoryId(cat != null ? cat.getId() : null);
        dto.setAddedDate(product.getAddedDate());
        dto.setImagePath(product.getImagePath());
        dto.setDescription(product.getDescription());

        List<Instruction> instrList = product.getInstructions();
        dto.setInstructions(
                instrList != null
                        ? instrList.stream()
                        .map(instructionMapper::toDto)
                        .collect(Collectors.toList())
                        : Collections.emptyList()
        );

        return dto;
    }

    /**
     * Converts ProductDto to Product entity.
     */
    public Product toEntity(ProductDto dto) {
        if (dto == null) {
            return null;
        }

        Product product = new Product();
        product.setId(dto.getId());
        product.setName(dto.getName());
        product.setPrice(dto.getPrice());
        product.setStock(dto.getStock());

        if (dto.getCategoryId() != null) {
            Category cat = new Category();
            cat.setId(dto.getCategoryId());
            product.setCategory(cat);
        }

        product.setAddedDate(dto.getAddedDate());
        product.setImagePath(dto.getImagePath());
        product.setDescription(dto.getDescription());

        if (dto.getInstructions() != null) {
            List<Instruction> instr = dto.getInstructions().stream()
                    .map(instrDto -> {
                        Instruction i = new Instruction();
                        i.setId(instrDto.getId()); // только ID, чтобы не было detached entity
                        return i;
                    })
                    .collect(Collectors.toList());
            product.setInstructions(instr);
        }

        return product;
    }

    /**
     * Converts list of Product entities to DTOs.
     */
    public List<ProductDto> toDtoList(List<Product> list) {
        return list == null ? null :
                list.stream().map(this::toDto).collect(Collectors.toList());
    }

    /**
     * Converts list of ProductDto to entities.
     */
    public List<Product> toEntityList(List<ProductDto> list) {
        return list == null ? null :
                list.stream().map(this::toEntity).collect(Collectors.toList());
    }
}
