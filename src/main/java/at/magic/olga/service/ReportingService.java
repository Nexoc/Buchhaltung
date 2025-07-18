package at.magic.olga.service;

import at.magic.olga.models.view.YearCashJournal;
import at.magic.olga.repositories.view.CashJournalRepository;
import at.magic.olga.repositories.view.DailyProfitLossRepository;
import org.springframework.stereotype.Service;

import java.util.List;

import at.magic.olga.dto.view.DailyProfitLossDto;
import at.magic.olga.dto.view.CashJournalDto;
import at.magic.olga.dto.view.WeeklyProfitLossDto;
import at.magic.olga.dto.view.YearlyProfitLossDto;
import at.magic.olga.dto.view.AnnualSummaryDto;
import at.magic.olga.mappers.view.DailyProfitLossMapper;
import at.magic.olga.mappers.view.CashJournalMapper;
import at.magic.olga.mappers.view.WeeklyProfitLossMapper;
import at.magic.olga.mappers.view.YearlyProfitLossMapper;
import at.magic.olga.mappers.view.AnnualSummaryMapper;

import at.magic.olga.models.view.AnnualSummary;

import at.magic.olga.repositories.view.WeeklyProfitLossRepository;
import at.magic.olga.repositories.view.YearlyProfitLossRepository;
import at.magic.olga.repositories.view.AnnualSummaryRepository;

import java.util.stream.Collectors;

@Service
public class ReportingService {

    private final DailyProfitLossRepository dailyRepo;
    private final CashJournalRepository journalRepo;
    private final WeeklyProfitLossRepository weeklyRepo;
    private final YearlyProfitLossRepository yearlyRepo;
    private final AnnualSummaryRepository annualRepo;

    private final DailyProfitLossMapper dailyMapper;
    private final CashJournalMapper monthlyMapper;
    private final WeeklyProfitLossMapper weeklyMapper;
    private final YearlyProfitLossMapper yearlyMapper;
    private final AnnualSummaryMapper annualMapper;

    public ReportingService(
            DailyProfitLossRepository dailyRepo,
            CashJournalRepository journalRepo,
            WeeklyProfitLossRepository weeklyRepo,
            YearlyProfitLossRepository yearlyRepo,
            AnnualSummaryRepository annualRepo,
            DailyProfitLossMapper dailyMapper,
            CashJournalMapper monthlyMapper,
            WeeklyProfitLossMapper weeklyMapper,
            YearlyProfitLossMapper yearlyMapper,
            AnnualSummaryMapper annualMapper) {
        this.dailyRepo = dailyRepo;
        this.journalRepo = journalRepo;
        this.weeklyRepo = weeklyRepo;
        this.yearlyRepo = yearlyRepo;
        this.annualRepo = annualRepo;
        this.dailyMapper = dailyMapper;
        this.monthlyMapper = monthlyMapper;
        this.weeklyMapper = weeklyMapper;
        this.yearlyMapper = yearlyMapper;
        this.annualMapper = annualMapper;
    }

    /**
     * Get profit and loss for each day.
     */
    public List<DailyProfitLossDto> getDailyProfitLoss() {
        return dailyRepo.findAll().stream()
                .map(dailyMapper::toDto)
                .collect(Collectors.toList());
    }

    /**
     * Get profit and loss for each week.
     */
    public List<WeeklyProfitLossDto> getWeeklyProfitLoss() {
        return weeklyRepo.findAll().stream()
                .map(weeklyMapper::toDto)
                .collect(Collectors.toList());
    }

    /**
     * Get monthly cash journal entries for a specific year.
     */
    public List<CashJournalDto> getMonthlyReport(Integer year) {
        List<YearCashJournal> list = journalRepo.findByYear(year);
        return monthlyMapper.toDtoList(list);
    }

    /**
     * Get profit and loss for each year.
     */
    public List<YearlyProfitLossDto> getYearlyProfitLoss() {
        return yearlyRepo.findAll().stream()
                .map(yearlyMapper::toDto)
                .collect(Collectors.toList());
    }

    /**
     * Get aggregated summary across all years (single record).
     */
    public AnnualSummaryDto getAnnualSummary() {
        AnnualSummary entity = annualRepo.findAll().stream().findFirst().orElse(null);
        return annualMapper.toDto(entity);
    }
}
