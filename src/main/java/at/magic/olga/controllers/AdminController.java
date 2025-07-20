package at.magic.olga.controllers;

import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@RestController
@RequestMapping("/api/admin")
public class AdminController {

    @Value("${db.container-name:db_olga}")
    private String containerName;

    @Value("${db.name}")
    private String dbName;

    @Value("${db.user}")
    private String dbUser;

    @Value("${db.password}")
    private String dbPassword;

    @GetMapping(value = "/backup", produces = MediaType.APPLICATION_OCTET_STREAM_VALUE)
    public void backupDb(HttpServletResponse response) throws IOException {
        String filename = "backup_" + LocalDate.now().format(DateTimeFormatter.ofPattern("dd_MM_yyyy")) + ".dump";

        ProcessBuilder pb = new ProcessBuilder(
                "docker", "exec", "-t", containerName,
                "pg_dump", "-U", dbUser, "-F", "c", "-d", dbName
        );

        pb.environment().put("PGPASSWORD", dbPassword);
        pb.redirectErrorStream(true);

        Process process = pb.start();

        response.setContentType("application/octet-stream");
        response.setHeader("Content-Disposition", "attachment; filename=\"" + filename + "\"");

        try (InputStream is = process.getInputStream();
             OutputStream os = response.getOutputStream()) {
            is.transferTo(os);
        }

        int exitCode;
        try {
            exitCode = process.waitFor();
        } catch (InterruptedException e) {
            throw new IOException("Interrupted while waiting for pg_dump", e);
        }

        if (exitCode != 0) {
            throw new IOException("pg_dump failed with exit code " + exitCode);
        }
    }

    @PostMapping("/restore")
    public ResponseEntity<?> restoreBackup(@RequestParam("file") MultipartFile file) {
        try {
            if (file.isEmpty()) {
                return ResponseEntity.badRequest().body("No file uploaded.");
            }

            Path tempFile = Files.createTempFile("restore-", ".dump");
            file.transferTo(tempFile.toFile());

            if (dbUser == null || dbPassword == null || dbName == null || containerName == null) {
                return ResponseEntity.status(500).body("Database environment variables are missing.");
            }

            ProcessBuilder pb = new ProcessBuilder(
                    "docker", "exec", "-i", containerName,
                    "pg_restore", "-U", dbUser, "-d", dbName, "--clean"
            );
            pb.environment().put("PGPASSWORD", dbPassword);
            pb.redirectErrorStream(true);

            Process process = pb.start();

            try (OutputStream os = process.getOutputStream();
                 InputStream is = new FileInputStream(tempFile.toFile())) {
                is.transferTo(os);
            }

            StringBuilder output = new StringBuilder();
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    output.append(line).append("\n");
                }
            }

            int exitCode = process.waitFor();
            Files.deleteIfExists(tempFile);

            if (exitCode == 0) {
                return ResponseEntity.ok("Backup restored successfully.");
            } else {
                return ResponseEntity.status(500).body("Restore failed (code " + exitCode + "):\n" + output);
            }

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body("Error during restore: " + e.getMessage());
        }
    }
}
