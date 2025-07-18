package at.magic.olga.repositories;

import at.magic.olga.models.Sale;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface SaleRepository extends JpaRepository<Sale, Long> {
    List<Sale> findBySaleTimeBetween(LocalDateTime from, LocalDateTime to);
}