package at.magic.olga.dto;

import jakarta.validation.constraints.*;
import lombok.Data;
import java.time.LocalDateTime;
import at.magic.olga.models.Sale.PaymentMethod;

@Data
public class SaleDto {

    private Long id;

    @NotNull
    private Integer productId;

    private LocalDateTime saleTime;

    @NotNull
    @Min(1)
    private Integer quantity;

    @NotNull
    private PaymentMethod paymentMethod;

    @Size(max = 255)
    private String comment;
}
