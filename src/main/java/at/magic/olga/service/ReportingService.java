package at.magic.olga.service;

import at.magic.olga.models.view.DailyProfitLoss;
import at.magic.olga.models.view.WeeklyProfitLoss;
import at.magic.olga.models.view.YearCashJournal;
import at.magic.olga.models.view.YearlyProfitLoss;
import at.magic.olga.models.view.AnnualSummary;
import at.magic.olga.repositories.view.DailyProfitLossRepository;
import at.magic.olga.repositories.view.WeeklyProfitLossRepository;
import at.magic.olga.repositories.view.CashJournalRepository;
import at.magic.olga.repositories.view.YearlyProfitLossRepository;
import at.magic.olga.repositories.view.AnnualSummaryRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ReportingService {

    private final DailyProfitLossRepository    dailyRepo;
    private final WeeklyProfitLossRepository   weeklyRepo;
    private final CashJournalRepository        journalRepo;
    private final YearlyProfitLossRepository   yearlyRepo;
    private final AnnualSummaryRepository      summaryRepo;

    public ReportingService(DailyProfitLossRepository dailyRepo,
                            WeeklyProfitLossRepository weeklyRepo,
                            CashJournalRepository journalRepo,
                            YearlyProfitLossRepository yearlyRepo,
                            AnnualSummaryRepository summaryRepo) {
        this.dailyRepo   = dailyRepo;
        this.weeklyRepo  = weeklyRepo;
        this.journalRepo = journalRepo;
        this.yearlyRepo  = yearlyRepo;
        this.summaryRepo = summaryRepo;
    }

    /**
     * Все записи P&L по дням.
     */
    public List<DailyProfitLoss> getDailyProfitLoss() {
        return dailyRepo.findAll();
    }

    /**
     * Все записи P&L по неделям.
     */
    public List<WeeklyProfitLoss> getWeeklyProfitLoss() {
        return weeklyRepo.findAll();
    }

    /**
     * P&L по дням внутри указанного года и месяца.
     */
    public List<DailyProfitLoss> getDailyProfitLossByMonth(int year, int month) {
        return dailyRepo.findByYearAndMonth(year, month);
    }

    /**
     * Ежемесячный журнал (P&L) за указанный год.
     */
    public List<YearCashJournal> getMonthlyReport(int year) {
        return journalRepo.findByYear(year);
    }

    /**
     * P&L по годам.
     */
    public List<YearlyProfitLoss> getYearlyProfitLoss() {
        return yearlyRepo.findAll();
    }

    /**
     * Сводный агрегированный отчёт за все годы.
     */
    public AnnualSummary getAnnualSummary() {
        return summaryRepo.findFirst();
    }



}
