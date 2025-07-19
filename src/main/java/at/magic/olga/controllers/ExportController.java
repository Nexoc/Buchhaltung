package at.magic.olga.controllers;

import at.magic.olga.service.ExportService;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
@RequestMapping("/api/export")
@Validated
public class ExportController {

    private final ExportService exportService;

    public ExportController(ExportService exportService) {
        this.exportService = exportService;
    }

     // 1) транзакции (продажи + расходы) за указанный год и месяц
    @GetMapping(value = "/raw-by-month.pdf", produces = MediaType.APPLICATION_PDF_VALUE)
    public void rawByMonthReport(
            HttpServletResponse response,
            @RequestParam @Min(2000) @Max(2100) int year,
            @RequestParam @Min(1)     @Max(12)   int month
    ) throws IOException {
        exportService.exportRawByMonthPdf(response, year, month);
    }

     // 2) транзакции (продажи) за указанный год и месяц
    @GetMapping(value = "/raw-sales-by-month.pdf", produces = MediaType.APPLICATION_PDF_VALUE)
    public void rawSalesByMonthReport(
            HttpServletResponse response,
            @RequestParam @Min(2000) @Max(2100) int year,
            @RequestParam @Min(1)     @Max(12)   int month
    ) throws IOException {
        exportService.exportSalesByMonthPdf(response, year, month);
    }


     // 3) транзакции (покупки наликом) за указанный год и месяц
    @GetMapping(value = "/raw-cash-by-month.pdf", produces = MediaType.APPLICATION_PDF_VALUE)
    public void rawCashByMonthReport(
            HttpServletResponse response,
            @RequestParam @Min(2000) @Max(2100) int year,
            @RequestParam @Min(1)     @Max(12)   int month
    ) throws IOException {
        exportService.exportCashByMonthPdf(response, year, month);
    }


     // 4) транзакции (покупки по карте) за указанный год и месяц
    @GetMapping(value = "/raw-card-by-month.pdf", produces = MediaType.APPLICATION_PDF_VALUE)
    public void rawCardByMonthReport(
            HttpServletResponse response,
            @RequestParam @Min(2000) @Max(2100) int year,
            @RequestParam @Min(1)     @Max(12)   int month
    ) throws IOException {
        exportService.exportCardByMonthPdf(response, year, month);
    }

    // 5) транзакции (продажи) за указанный год и месяц
    @GetMapping(value = "/raw-sales-day-by-month.pdf", produces = MediaType.APPLICATION_PDF_VALUE)
    public void rawSalesDayByMonthReport(
            HttpServletResponse response,
            @RequestParam @Min(2000) @Max(2100) int year,
            @RequestParam @Min(1)     @Max(12)   int month
    ) throws IOException {
        exportService.exportSalesDayByMonthPdf(response, year, month);
    }

    // 6) транзакции (продажи) за указанный год и месяц
    @GetMapping(value = "/raw-cash-day-by-month.pdf", produces = MediaType.APPLICATION_PDF_VALUE)
    public void rawCashDayByMonthReport(
            HttpServletResponse response,
            @RequestParam @Min(2000) @Max(2100) int year,
            @RequestParam @Min(1)     @Max(12)   int month
    ) throws IOException {
        exportService.exportCashDayByMonthPdf(response, year, month);
    }

    // 7) транзакции (продажи) за указанный год и месяц
    @GetMapping(value = "/raw-card-day-by-month.pdf", produces = MediaType.APPLICATION_PDF_VALUE)
    public void rawCardDayByMonthReport(
            HttpServletResponse response,
            @RequestParam @Min(2000) @Max(2100) int year,
            @RequestParam @Min(1)     @Max(12)   int month
    ) throws IOException {
        exportService.exportCardDayByMonthPdf(response, year, month);
    }

    // 8) транзакции (журнал кассы) за указанный год
    @GetMapping(value = "/kassa-journal.pdf", produces = MediaType.APPLICATION_PDF_VALUE)
    public void KassaReport(
            HttpServletResponse response,
            @RequestParam @Min(2000) @Max(2100) int year,
            @RequestParam int stand
    ) throws IOException {
        exportService.exportKassajournalPdf(response, year, stand);
    }
}
