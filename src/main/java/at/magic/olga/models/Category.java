package at.magic.olga.models;

import jakarta.persistence.*;
import lombok.Data;
import java.util.ArrayList;
import java.util.List;

/**
 * Entity representing a product category.
 * Maps to the 'categories' table in the database.
 */
@Entity
@Table(name = "categories")
@Data
public class Category {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String name;
    private String description;
    private String imagePath;

    /**
     * One-to-many relationship to Product.
     * A category can have multiple products.
     */
    @OneToMany(
            mappedBy = "category",       // 'category' field in Product owns the relationship
            cascade = CascadeType.ALL,     // propagate all operations
            orphanRemoval = true          // remove products if they are no longer linked
    )
    private List<Product> products = new ArrayList<>();
}
