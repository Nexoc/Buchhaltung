package at.magic.olga.controllers;

import at.magic.olga.dto.SaleDto;
import at.magic.olga.mappers.SaleMapper;
import at.magic.olga.models.Sale;
import at.magic.olga.service.SaleService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

/**
 * REST controller for managing sales.
 */
@RestController
@RequestMapping("/api/sales")
@Validated
public class SaleController {

    private final SaleService saleService;
    private final SaleMapper saleMapper;

    public SaleController(SaleService saleService, SaleMapper saleMapper) {
        this.saleService = saleService;
        this.saleMapper = saleMapper;
    }

    @GetMapping
    public List<SaleDto> listAll() {
        return saleMapper.toDtoList(saleService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<SaleDto> getOne(@PathVariable Long id) {
        Sale sale = saleService.findById(id);
        return ResponseEntity.ok(saleMapper.toDto(sale));
    }

    @PostMapping
    public ResponseEntity<SaleDto> create(@RequestBody @Valid SaleDto dto) {
        Sale saved = saleService.recordSale(
                dto.getProductId(),
                dto.getQuantity(),
                dto.getPaymentMethod(),
                dto.getComment()
        );
        SaleDto out = saleMapper.toDto(saved);
        return ResponseEntity
                .created(URI.create("/api/sales/" + out.getId()))
                .body(out);
    }

    @PutMapping("/{id}")
    public ResponseEntity<SaleDto> update(@PathVariable Long id,
                                          @RequestBody @Valid SaleDto dto) {
        Sale updated = saleMapper.toEntity(dto);
        updated.setId(id);
        Sale result = saleService.update(id, updated);
        return ResponseEntity.ok(saleMapper.toDto(result));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        saleService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
