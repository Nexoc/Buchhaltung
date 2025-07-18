package at.magic.olga.controllers;

import at.magic.olga.dto.CashExpenseDto;
import at.magic.olga.mappers.CashExpenseMapper;
import at.magic.olga.models.CashExpense;
import at.magic.olga.service.CashExpenseService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

/**
 * REST controller for managing cash expenses.
 */
@RestController
@RequestMapping("/api/expenses/cash")
@Validated
public class CashExpenseController {

    private final CashExpenseService cashExpenseService;
    private final CashExpenseMapper cashExpenseMapper;

    public CashExpenseController(CashExpenseService cashExpenseService,
                                 CashExpenseMapper cashExpenseMapper) {
        this.cashExpenseService = cashExpenseService;
        this.cashExpenseMapper = cashExpenseMapper;
    }

    @GetMapping
    public List<CashExpenseDto> listAll() {
        List<CashExpense> list = cashExpenseService.findAll();
        return cashExpenseMapper.toDtoList(list);
    }

    @GetMapping("/{id}")
    public ResponseEntity<CashExpenseDto> getById(@PathVariable Long id) {
        CashExpense expense = cashExpenseService.findById(id);
        if (expense == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(cashExpenseMapper.toDto(expense));
    }

    @PostMapping
    public ResponseEntity<CashExpenseDto> create(@RequestBody @Valid CashExpenseDto dto) {
        CashExpense saved = cashExpenseService.create(cashExpenseMapper.toEntity(dto));
        CashExpenseDto out = cashExpenseMapper.toDto(saved);
        return ResponseEntity
                .created(URI.create("/api/expenses/cash/" + out.getId()))
                .body(out);
    }

    @PutMapping("/{id}")
    public ResponseEntity<CashExpenseDto> update(@PathVariable Long id,
                                                 @RequestBody @Valid CashExpenseDto dto) {
        CashExpense updated = cashExpenseService.update(id, cashExpenseMapper.toEntity(dto));
        if (updated == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(cashExpenseMapper.toDto(updated));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        cashExpenseService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
