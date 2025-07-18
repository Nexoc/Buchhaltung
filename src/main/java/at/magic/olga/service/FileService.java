package at.magic.olga.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

@Service
public class FileService {

    private final Path rootDir;

    public FileService(@Value("${file.upload-dir}") String uploadDir) {
        this.rootDir = Paths.get(uploadDir).toAbsolutePath().normalize();
        try {
            Files.createDirectories(rootDir);
        } catch (IOException e) {
            throw new RuntimeException("Could not create upload directory", e);
        }
    }

    /** Сохранить файл в подпапку и вернуть относительный путь */
    public String store(MultipartFile file, String subDir) throws IOException {
        String originalName = StringUtils.cleanPath(file.getOriginalFilename());

        // Проверка на недопустимые символы и пустое имя
        if (originalName.contains("..") || originalName.isBlank()) {
            throw new IOException("Invalid file name: " + originalName);
        }

        Path targetDir = rootDir.resolve(subDir).normalize();
        Files.createDirectories(targetDir);

        Path targetFile = targetDir.resolve(originalName).normalize();
        Files.copy(file.getInputStream(), targetFile, StandardCopyOption.REPLACE_EXISTING);

        return subDir + "/" + originalName;
    }

    /** Загрузить файл по относительному пути */
    public Resource load(String relativePath) throws IOException {
        Path file = rootDir.resolve(relativePath).normalize();

        if (!file.startsWith(rootDir)) {
            throw new SecurityException("Access to file outside upload directory is not allowed: " + relativePath);
        }

        Resource resource = new UrlResource(file.toUri());

        if (!resource.exists() || !resource.isReadable()) {
            throw new FileNotFoundException("File not found: " + relativePath);
        }

        return resource;
    }
}
