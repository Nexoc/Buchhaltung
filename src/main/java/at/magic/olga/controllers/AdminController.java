package at.magic.olga.controllers;

import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@RestController
@RequestMapping("/api/admin")
public class AdminController {

    @GetMapping(value = "/backup", produces = MediaType.APPLICATION_OCTET_STREAM_VALUE)
    public void backupDb(HttpServletResponse response) throws IOException {
        String filename = "backup_" + LocalDate.now().format(DateTimeFormatter.ofPattern("dd_MM_yyyy")) + ".dump";

        ProcessBuilder pb = new ProcessBuilder(
                "docker", "exec", "-t", "my-postgres",
                "pg_dump", "-U", "postgres", "-F", "c", "-d", "magic"
        );

        Process process = pb.start();

        response.setContentType("application/octet-stream");
        response.setHeader("Content-Disposition", "attachment; filename=\"" + filename + "\"");

        try (InputStream is = process.getInputStream();
             OutputStream os = response.getOutputStream()) {
            is.transferTo(os);
        }
    }


    @PostMapping("/api/restore")
    public ResponseEntity<?> restoreBackup(@RequestParam("file") MultipartFile file) {
        try {
            // Сохраняем загруженный файл во временную директорию
            Path tempFile = Files.createTempFile("restore-", ".dump");
            file.transferTo(tempFile.toFile());

            // Команда pg_restore
            String dbUser = System.getenv("POSTGRES_USER");
            String dbPassword = System.getenv("POSTGRES_PASSWORD");
            String dbName = System.getenv("POSTGRES_DB");

            ProcessBuilder pb = new ProcessBuilder(
                    "docker", "exec", "-i", "my-postgres",
                    "pg_restore", "-U", dbUser, "-d", dbName, "--clean"
            );
            pb.environment().put("PGPASSWORD", dbPassword);


            Process process = pb.start();

            // Передаём файл через stdin в pg_restore
            try (OutputStream os = process.getOutputStream();
                 InputStream is = new FileInputStream(tempFile.toFile())) {
                is.transferTo(os);
            }

            int exitCode = process.waitFor();
            Files.delete(tempFile);

            if (exitCode == 0) {
                return ResponseEntity.ok("Backup restored successfully");
            } else {
                return ResponseEntity.status(500).body("Restore failed with exit code " + exitCode);
            }

        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error during restore: " + e.getMessage());
        }
    }


}
