package at.magic.olga.repositories.view;

import at.magic.olga.models.view.DailyProfitLoss;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

/**
 * Repository for DailyProfitLoss view entities.
 */
@Repository
public interface DailyProfitLossRepository extends JpaRepository<DailyProfitLoss, Long> {
    List<DailyProfitLoss> findAllByDayBetween(LocalDate start, LocalDate end);

    /**
     * Находит все записи P&L, у которых поле day лежит в заданном году и месяце.
     */
    @Query("select d from DailyProfitLoss d " +
            "where year(d.day) = :year and month(d.day) = :month " +
            "order by d.day")
    List<DailyProfitLoss> findByYearAndMonth(@Param("year") int year,
                                             @Param("month") int month);
}
