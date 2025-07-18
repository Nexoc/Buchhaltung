package at.magic.olga.models;

import jakarta.persistence.*;
import lombok.Data;

/**
 * Entity representing an instruction file for a product.
 * Maps to the 'instructions' table in the database.
 */
@Entity
@Table(name = "instructions")
@Data
public class Instruction {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String filename;
    private String description;

    /**
     * Many-to-one relationship to Product.
     * Represents which product this instruction belongs to.
     * Maps to the 'product_id' foreign key column and is mandatory.
     */
    @ManyToOne
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;
}
