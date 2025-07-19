package at.magic.olga.controllers;

import at.magic.olga.dto.view.DailyProfitLossDto;
import at.magic.olga.mappers.view.DailyProfitLossMapper;
import at.magic.olga.service.ReportingService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/api/export/debug")
public class ExportViewController {

    private final ReportingService reportingService;
    private final DailyProfitLossMapper dplMapper;

    public ExportViewController(ReportingService reportingService,
                                DailyProfitLossMapper dplMapper) {
        this.reportingService = reportingService;
        this.dplMapper         = dplMapper;
    }

    /**
     * HTML‑отладка ежедневного отчёта.
     */
    @GetMapping("/daily.html")
    public String debugDaily(Model model) {
        // получаем сущности и конвертируем в DTO
        List<DailyProfitLossDto> dtos = dplMapper.toDtoList(
                reportingService.getDailyProfitLoss()
        );

        model.addAttribute("title", "Daily Profit & Loss (DEBUG)");
        model.addAttribute("columns",
                List.of("day", "salesAmount", "cardExpenses", "cashExpenses", "netProfit"));
        model.addAttribute("rows", dtos.stream()
                .map(d -> Map.<String,String>of(
                        "day",          d.getDay().toString(),
                        "salesAmount",  d.getSalesAmount().toString(),
                        "cardExpenses", d.getCardExpenses().toString(),
                        "cashExpenses", d.getCashExpenses().toString(),
                        "netProfit",    d.getNetProfit().toString()
                ))
                .collect(Collectors.toList()));

        return "report";  // имя Thymeleaf‑шаблона report.html
    }
}
