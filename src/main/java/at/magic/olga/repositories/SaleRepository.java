package at.magic.olga.repositories;

import at.magic.olga.models.Sale;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDateTime;
import java.util.List;

public interface SaleRepository extends JpaRepository<Sale, Long> {
    List<Sale> findBySaleTimeBetween(LocalDateTime from, LocalDateTime to);

    @Query("select s from Sale s where year(s.saleTime)=?1 and month(s.saleTime)=?2")
    List<Sale> findByYearAndMonth(int year, int month);

    @Query("select s from Sale s where year(s.saleTime)=?1")
    List<Sale> findByYear(int year);

    List<Sale> findByProductId(Integer productId);

}