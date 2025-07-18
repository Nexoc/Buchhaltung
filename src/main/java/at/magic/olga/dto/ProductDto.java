package at.magic.olga.dto;

import jakarta.validation.constraints.*;
import lombok.Data;

import java.time.LocalDate;
import java.util.List;

@Data
public class ProductDto {

    private Integer id;

    @NotBlank
    @Size(max = 100)
    private String name;

    @NotNull
    @PositiveOrZero
    private Double price;

    @NotNull
    @Min(0)
    private Integer stock;

    @NotNull
    private Integer categoryId;

    @NotNull
    private LocalDate addedDate;

    @Size(max = 255)
    private String imagePath;

    @Size(max = 1000)
    private String description;

    private List<InstructionDto> instructions; // Только для чтения


}
