package at.magic.olga.service;

import at.magic.olga.models.*;
import at.magic.olga.repositories.*;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Service
public class CategoryService {
    private final CategoryRepository categoryRepo;
    private final FileService fileService;

    public CategoryService(CategoryRepository categoryRepo, FileService fileService) {
        this.categoryRepo = categoryRepo;
        this.fileService = fileService;
    }

    public List<Category> findAll() {
        return categoryRepo.findAll();
    }

    public Category findById(Integer id) {
        return categoryRepo.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Category not found: " + id));
    }

    @Transactional
    public Category createWithImage(String name, String description, MultipartFile image) throws IOException {
        String imagePath;

        // Если загружен файл — сохранить его в images/categories
        if (image != null && !image.isEmpty()) {
            imagePath = fileService.store(image, "images/categories");
        } else {
            // Иначе использовать дефолтное изображение
            imagePath = "images/categories/default.jpg";
        }

        // Создаём категорию
        Category category = new Category();
        category.setName(name);
        category.setDescription(description);
        category.setImagePath(imagePath);

        // Сохраняем в БД
        return categoryRepo.save(category);
    }

    @Transactional
    public Category create(Category category) {
        if (category.getImagePath() == null || category.getImagePath().isBlank()) {
            category.setImagePath("images/categories/default.jpg");
        }
        return categoryRepo.save(category);
    }


    @Transactional
    public Category update(Integer id, Category updated) {
        Category existing = findById(id);

        existing.setName(updated.getName());
        existing.setDescription(updated.getDescription());
        existing.setImagePath(updated.getImagePath()); // если используется DTO с картинкой

        return categoryRepo.save(existing);
    }



    @Transactional
    public Category updateImage(Integer id, MultipartFile image) throws IOException {
        Category category = findById(id);

        if (image != null && !image.isEmpty()) {
            String imagePath = fileService.store(image, "images/categories");
            category.setImagePath(imagePath);
        }

        return categoryRepo.save(category);
    }


    public void delete(Integer id) {
        categoryRepo.deleteById(id);
    }


}