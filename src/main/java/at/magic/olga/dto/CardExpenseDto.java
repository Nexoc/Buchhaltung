package at.magic.olga.dto;

import jakarta.validation.constraints.*;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * DTO для CardExpense с валидацией входных данных и описаниями для OpenAPI.
 */
@Data
public class CardExpenseDto {


    private Long id;

    @NotNull(message = "Expense time must be provided")
    private LocalDateTime expenseTime;

    @NotNull(message = "Amount must be provided")
    @PositiveOrZero(message = "Amount must be zero or positive")
    private Double amount;

    @Size(max = 255, message = "Description must be at most 255 characters")
    private String description;
}
