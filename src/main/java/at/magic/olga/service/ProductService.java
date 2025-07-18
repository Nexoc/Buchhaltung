package at.magic.olga.service;

import at.magic.olga.dto.ProductDto;
import at.magic.olga.models.Instruction;
import at.magic.olga.models.Product;
import at.magic.olga.repositories.CategoryRepository;
import at.magic.olga.repositories.InstructionRepository;
import at.magic.olga.repositories.ProductRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ProductService {

    private final ProductRepository productRepo;
    private final CategoryRepository categoryRepo;
    private final InstructionRepository instructionRepo;

    public ProductService(ProductRepository productRepo,
                          CategoryRepository categoryRepo,
                          InstructionRepository instructionRepo) {
        this.productRepo = productRepo;
        this.categoryRepo = categoryRepo;
        this.instructionRepo = instructionRepo;
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
            if (dto.getInstructions().isEmpty()) {
                existing.getInstructions().clear();
            } else {
                List<Instruction> instructions = dto.getInstructions().stream()
                        .map(i -> instructionRepo.findById(i.getId())
                                .orElseThrow(() -> new EntityNotFoundException("Instruction not found: " + i.getId())))
                        .toList();
                existing.setInstructions(instructions);
            }
        }

        return productRepo.save(existing);
    }

    public void delete(Integer id) {
        productRepo.deleteById(id);
    }
}
