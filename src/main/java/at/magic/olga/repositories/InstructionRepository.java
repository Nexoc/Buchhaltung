package at.magic.olga.repositories;

import at.magic.olga.models.Instruction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Repository for Instruction entities.
 */
@Repository
public interface InstructionRepository extends JpaRepository<Instruction, Integer> {
}
