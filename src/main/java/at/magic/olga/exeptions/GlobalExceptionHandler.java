package at.magic.olga.exeptions;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintViolationException;
import org.springframework.http.*;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;
import java.io.FileNotFoundException;
import java.time.Instant;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.web.servlet.resource.NoResourceFoundException;

/**
 * централизованный обработчик исключений для REST контроллеров
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    /** 400 — ошибки валидации @RequestBody */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> handleValidation(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getFieldErrors().forEach(err ->
                errors.put(err.getField(), err.getDefaultMessage())
        );
        return ResponseEntity.badRequest().body(errors);
    }

    /** 400 — ошибки валидации @PathVariable и @RequestParam */
    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<Map<String, String>> handleConstraint(ConstraintViolationException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getConstraintViolations().forEach(cv ->
                errors.put(cv.getPropertyPath().toString(), cv.getMessage())
        );
        return ResponseEntity.badRequest().body(errors);
    }

    /** 404 — когда не нашли сущность */
    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<String> handleNotFound(EntityNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
    }

    /** 404 — когда не найден файл */
    @ExceptionHandler(FileNotFoundException.class)
    public ResponseEntity<String> handleFileNotFound(FileNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
    }

    /** 500 — все остальные ошибки */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, Object>> handleAll(Exception ex) {
        ex.printStackTrace();  // лог в консоль
        Map<String, Object> body = new LinkedHashMap<>();
        body.put("timestamp", Instant.now().toString());
        body.put("status", HttpStatus.INTERNAL_SERVER_ERROR.value());
        body.put("error", "Internal Server Error");
        body.put("message", ex.getMessage());      // <<< вот тут ваше реальное сообщение
        body.put("path",  /* можно достать текущий URI через RequestContextHolder */ "" );
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .contentType(MediaType.APPLICATION_JSON)
                .body(body);
    }


    @ExceptionHandler(NoResourceFoundException.class)
    public ResponseEntity<?> handleNoResource(NoResourceFoundException ex, HttpServletRequest req) throws NoResourceFoundException {
        String path = req.getRequestURI();
        // если swagger/static — не трогаем, даём Spring отдавать статику
        if (path.startsWith("/webjars/") ||
                path.startsWith("/swagger-ui/") ||
                path.startsWith("/v3/api-docs/")) {
            // ВАЖНО: не возвращать ResponseEntity, а пробросить исключение дальше
            throw ex;
        }
        // для остальных случаев — ваша логика
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("У нас тут какая‑то внутренняя ошибка");
    }
}
