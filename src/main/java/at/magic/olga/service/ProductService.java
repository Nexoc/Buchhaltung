package at.magic.olga.service;

import at.magic.olga.dto.ProductDto;
import at.magic.olga.models.Instruction;
import at.magic.olga.models.Product;
import at.magic.olga.repositories.CategoryRepository;
import at.magic.olga.repositories.InstructionRepository;
import at.magic.olga.repositories.ProductRepository;
import at.magic.olga.repositories.SaleRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProductService {

    private final ProductRepository productRepo;
    private final CategoryRepository categoryRepo;
    private final InstructionRepository instructionRepo;
    private SaleRepository saleRepository;

    public ProductService(ProductRepository productRepo,
                          CategoryRepository categoryRepo,
                          InstructionRepository instructionRepo,
                          SaleRepository saleRepository) {
        this.productRepo = productRepo;
        this.categoryRepo = categoryRepo;
        this.instructionRepo = instructionRepo;
        this.saleRepository = saleRepository;
    }

    public List<Product> findAll() {
        return productRepo.findAll();
    }

    public Product findById(Integer id) {
        return productRepo.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Product not found: " + id));
    }

    public Product create(Product product) {
        return productRepo.save(product);
    }

    @Transactional
    public Product update(Integer id, ProductDto dto) {
        Product existing = productRepo.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Product not found: " + id));

        existing.setName(dto.getName());
        existing.setPrice(dto.getPrice());
        existing.setStock(dto.getStock());
        existing.setCategory(categoryRepo.getReferenceById(dto.getCategoryId()));
        existing.setDescription(dto.getDescription());
        existing.setAddedDate(dto.getAddedDate());
        existing.setImagePath(dto.getImagePath());

        // Обработка инструкций
        if (dto.getInstructions() != null) {
            existing.getInstructions().clear();

            List<Instruction> instructions = dto.getInstructions().stream()
                    .map(i -> instructionRepo.findById(i.getId())
                            .orElseThrow(() -> new EntityNotFoundException("Instruction not found: " + i.getId())))
                    .toList();

            existing.getInstructions().addAll(instructions);
        }


        return productRepo.save(existing);
    }

    @Transactional
    public void delete(Integer id) {
        Product prod = productRepo.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Produkt nicht gefunden mit ID: " + id));

        // Проверка наличия продаж
        if (!saleRepository.findByProductId(id).isEmpty()) {
            throw new IllegalStateException("Produkt kann nicht gelöscht werden – es existieren bereits Verkäufe.");
        }

        productRepo.delete(prod);
    }
}
