package at.magic.olga.controllers;

import at.magic.olga.dto.CategoryDto;
import at.magic.olga.mappers.CategoryMapper;
import at.magic.olga.models.Category;
import at.magic.olga.repositories.ProductRepository;
import at.magic.olga.service.CategoryService;

import jakarta.validation.Valid;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
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
    private final ProductRepository productRepository;

    public CategoryController(CategoryService categoryService,
                              CategoryMapper categoryMapper,
                              ProductRepository productRepository) {
        this.categoryService = categoryService;
        this.categoryMapper = categoryMapper;
        this.productRepository = productRepository;
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


    @PostMapping(value = "/with-image", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<CategoryDto> createWithImage(
            @RequestParam("name") String name,
            @RequestParam("description") String description,
            @RequestParam(value = "image", required = false) MultipartFile image
    ) throws IOException {
        Category category = categoryService.createWithImage(name, description, image);
        return ResponseEntity.created(URI.create("/api/categories/" + category.getId()))
                .body(categoryMapper.toDto(category));
    }



    @PutMapping("/{id}")
    public ResponseEntity<CategoryDto> update(
            @PathVariable Integer id,
            @RequestBody @Valid CategoryDto dto) {

        // Преобразуем DTO в сущность
        Category entity = categoryMapper.toEntity(dto);

        // Обновляем через сервис
        Category updated = categoryService.update(id, entity);

        // Возвращаем обратно DTO
        return ResponseEntity.ok(categoryMapper.toDto(updated));
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable Integer id) {
        if (productRepository.existsByCategoryId(id)) {
            return ResponseEntity
                    .status(409)
                    .body("Kategorie kann nicht gelöscht werden, da Produkte damit verknüpft sind.");
        }

        categoryService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
