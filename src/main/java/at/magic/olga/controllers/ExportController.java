package at.magic.olga.controllers;


import at.magic.olga.dto.view.DailyProfitLossDto;
import at.magic.olga.service.ExportService;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import org.springframework.http.MediaType;
import org.springframework.ui.Model;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * REST‑контроллер для скачивания PDF‑отчётов.
 */
@RestController
@RequestMapping("/api/export")
@Validated
public class ExportController {

    private final ExportService exportService;

    public ExportController(ExportService exportService) {
        this.exportService = exportService;
    }

    /**
     * Скачивание ежедневного отчёта в PDF.
     */
    @GetMapping(value = "/daily.pdf", produces = MediaType.APPLICATION_PDF_VALUE)
    public void dailyReport(HttpServletResponse response) throws IOException {
        exportService.exportDailyPdf(response);
    }


    /**
     * Скачивание еженедельного отчёта в PDF.
     */
    @GetMapping(value = "/weekly.pdf", produces = MediaType.APPLICATION_PDF_VALUE)
    public void weeklyReport(HttpServletResponse response) throws IOException {
        exportService.exportWeeklyPdf(response);
    }

    /**
     * Скачивание ежемесячного отчёта в PDF за указанный год.
     */
    @GetMapping(value = "/monthly.pdf", produces = MediaType.APPLICATION_PDF_VALUE)
    public void monthlyReport(
            HttpServletResponse response,
            @RequestParam @Min(2000) @Max(2100) Integer year
    ) throws IOException {
        exportService.exportMonthlyPdf(response, year);
    }

    /**
     * Скачивание годового отчёта в PDF.
     */
    @GetMapping(value = "/yearly.pdf", produces = MediaType.APPLICATION_PDF_VALUE)
    public void yearlyReport(HttpServletResponse response) throws IOException {
        exportService.exportYearlyPdf(response);
    }

    /**
     * Скачивание сводного отчёта (аннуал) в PDF.
     */
    @GetMapping(value = "/summary.pdf", produces = MediaType.APPLICATION_PDF_VALUE)
    public void summaryReport(HttpServletResponse response) throws IOException {
        exportService.exportSummaryPdf(response);
    }
}
