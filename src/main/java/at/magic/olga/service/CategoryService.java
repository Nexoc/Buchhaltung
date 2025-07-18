package at.magic.olga.service;

import at.magic.olga.models.*;
import at.magic.olga.repositories.*;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
public class CategoryService {
    private final CategoryRepository categoryRepo;

    public CategoryService(CategoryRepository categoryRepo) {
        this.categoryRepo = categoryRepo;
    }

    public List<Category> findAll() {
        return categoryRepo.findAll();
    }

    public Category findById(Integer id) {
        return categoryRepo.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Category not found: " + id));
    }

    public Category create(Category category) {
        return categoryRepo.save(category);
    }

    @Transactional
    public Category update(Integer id, Category updated) {
        Category existing = findById(id);
        existing.setName(updated.getName());
        existing.setDescription(updated.getDescription());
        return categoryRepo.save(existing);
    }

    public void delete(Integer id) {
        categoryRepo.deleteById(id);
    }
}