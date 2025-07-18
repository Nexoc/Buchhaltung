package at.magic.olga.service;

import at.magic.olga.models.CashExpense;
import at.magic.olga.repositories.CashExpenseRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CashExpenseService {
    private final CashExpenseRepository cashRepo;

    public CashExpenseService(CashExpenseRepository cashRepo) {
        this.cashRepo = cashRepo;
    }

    public List<CashExpense> findAll() {
        return cashRepo.findAll();
    }

    public CashExpense findById(Long id) {
        return cashRepo.findById(id).orElse(null);
    }

    public CashExpense create(CashExpense expense) {
        return cashRepo.save(expense);
    }

    public CashExpense update(Long id, CashExpense expense) {
        if (!cashRepo.existsById(id)) {
            return null;
        }
        expense.setId(id);
        return cashRepo.save(expense);
    }

    public void delete(Long id) {
        cashRepo.deleteById(id);
    }
}

