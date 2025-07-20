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
        Path path = Path.of(uploadDir);
        if (!Files.exists(path)) {
            Files.createDirectories(path); // Creates all intermediate directories
            System.out.println("Created folder: " + path.toAbsolutePath());
        } else {
            System.out.println("Folder already exists: " + path.toAbsolutePath());
        }
    }
}
