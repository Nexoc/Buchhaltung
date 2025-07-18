package at.magic.olga.controllers;

import at.magic.olga.dto.ProductDto;
import at.magic.olga.mappers.ProductMapper;
import at.magic.olga.models.Product;
import at.magic.olga.service.FileService;
import at.magic.olga.service.ProductService;
import jakarta.validation.Valid;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.URI;
import java.nio.file.Files;
import java.util.List;

/**
 * REST controller for managing products.
 */
@Validated
@RestController
@RequestMapping("/api/products")
public class ProductController {

    private final ProductService productService;
    private final ProductMapper productMapper;
    private final FileService fileService;

    public ProductController(ProductService productService,
                             ProductMapper productMapper,
                             FileService fileService) {
        this.productService = productService;
        this.productMapper = productMapper;
        this.fileService = fileService;
    }

    /**
     * Get all products.
     */
    @GetMapping
    public List<ProductDto> listAll() {
        List<Product> products = productService.findAll();
        return productMapper.toDtoList(products);
    }

    /**
     * Get product by ID.
     */
    @GetMapping("/{id}")
    public ResponseEntity<ProductDto> getOne(@PathVariable Integer id) {
        Product product = productService.findById(id);
        if (product == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(productMapper.toDto(product));
    }


    /**
     * Create a new product.
     */
    @PostMapping
    public ResponseEntity<ProductDto> create(@RequestBody @Valid ProductDto dto) {
        Product product = productMapper.toEntity(dto);
        Product saved = productService.create(product);
        URI location = URI.create("/api/products/" + saved.getId());
        return ResponseEntity.created(location)
                .body(productMapper.toDto(saved));
    }

    /**
     * Update an existing product by ID.
     */
    @PutMapping("/{id}")
    public ResponseEntity<ProductDto> update(@PathVariable Integer id,
                                             @RequestBody @Valid ProductDto dto) {
        Product updated = productService.update(id, dto);
        return ResponseEntity.ok(productMapper.toDto(updated));
    }

    /**
     * Delete product by ID.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Integer id) {
        productService.delete(id);
        return ResponseEntity.noContent().build();
    }

    /**
     * Upload product image and update product.
     */
    @PostMapping("/{id}/image")
    public ResponseEntity<String> uploadImage(@PathVariable Integer id,
                                              @RequestParam("file") MultipartFile file) throws IOException {
        Product product = productService.findById(id);
        if (product == null) {
            return ResponseEntity.notFound().build();
        }

        String relativePath = fileService.store(file, "images");

        ProductDto dto = productMapper.toDto(product);
        dto.setImagePath(relativePath);

        productService.update(id, dto);
        return ResponseEntity.ok(relativePath);
    }



    /**
     * Serve product image by filename.
     */
    @GetMapping("/image/{filename:.+}")
    public ResponseEntity<Resource> serveImage(@PathVariable String filename) throws IOException {
        Resource resource = fileService.load("images/" + filename);
        String contentType = Files.probeContentType(resource.getFile().toPath());
        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(contentType != null ? contentType : "application/octet-stream"))
                .body(resource);
    }
}
