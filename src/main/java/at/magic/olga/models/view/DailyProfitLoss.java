package at.magic.olga.models.view;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.Immutable;

import java.time.LocalDate;

@Entity
@Table(name = "daily_pl")
@Data
@Immutable
public class DailyProfitLoss {

    @Id
    private Long id;

    @Column(unique = true)
    private LocalDate day;

    private Double salesAmount;
    private Double cardExpenses;
    private Double cashExpenses;
    private Double netProfit;

    @Column(name = "description", columnDefinition = "TEXT")
    private String description;
}
