package at.magic.olga.models;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * Entity representing a product in inventory.
 * Maps to the 'products' table in the database.
 */
@Entity
@Table(name = "products")
@Data
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String name;
    private Double price;
    private Integer stock;

    // Many-to-one relationship to Category (mandatory)
    @ManyToOne
    @JoinColumn(name = "category_id", nullable = false)
    private Category category;

    // Date when the product was added (maps to 'added_date')
    @Column(name = "added_date")
    private LocalDate addedDate;

    // Path to the product image file (maps to 'image_path')
    @Column(name = "image_path")
    private String imagePath;

    // Long text field for product description with no length limit in the database
    @Column(columnDefinition = "TEXT")
    private String description;

    /**
     * cascade = CascadeType.ALL enables all JPA cascade operations from Product to its instructions:
     * - PERSIST: new instructions are automatically saved when the product is saved.
     * - MERGE: updates to the product are propagated to its instructions.
     * - REMOVE: deleting the product also deletes its instructions.
     * - REFRESH: refreshing the product reloads its instructions from the database.
     * - DETACH: detaching the product detaches its instructions from the persistence context.
     */
    @OneToMany(
            mappedBy = "product",        // owning side in Instruction
            cascade = CascadeType.ALL,     // cascade operations to instructions
            orphanRemoval = true         // delete orphans
    )
    private List<Instruction> instructions = new ArrayList<>();
}
