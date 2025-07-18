package at.magic.olga.service;

import at.magic.olga.models.Instruction;
import at.magic.olga.repositories.InstructionRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
public class InstructionService {
    private final InstructionRepository instructionRepo;

    public InstructionService(InstructionRepository instructionRepo) {
        this.instructionRepo = instructionRepo;
    }

    public List<Instruction> findAll() {
        return instructionRepo.findAll();
    }

    public Instruction findById(Integer id) {
        return instructionRepo.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Instruction not found: " + id));
    }

    public Instruction create(Instruction instruction) {
        return instructionRepo.save(instruction);
    }

    @Transactional
    public Instruction update(Integer id, Instruction updated) {
        Instruction existing = findById(id);
        existing.setFilename(updated.getFilename());
        existing.setDescription(updated.getDescription());
        existing.setProduct(updated.getProduct());
        return instructionRepo.save(existing);
    }

    public void delete(Integer id) {
        instructionRepo.deleteById(id);
    }
}