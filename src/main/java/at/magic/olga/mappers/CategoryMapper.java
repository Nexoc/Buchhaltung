package at.magic.olga.mappers;

import at.magic.olga.dto.CategoryDto;
import at.magic.olga.models.Category;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class CategoryMapper {

    public CategoryDto toDto(Category category) {
        if (category == null) {
            return null;
        }
        CategoryDto dto = new CategoryDto();
        dto.setId(category.getId());
        dto.setName(category.getName());
        dto.setDescription(category.getDescription());
        return dto;
    }

    public Category toEntity(CategoryDto dto) {
        if (dto == null) {
            return null;
        }
        Category category = new Category();
        category.setId(dto.getId());
        category.setName(dto.getName());
        category.setDescription(dto.getDescription());
        return category;
    }

    public List<CategoryDto> toDtoList(List<Category> list) {
        return list == null ? null :
                list.stream().map(this::toDto).collect(Collectors.toList());
    }

    public List<Category> toEntityList(List<CategoryDto> list) {
        return list == null ? null :
                list.stream().map(this::toEntity).collect(Collectors.toList());
    }
}
