package at.magic.olga.service;

import at.magic.olga.repositories.SaleRepository;
import at.magic.olga.repositories.CashExpenseRepository;
import at.magic.olga.repositories.CardExpenseRepository;
import com.openhtmltopdf.pdfboxout.PdfRendererBuilder;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.io.IOException;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class ExportService {

    private static final Logger log = LoggerFactory.getLogger(ExportService.class);

    private final SaleRepository saleRepo;
    private final CashExpenseRepository cashRepo;
    private final CardExpenseRepository cardRepo;
    private final ReportingService reportingService;
    private final TemplateEngine thymeleaf;

    public ExportService(SaleRepository saleRepo,
                         CashExpenseRepository cashRepo,
                         CardExpenseRepository cardRepo,
            ReportingService reportingService,
            TemplateEngine thymeleaf) {
        this.saleRepo = saleRepo;
        this.cashRepo = cashRepo;
        this.cardRepo = cardRepo;
        this.reportingService = reportingService;
        this.thymeleaf = thymeleaf;
    }

     // 1) транзакции (продажи + расходы) за указанный год и месяц
     public void exportRawByMonthPdf(HttpServletResponse response, int year, int month) throws IOException {
         DateTimeFormatter fmt = DateTimeFormatter.ofPattern("HH:mm dd.MM.yyyy");
        // после того, как получили три списка:
         List<Map<String,Object>> sales   = fetchSales(year, month, fmt);
         List<Map<String,Object>> cashExp = fetchCashExpenses(year, month, fmt);
         List<Map<String,Object>> cardExp = fetchCardExpenses(year, month, fmt);

        // для каждого списка делаем маппинг в упрощённый формат,
        // оставляя только timestamp, type, total и comment
         List<Map<String,Object>> simplifiedSales = sales.stream()
                 .map(m -> Map.<String,Object>of(
                         "timestamp", m.get("timestamp"),
                         "type",      m.get("type"),
                         "total",     m.get("totalPaid"),
                         "comment",   m.get("comment")
                 ))
                 .collect(Collectors.toList());

         List<Map<String,Object>> simplifiedCash = cashExp.stream()
                 .map(m -> Map.<String,Object>of(
                         "timestamp", m.get("timestamp"),
                         "type",      m.get("type"),
                         "total",     m.get("amount"),
                         "comment",   m.get("comment")
                 ))
                 .collect(Collectors.toList());

         List<Map<String,Object>> simplifiedCard = cardExp.stream()
                 .map(m -> Map.<String,Object>of(
                         "timestamp", m.get("timestamp"),
                         "type",      m.get("type"),
                         "total",     m.get("amount"),
                         "comment",   m.get("comment")
                 ))
                 .collect(Collectors.toList());

        // объединяем всё в один список и сортируем по timestamp
         List<Map<String,Object>> summary = Stream.of(simplifiedSales, simplifiedCash, simplifiedCard)
                 .flatMap(List::stream)
                 .sorted(Comparator.comparing(m ->
                         LocalDateTime.parse((String) m.get("timestamp"), fmt)
                 ))
                 .collect(Collectors.toList());

        // передать summary в шаблон
         renderReport(
                 response,
                 "raw_summary",
                 String.format("Übersicht für %d-%02d", year, month),
                 List.of("timestamp", "type", "total", "comment"),
                 summary,
                 String.format("summary_%d_%02d.pdf", year, month)
         );
     }


     // 2) транзакции (продажи) за указанный год и месяц
    public void exportSalesByMonthPdf(HttpServletResponse response, int year, int month) throws IOException {
        DateTimeFormatter fmt = DateTimeFormatter.ofPattern("HH:mm dd.MM.yyyy");
        var sales   = fetchSales(year, month, fmt);
        renderReport(
                response,
                "report",
                String.format("Verkaufstransaktionen für %d-%02d", year, month),
                List.of("timestamp","type","productId","amount","paymentMethod","comment"),
                sales,
                String.format("sales_transactions_%d_%02d.pdf", year, month)
        );
    }

    // 3) транзакции (продажи) за указанный год и месяц с объединением
    public void exportSalesDayByMonthPdf(HttpServletResponse response, int year, int month) throws IOException {
        DateTimeFormatter fullFmt = DateTimeFormatter.ofPattern("HH:mm dd.MM.yyyy");
        DateTimeFormatter dateFmt = DateTimeFormatter.ofPattern("dd.MM.yyyy");

        var sales = fetchSales(year, month, fullFmt);

        var dailyTotals = sales.stream()
                .collect(Collectors.groupingBy(
                        s -> ((String) s.get("timestamp")).substring(6), // "HH:mm dd.MM.yyyy" -> "dd.MM.yyyy"
                        TreeMap::new,
                        Collectors.summingDouble(s -> ((Number) s.get("totalPaid")).doubleValue())
                ))
                .entrySet().stream()
                .map(entry -> Map.<String, Object>of(
                        "timestamp", entry.getKey(),                            // только дата с фиктивным временем
                        "amount",    Math.round(entry.getValue() * 100.0) / 100.0     // сумма продаж за день
                ))
                .collect(Collectors.toList());

        renderReport(
                response,
                "sales_per_day_monthly",
                String.format("Tägliche Zusammenfassung der Verkäufe für %d-%02d", year, month),
                List.of("timestamp", "amount"),
                dailyTotals,
                String.format("daily_sales_%d_%02d.pdf", year, month)
        );
    }


    // 4) транзакции (расходы наликом) за указанный год и месяц
    public void exportCashByMonthPdf(HttpServletResponse response, int year, int month) throws IOException {
        DateTimeFormatter fmt = DateTimeFormatter.ofPattern("HH:mm dd.MM.yyyy");
        var cashExp = fetchCashExpenses(year, month, fmt);
        renderReport(
                response,
                "cash-card_report",
                String.format("Bargeldtransaktionen für %d-%02d", year, month),
                List.of("timestamp","type","productId","amount","paymentMethod","comment"),
                cashExp,
                String.format("cash_transactions_%d_%02d.pdf", year, month)
        );
    }

    // 5) транзакции (расходы наликом) за указанный год и месяц с объединением
    public void exportCashDayByMonthPdf(HttpServletResponse response, int year, int month) throws IOException {
        DateTimeFormatter fullFmt = DateTimeFormatter.ofPattern("HH:mm dd.MM.yyyy");
        DateTimeFormatter dateFmt = DateTimeFormatter.ofPattern("dd.MM.yyyy");

        var cashExp = fetchCashExpenses(year, month, fullFmt);

        var dailyTotals = cashExp.stream()
                .collect(Collectors.groupingBy(
                        s -> ((String) s.get("timestamp")).substring(6), // "HH:mm dd.MM.yyyy" -> "dd.MM.yyyy"
                        TreeMap::new,
                        Collectors.summingDouble(s -> ((Number) s.get("amount")).doubleValue())
                ))
                .entrySet().stream()
                .map(entry -> Map.<String, Object>of(
                        "timestamp", entry.getKey(),  // только дата с фиктивным временем
                        "amount",    Math.round(entry.getValue() * 100.0) / 100.0            // сумма продаж за день
                ))
                .collect(Collectors.toList());

        renderReport(
                response,
                "sales_per_day_monthly",
                String.format("Tägliche Zusammenfassung der Barzahlungen für %d-%02d", year, month),
                List.of("timestamp", "amount"),
                dailyTotals,
                String.format("daily_cash_%d_%02d.pdf", year, month)
        );
    }

     // 6) транзакции (расходы картой) за указанный год и месяц
    public void exportCardByMonthPdf(HttpServletResponse response, int year, int month) throws IOException {

        DateTimeFormatter fmt = DateTimeFormatter.ofPattern("HH:mm dd.MM.yyyy");
        var cardExp = fetchCardExpenses(year, month, fmt);
        renderReport(
                response,
                "cash-card_report",
                String.format("Kartentransaktionen für %d-%02d", year, month),
                List.of("timestamp","type","productId","amount","paymentMethod","comment"),
                cardExp,
                String.format("card_transactions_%d_%02d.pdf", year, month)
        );
    }

    // 6) транзакции (расходы картой) за указанный год и месяц с объединением
    public void exportCardDayByMonthPdf(HttpServletResponse response, int year, int month) throws IOException {
        DateTimeFormatter fullFmt = DateTimeFormatter.ofPattern("HH:mm dd.MM.yyyy");
        DateTimeFormatter dateFmt = DateTimeFormatter.ofPattern("dd.MM.yyyy");

        var cardExp = fetchCardExpenses(year, month, fullFmt);

        var dailyTotals = cardExp.stream()
                .collect(Collectors.groupingBy(
                        s -> ((String) s.get("timestamp")).substring(6), // "HH:mm dd.MM.yyyy" -> "dd.MM.yyyy"
                        TreeMap::new,
                        Collectors.summingDouble(s -> ((Number) s.get("amount")).doubleValue())
                ))
                .entrySet().stream()
                .map(entry -> Map.<String, Object>of(
                        "timestamp", entry.getKey(),  // только дата с фиктивным временем
                        "amount",    Math.round(entry.getValue() * 100.0) / 100.0            // сумма продаж за день
                ))
                .collect(Collectors.toList());

        renderReport(
                response,
                "sales_per_day_monthly",
                String.format("Tägliche Zusammenfassung der Bargeldtransaktionen für %d-%02d", year, month),
                List.of("timestamp", "amount"),
                dailyTotals,
                String.format("daily_card_%d_%02d.pdf", year, month)
        );
    }


    // New export: Kassajournal
    public void exportKassajournalPdf(HttpServletResponse response, int year, double kassastandStart) throws IOException {
        List<Map<String, Object>> report = new ArrayList<>();
        double laufend = kassastandStart;

        for (int month = 1; month <= 12; month++) {
            final int currentMonth = month;

            double einnahmen = saleRepo.findByYearAndMonth(year, currentMonth).stream()
                    .mapToDouble(s -> {
                        if (s.getProduct() != null)
                            return s.getProduct().getPrice() * s.getQuantity();
                        return 0.0;
                    }).sum();

            double ec = cardRepo.findAll().stream()
                    .filter(e -> e.getExpenseTime().getYear() == year && e.getExpenseTime().getMonthValue() == currentMonth)
                    .mapToDouble(e -> e.getAmount()).sum();

            double ausgaben = cashRepo.findAll().stream()
                    .filter(e -> e.getExpenseTime().getYear() == year && e.getExpenseTime().getMonthValue() == currentMonth)
                    .mapToDouble(e -> e.getAmount()).sum();

            double bewegung = einnahmen - ec - ausgaben;
            laufend += bewegung;

            Map<String, Object> row = new HashMap<>();
            row.put("monat", monthName(month));
            row.put("einnahmen", einnahmen);
            row.put("ec", ec);
            row.put("ausgaben", ausgaben);
            row.put("bewegung", bewegung);
            row.put("laufend", laufend);
            report.add(row);
        }

        renderReport(
                response,
                "kassajournal",
                String.format("Kassajournal %d", year),
                List.of("monat", "einnahmen", "ec", "ausgaben", "bewegung", "laufend"),
                report,
                String.format("kassajournal_%d.pdf", year)
        );
    }

    private String monthName(int month) {
        return switch (month) {
            case 1 -> "Jänner";
            case 2 -> "Februar";
            case 3 -> "März";
            case 4 -> "April";
            case 5 -> "Mai";
            case 6 -> "Juni";
            case 7 -> "Juli";
            case 8 -> "August";
            case 9 -> "September";
            case 10 -> "Oktober";
            case 11 -> "November";
            case 12 -> "Dezember";
            default -> "";
        };
    }


    /** Сбор всех продаж за месяц в виде списка Map */
    private List<Map<String,Object>> fetchSales(int year, int month, DateTimeFormatter fmt) {
        var sales = saleRepo.findAll().stream()
                .filter(s -> {
                    var t = s.getSaleTime();
                    return t.getYear() == year && t.getMonthValue() == month;
                })
                .map(s -> {
                    // имя товара или "<без товара>"
                    String productName = s.getProduct() != null
                            ? s.getProduct().getName()
                            : "<без товара>";

                    // цена за единицу или 0.0, если товара нет
                    Double unitPrice = s.getProduct() != null
                            ? s.getProduct().getPrice()
                            : 0.0;

                    // итоговая сумма — цена * количество
                    Double totalPaid = unitPrice * s.getQuantity();

                    return Map.<String,Object>of(
                            "timestamp",     s.getSaleTime().format(fmt),
                            "type",          "SALE",
                            "productName",   productName,
                            "unitPrice",     unitPrice,      // цена за единицу
                            "quantity",      s.getQuantity(),
                            "totalPaid",     totalPaid,      // итоговая сумма
                            "paymentMethod", s.getPaymentMethod(),
                            "comment",       s.getComment()
                    );
                })
                .collect(Collectors.toList());

        log.info("Found {} sales records", sales.size());
        return sales;
    }


    /** Сбор всех cash‑расходов за месяц */
    private List<Map<String,Object>> fetchCashExpenses(int year, int month, DateTimeFormatter fmt) {
        var list = cashRepo.findAll().stream()
                .filter(e -> {
                    var t = e.getExpenseTime();
                    return t.getYear() == year && t.getMonthValue() == month;
                })
                .map(e -> {
                    Map<String,Object> m = new HashMap<>();
                    m.put("timestamp",     e.getExpenseTime().format(fmt));
                    m.put("type",          "Kassa");
                    m.put("amount",        e.getAmount());
                    m.put("comment",       e.getDescription());
                    return m;
                })
                .collect(Collectors.toList());
        log.info("Found {} cash‑expense records", list.size());
        return list;
    }

    /** Сбор всех card‑расходов за месяц */
    private List<Map<String,Object>> fetchCardExpenses(int year, int month, DateTimeFormatter fmt) {
        var list = cardRepo.findAll().stream()
                .filter(e -> {
                    var t = e.getExpenseTime();
                    return t.getYear() == year && t.getMonthValue() == month;
                })
                .map(e -> {
                    Map<String,Object> m = new HashMap<>();
                    m.put("timestamp",     e.getExpenseTime().format(fmt));
                    m.put("type",          "EC");
                    m.put("amount",        e.getAmount());
                    m.put("comment",       e.getDescription());
                    return m;
                })
                .collect(Collectors.toList());
        log.info("Found {} card‑expense records", list.size());
        return list;
    }


    /**
     * HTML → PDF через Thymeleaf + OpenHTMLToPDF.
     */
    private void renderReport(HttpServletResponse response,
                              String templateName,
                              String title,
                              List<String> columns,
                              List<? extends Map<String, ?>> rows,
                              String filename) throws IOException {
        // Логируем ключевую информацию
        log.debug("renderReport: title='{}', filename='{}', columns={}, rowsCount={}", templateName,
                title, filename, columns, rows.size());

        // Логируем сами столбцы
        log.debug("renderReport: columns details -> {}", columns);


        Context ctx = new Context();
        ctx.setVariable("title", title);
        ctx.setVariable("columns", columns);
        ctx.setVariable("rows", rows);

        // вместо жёстко "report" теперь используем templateName
        String html = thymeleaf.process(templateName, ctx);
        log.trace("renderReport: generated html: {}", html);

        response.setContentType("application/pdf");
        response.setHeader("Content-Disposition", "attachment; filename=\"" + filename + "\"");

        PdfRendererBuilder builder = new PdfRendererBuilder();

        String baseUri = getClass()
                .getResource("/static/")
                .toExternalForm();  // например "file:/.../BOOT-INF/classes/static/"


        builder.withHtmlContent(html, baseUri);
        builder.toStream(response.getOutputStream());
        builder.run();
        log.info("renderReport: PDF sent");
    }
}
