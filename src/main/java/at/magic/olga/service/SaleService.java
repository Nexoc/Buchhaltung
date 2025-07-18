package at.magic.olga.service;

import at.magic.olga.models.Product;
import at.magic.olga.models.Sale;
import at.magic.olga.repositories.ProductRepository;
import at.magic.olga.repositories.SaleRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class SaleService {
    private final SaleRepository saleRepo;
    private final ProductRepository productRepo;

    public SaleService(SaleRepository saleRepo, ProductRepository productRepo) {
        this.saleRepo = saleRepo;
        this.productRepo = productRepo;
    }

    public List<Sale> findAll() {
        return saleRepo.findAll();
    }

    public Sale findById(Long id) {
        return saleRepo.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Sale not found: " + id));
    }

    /**
     * Record a sale and update product stock atomically.
     */
    @Transactional
    public Sale recordSale(Integer productId, int quantity, Sale.PaymentMethod method, String comment) {
        Product product = productRepo.findById(productId)
                .orElseThrow(() -> new EntityNotFoundException("Product not found: " + productId));
        product.setStock(product.getStock() - quantity);
        productRepo.save(product);

        Sale sale = new Sale();
        sale.setProduct(product);
        sale.setQuantity(quantity);
        sale.setPaymentMethod(method);
        sale.setSaleTime(LocalDateTime.now());
        sale.setComment(comment);
        return saleRepo.save(sale);
    }
    /**
     * Update an existing sale by ID.
     */
    @Transactional
    public Sale update(Long id, Sale updatedSale) {
        Sale existing = saleRepo.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Sale not found: " + id));

        // Обновляем поля
        existing.setQuantity(updatedSale.getQuantity());
        existing.setPaymentMethod(updatedSale.getPaymentMethod());
        existing.setComment(updatedSale.getComment());
        existing.setSaleTime(updatedSale.getSaleTime());

        // Обновление продукта (если был передан другой ID)
        if (updatedSale.getProduct() != null &&
                !updatedSale.getProduct().getId().equals(existing.getProduct().getId())) {

            Product newProduct = productRepo.findById(updatedSale.getProduct().getId())
                    .orElseThrow(() -> new EntityNotFoundException("Product not found: " + updatedSale.getProduct().getId()));
            existing.setProduct(newProduct);
        }

        return saleRepo.save(existing);
    }

    /**
     * Delete sale by ID.
     */
    public void delete(Long id) {
        if (!saleRepo.existsById(id)) {
            throw new EntityNotFoundException("Sale not found: " + id);
        }
        saleRepo.deleteById(id);
    }
}