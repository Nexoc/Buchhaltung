package at.magic.olga.controllers;

import at.magic.olga.dto.CategoryDto;
import at.magic.olga.mappers.CategoryMapper;
import at.magic.olga.models.Category;
import at.magic.olga.service.CategoryService;

import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

/**
 * REST controller for managing categories.
 */
@RestController
@RequestMapping("/api/categories")
@Validated
public class CategoryController {

    private final CategoryService categoryService;
    private final CategoryMapper categoryMapper;

    public CategoryController(CategoryService categoryService,
                              CategoryMapper categoryMapper) {
        this.categoryService = categoryService;
        this.categoryMapper = categoryMapper;
    }

    @GetMapping
    public List<CategoryDto> listAll() {
        List<Category> list = categoryService.findAll();
        return categoryMapper.toDtoList(list);
    }

    @GetMapping("/{id}")
    public ResponseEntity<CategoryDto> getOne(@PathVariable Integer id) {
        Category entity = categoryService.findById(id);
        if (entity == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(categoryMapper.toDto(entity));
    }


    @PostMapping
    public ResponseEntity<CategoryDto> create(
            @RequestBody @Valid CategoryDto dto) {
        Category saved = categoryService.create(categoryMapper.toEntity(dto));
        CategoryDto out = categoryMapper.toDto(saved);
        URI location = URI.create("/api/categories/" + out.getId());
        return ResponseEntity
                .created(location)
                .body(out);
    }

    @PutMapping("/{id}")
    public ResponseEntity<CategoryDto> update(
            @PathVariable Integer id,
            @RequestBody @Valid CategoryDto dto) {

        Category updated = categoryService.update(id, categoryMapper.toEntity(dto));
        return ResponseEntity.ok(categoryMapper.toDto(updated));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Integer id) {
        categoryService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
