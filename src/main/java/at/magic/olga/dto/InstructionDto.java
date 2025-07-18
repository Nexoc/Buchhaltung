package at.magic.olga.dto;

import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class InstructionDto {

    private Integer id;

    private String filename;

    @Size(max = 255)
    private String description;

    @NotNull
    private Integer productId;
}
