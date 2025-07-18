package at.magic.olga.models;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

/**
 * Entity representing a sale transaction.
 * Maps to the 'sales' table in the database.
 */
@Entity
@Table(name = "sales")
@Data
public class Sale {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * The product sold in this transaction.
     * Many-to-one relationship to Product.
     */
    @ManyToOne
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    @Column(name = "sale_time", nullable = false)
    private LocalDateTime saleTime;
    private Integer quantity;

    @Enumerated(EnumType.STRING)
    @Column(name = "payment_method", nullable = false)
    private PaymentMethod paymentMethod;

    @Column(columnDefinition = "TEXT")
    private String comment;

    /**
     * Enum representing supported payment methods.
     */
    public enum PaymentMethod {
        CASH,
        CARD
    }
}
