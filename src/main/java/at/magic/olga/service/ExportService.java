package at.magic.olga.service;

import at.magic.olga.dto.view.AnnualSummaryDto;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;
import com.openhtmltopdf.pdfboxout.PdfRendererBuilder;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Service responsible solely for exporting PDF reports.
 */
@Service
public class ExportService {

    private final ReportingService reportingService;
    private final SpringTemplateEngine templateEngine;

    public ExportService(ReportingService reportingService,
                         SpringTemplateEngine templateEngine) {
        this.reportingService = reportingService;
        this.templateEngine = templateEngine;
    }

    /**
     * Export daily profit & loss report as PDF.
     */
    public void exportDailyPdf(HttpServletResponse response) throws IOException {
        renderReport(
                response,
                "Daily Profit & Loss",
                List.of("day","salesAmount","cardExpenses","cashExpenses","netProfit"),
                reportingService.getDailyProfitLoss().stream().map(d -> Map.<String,String>of(
                        "day", d.getDay().toString(),
                        "salesAmount", d.getSalesAmount().toString(),
                        "cardExpenses", d.getCardExpenses().toString(),
                        "cashExpenses", d.getCashExpenses().toString(),
                        "netProfit", d.getNetProfit().toString()
                )).collect(Collectors.toList()),
                "daily_report.pdf"
        );
    }


    /**
     * Export weekly profit & loss report as PDF.
     */
    public void exportWeeklyPdf(HttpServletResponse response) throws IOException {
        renderReport(
                response,
                "Weekly Profit & Loss",
                List.of("weekStart","salesAmount","cardExpenses","cashExpenses","netProfit"),
                reportingService.getWeeklyProfitLoss().stream().map(w -> Map.<String,String>of(
                        "weekStart", w.getWeekStart().toString(),
                        "salesAmount", w.getSalesAmount().toString(),
                        "cardExpenses", w.getCardExpenses().toString(),
                        "cashExpenses", w.getCashExpenses().toString(),
                        "netProfit", w.getNetProfit().toString()
                )).collect(Collectors.toList()),
                "weekly_report.pdf"
        );
    }

    /**
     * Export monthly cash journal for a given year as PDF.
     */
    public void exportMonthlyPdf(HttpServletResponse response, Integer year) throws IOException {
        renderReport(
                response,
                "Monthly Cash Journal " + year,
                List.of("id","monthNumber","year","incomeTotal","cardExpensesTotal","cashExpensesTotal","balanceChange","runningTotal"),
                reportingService.getMonthlyReport(year).stream().map(j -> Map.<String,String>of(
                        "id", j.getId().toString(),
                        "monthNumber", j.getMonthNumber().toString(),
                        "year", j.getYear().toString(),
                        "incomeTotal", j.getIncomeTotal().toString(),
                        "cardExpensesTotal", j.getCardExpensesTotal().toString(),
                        "cashExpensesTotal", j.getCashExpensesTotal().toString(),
                        "balanceChange", j.getBalanceChange().toString(),
                        "runningTotal", j.getRunningTotal().toString()
                )).collect(Collectors.toList()),
                "monthly_report_" + year + ".pdf"
        );
    }

    /**
     * Export yearly profit & loss report as PDF.
     */
    public void exportYearlyPdf(HttpServletResponse response) throws IOException {
        renderReport(
                response,
                "Yearly Profit & Loss",
                List.of("year","salesAmount","cardExpenses","cashExpenses","netProfit"),
                reportingService.getYearlyProfitLoss().stream().map(y -> Map.<String,String>of(
                        "year", y.getYear().toString(),
                        "salesAmount", y.getSalesAmount().toString(),
                        "cardExpenses", y.getCardExpenses().toString(),
                        "cashExpenses", y.getCashExpenses().toString(),
                        "netProfit", y.getNetProfit().toString()
                )).collect(Collectors.toList()),
                "yearly_report.pdf"
        );
    }

    /**
     * Export aggregate annual summary as PDF.
     */
    public void exportSummaryPdf(HttpServletResponse response) throws IOException {
        AnnualSummaryDto summary = reportingService.getAnnualSummary();
        renderReport(
                response,
                "Annual Summary",
                List.of("totalSales","totalCardExpenses","totalCashExpenses","totalNetProfit"),
                List.of(Map.of(
                        "totalSales", summary.getTotalSales().toString(),
                        "totalCardExpenses", summary.getTotalCardExpenses().toString(),
                        "totalCashExpenses", summary.getTotalCashExpenses().toString(),
                        "totalNetProfit", summary.getTotalNetProfit().toString()
                )),
                "summary_report.pdf"
        );
    }

    /**
     * General-purpose PDF rendering.
     */
    private void renderReport(HttpServletResponse response,
                              String title,
                              List<String> columns,
                              List<Map<String,String>> rows,
                              String filename) throws IOException {
        response.setContentType("application/pdf");
        response.setHeader("Content-Disposition", "attachment; filename=\"" + filename + "\"");

        Context ctx = new Context();
        ctx.setVariable("title", title);
        ctx.setVariable("columns", columns);
        ctx.setVariable("rows", rows);
        String html = templateEngine.process("report", ctx);

        try (OutputStream os = response.getOutputStream()) {
            PdfRendererBuilder builder = new PdfRendererBuilder();
            builder.withHtmlContent(html, null)
                    .toStream(os)
                    .run();
        } catch (Exception e) {
            throw new IOException("PDF generation failed", e);
        }
    }
}
