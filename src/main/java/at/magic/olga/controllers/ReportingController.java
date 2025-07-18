package at.magic.olga.controllers;

import at.magic.olga.dto.view.DailyProfitLossDto;
import at.magic.olga.dto.view.WeeklyProfitLossDto;
import at.magic.olga.dto.view.CashJournalDto;
import at.magic.olga.dto.view.YearlyProfitLossDto;
import at.magic.olga.dto.view.AnnualSummaryDto;
import at.magic.olga.service.ReportingService;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * REST controller for reporting endpoints.
 */
@RestController
@RequestMapping("/api/reports")
@Validated
public class ReportingController {

    private final ReportingService reportingService;

    public ReportingController(ReportingService reportingService) {
        this.reportingService = reportingService;
    }

    /**
     * Get daily profit and loss records.
     */
    @GetMapping("/daily")
    public ResponseEntity<List<DailyProfitLossDto>> getDailyProfitLoss() {
        List<DailyProfitLossDto> dtoList = reportingService.getDailyProfitLoss();
        return ResponseEntity.ok(dtoList);
    }

    /**
     * Get weekly profit and loss records.
     */
    @GetMapping("/weekly")
    public ResponseEntity<List<WeeklyProfitLossDto>> getWeeklyProfitLoss() {
        List<WeeklyProfitLossDto> dtoList = reportingService.getWeeklyProfitLoss();
        return ResponseEntity.ok(dtoList);
    }

    /**
     * Get monthly cash journal (profit & loss) for a given year.
     * @param year  four-digit year between 2000 and 2100
     */
    @GetMapping("/monthly")
    public ResponseEntity<List<CashJournalDto>> getMonthlyReport(
            @RequestParam @Min(2000) @Max(2100) Integer year) {
        List<CashJournalDto> dtoList = reportingService.getMonthlyReport(year);
        return ResponseEntity.ok(dtoList);
    }

    /**
     * Get yearly profit and loss records.
     */
    @GetMapping("/yearly")
    public ResponseEntity<List<YearlyProfitLossDto>> getYearlyProfitLoss() {
        List<YearlyProfitLossDto> dtoList = reportingService.getYearlyProfitLoss();
        return ResponseEntity.ok(dtoList);
    }

    /**
     * Get aggregated summary across all years.
     */
    @GetMapping("/summary")
    public ResponseEntity<AnnualSummaryDto> getAnnualSummary() {
        AnnualSummaryDto summary = reportingService.getAnnualSummary();
        return ResponseEntity.ok(summary);
    }
}
