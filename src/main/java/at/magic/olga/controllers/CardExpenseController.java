package at.magic.olga.controllers;

import at.magic.olga.dto.CardExpenseDto;
import at.magic.olga.mappers.CardExpenseMapper;
import at.magic.olga.models.CardExpense;
import at.magic.olga.service.CardExpenseService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

/**
 * REST controller for managing card expenses.
 */
@RestController
@RequestMapping("/api/expenses/card")
@Validated
public class CardExpenseController {

    private final CardExpenseService cardExpenseService;
    private final CardExpenseMapper cardExpenseMapper;

    public CardExpenseController(CardExpenseService cardExpenseService,
                                 CardExpenseMapper cardExpenseMapper) {
        this.cardExpenseService = cardExpenseService;
        this.cardExpenseMapper = cardExpenseMapper;
    }


    @GetMapping
    public List<CardExpenseDto> listAll() {
        List<CardExpense> list = cardExpenseService.findAll();
        return cardExpenseMapper.toDtoList(list);
    }


    @PostMapping
    public ResponseEntity<CardExpenseDto> create(
            @RequestBody @Valid CardExpenseDto dto) {
        CardExpense saved = cardExpenseService.create(cardExpenseMapper.toEntity(dto));
        CardExpenseDto out = cardExpenseMapper.toDto(saved);
        return ResponseEntity
                .created(URI.create("/api/expenses/card/" + out.getId()))
                .body(out);
    }

    @GetMapping("/{id}")
    public ResponseEntity<CardExpenseDto> getById(@PathVariable Long id) {
        CardExpense expense = cardExpenseService.findById(id);
        return ResponseEntity.ok(cardExpenseMapper.toDto(expense));
    }

    @PutMapping("/{id}")
    public ResponseEntity<CardExpenseDto> update(@PathVariable Long id,
                                                 @RequestBody @Valid CardExpenseDto dto) {
        CardExpense expense = cardExpenseMapper.toEntity(dto);
        CardExpense updated = cardExpenseService.update(id, expense);

        if (updated == null) {
            return ResponseEntity.notFound().build();
        }
        CardExpenseDto out = cardExpenseMapper.toDto(updated);
        return ResponseEntity.ok(out);
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        cardExpenseService.delete(id);
        return ResponseEntity.noContent().build();
    }

}
