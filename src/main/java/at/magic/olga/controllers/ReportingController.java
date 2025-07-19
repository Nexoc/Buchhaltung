package at.magic.olga.controllers;

import at.magic.olga.dto.view.DailyProfitLossDto;
import at.magic.olga.dto.view.WeeklyProfitLossDto;
import at.magic.olga.dto.view.CashJournalDto;
import at.magic.olga.dto.view.YearlyProfitLossDto;
import at.magic.olga.dto.view.AnnualSummaryDto;
import at.magic.olga.mappers.view.*;
import at.magic.olga.models.view.DailyProfitLoss;
import at.magic.olga.models.view.YearCashJournal;
import at.magic.olga.service.ReportingService;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;

import java.util.List;


import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/reports")
@Validated
public class ReportingController {

    private final ReportingService reportingService;
    private final DailyProfitLossMapper dplMapper;
    private final WeeklyProfitLossMapper weeklyMapper;
    private final CashJournalMapper journalMapper;
    private final YearlyProfitLossMapper yearlyMapper;
    private final AnnualSummaryMapper summaryMapper;

    public ReportingController(ReportingService reportingService,
                               DailyProfitLossMapper dplMapper,
                               WeeklyProfitLossMapper weeklyMapper,
                               CashJournalMapper journalMapper,
                               YearlyProfitLossMapper yearlyMapper,
                               AnnualSummaryMapper summaryMapper) {
        this.reportingService = reportingService;
        this.dplMapper        = dplMapper;
        this.weeklyMapper     = weeklyMapper;
        this.journalMapper    = journalMapper;
        this.yearlyMapper     = yearlyMapper;
        this.summaryMapper    = summaryMapper;
    }

    @GetMapping("/daily")
    public ResponseEntity<List<DailyProfitLossDto>> getDaily() {
        var entities = reportingService.getDailyProfitLoss();
        var dtos     = dplMapper.toDtoList(entities);
        return ResponseEntity.ok(dtos);
    }

    @GetMapping("/weekly")
    public ResponseEntity<List<WeeklyProfitLossDto>> getWeekly() {
        var entities = reportingService.getWeeklyProfitLoss();
        var dtos     = weeklyMapper.toDtoList(entities);
        return ResponseEntity.ok(dtos);
    }

    @GetMapping("/daily-by-month")
    public ResponseEntity<List<DailyProfitLossDto>> getDailyByMonth(
            @RequestParam @Min(2000) @Max(2100) Integer year,
            @RequestParam @Min(1)    @Max(12)   Integer month) {
        var entities = reportingService.getDailyProfitLossByMonth(year, month);
        var dtos     = dplMapper.toDtoList(entities);
        return ResponseEntity.ok(dtos);
    }

    @GetMapping("/monthly")
    public ResponseEntity<List<CashJournalDto>> getMonthly(
            @RequestParam @Min(2000) @Max(2100) Integer year) {
        var entities = reportingService.getMonthlyReport(year);
        var dtos     = journalMapper.toDtoList(entities);
        return ResponseEntity.ok(dtos);
    }

    @GetMapping("/monthly-by-year")
    public ResponseEntity<List<CashJournalDto>> getMonthlyByYear(
            @RequestParam @Min(2000) @Max(2100) Integer year) {
        var entities = reportingService.getMonthlyReport(year);
        var dtos     = journalMapper.toDtoList(entities);
        return ResponseEntity.ok(dtos);
    }

    @GetMapping("/yearly")
    public ResponseEntity<List<YearlyProfitLossDto>> getYearly() {
        var entities = reportingService.getYearlyProfitLoss();
        var dtos     = yearlyMapper.toDtoList(entities);
        return ResponseEntity.ok(dtos);
    }

    @GetMapping("/summary")
    public ResponseEntity<AnnualSummaryDto> getSummary() {
        var entity = reportingService.getAnnualSummary();
        var dto    = summaryMapper.toDto(entity);
        return ResponseEntity.ok(dto);
    }
}
