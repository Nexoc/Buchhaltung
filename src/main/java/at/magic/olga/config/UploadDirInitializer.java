package at.magic.olga.config;

import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

@Component
public class UploadDirInitializer {

    @Value("${file.upload-dir}")
    private String uploadDir;

    @PostConstruct
    public void init() throws IOException {
        Path root = Path.of(uploadDir);
        Path imagesDir = root.resolve("images");
        Path productImagesDir = imagesDir.resolve("products");
        Path categoryImagesDir = imagesDir.resolve("categories");
        Path defaultCategoryImage = categoryImagesDir.resolve("default.jpg");

        // Создание папки uploads/
        if (Files.notExists(root)) {
            Files.createDirectories(root);
            System.out.println("Created: " + root.toAbsolutePath());
        }

        // Создание папки uploads/images/
        if (Files.notExists(imagesDir)) {
            Files.createDirectories(imagesDir);
            System.out.println("Created: " + imagesDir.toAbsolutePath());
        }

        // Создание папки uploads/images/products/
        if (Files.notExists(productImagesDir)) {
            Files.createDirectories(productImagesDir);
            System.out.println("Created: " + productImagesDir.toAbsolutePath());
        }

        // Создание папки uploads/images/categories/
        if (Files.notExists(categoryImagesDir)) {
            Files.createDirectories(categoryImagesDir);
            System.out.println("Created: " + categoryImagesDir.toAbsolutePath());
        }

        // Копируем default.jpg, если не существует
        if (Files.notExists(defaultCategoryImage)) {
            Path source = Path.of("src/main/resources/static/default.jpg");
            if (Files.exists(source)) {
                Files.copy(source, defaultCategoryImage);
                System.out.println("Copied default.jpg to: " + defaultCategoryImage.toAbsolutePath());
            } else {
                System.err.println("default.jpg not found in src/main/resources/static");
            }
        }
    }
}
