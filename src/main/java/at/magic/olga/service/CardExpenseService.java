package at.magic.olga.service;

import at.magic.olga.models.CardExpense;
import at.magic.olga.repositories.CardExpenseRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CardExpenseService {
    private final CardExpenseRepository cardRepo;

    public CardExpenseService(CardExpenseRepository cardRepo) {
        this.cardRepo = cardRepo;
    }

    public List<CardExpense> findAll() {
        return cardRepo.findAll();
    }

    public CardExpense findById(Long id) {
        return cardRepo.findById(id).orElse(null);
    }

    public CardExpense create(CardExpense expense) {
        return cardRepo.save(expense);
    }

    public CardExpense update(Long id, CardExpense expense) {
        if (!cardRepo.existsById(id)) {
            return null;
        }
        expense.setId(id);
        return cardRepo.save(expense);
    }

    public void delete(Long id) {
        cardRepo.deleteById(id);
    }

}
