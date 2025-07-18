package at.magic.olga.repositories;

import at.magic.olga.models.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProductRepository extends JpaRepository<Product, Integer> {
    // можно дописать методы поиска по названию, категории и т.п.
    List<Product> findByCategoryId(Integer categoryId);
}