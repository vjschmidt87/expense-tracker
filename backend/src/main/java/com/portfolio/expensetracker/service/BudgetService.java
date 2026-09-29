package com.portfolio.expensetracker.service;

import com.portfolio.expensetracker.dto.request.BudgetRequest;
import com.portfolio.expensetracker.dto.response.BudgetResponse;
import com.portfolio.expensetracker.entity.Budget;
import com.portfolio.expensetracker.entity.Category;
import com.portfolio.expensetracker.entity.User;
import com.portfolio.expensetracker.exception.BadRequestException;
import com.portfolio.expensetracker.exception.ResourceNotFoundException;
import com.portfolio.expensetracker.exception.UnauthorizedException;
import com.portfolio.expensetracker.repository.BudgetRepository;
import com.portfolio.expensetracker.repository.CategoryRepository;
import com.portfolio.expensetracker.repository.TransactionRepository;
import com.portfolio.expensetracker.repository.UserRepository;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.math.BigDecimal;
import java.util.List;

@Service
public class BudgetService {

    private final BudgetRepository budgetRepository;
    private final CategoryRepository categoryRepository;
    private final TransactionRepository transactionRepository;
    private final UserRepository userRepository;

    public BudgetService(BudgetRepository budgetRepository, CategoryRepository categoryRepository,
                         TransactionRepository transactionRepository, UserRepository userRepository) {
        this.budgetRepository = budgetRepository;
        this.categoryRepository = categoryRepository;
        this.transactionRepository = transactionRepository;
        this.userRepository = userRepository;
    }

    public List<BudgetResponse> findByMonthYear(String username, Integer month, Integer year) {
        User user = findUser(username);
        return budgetRepository.findByUserIdAndMonthAndYear(user.getId(), month, year).stream()
                .map(b -> {
                    BigDecimal spent = transactionRepository.sumByCategoryAndMonth(
                            user.getId(), b.getCategory().getId(), month, year);
                    return BudgetResponse.from(b, spent);
                }).toList();
    }

    @Transactional
    public BudgetResponse create(BudgetRequest request, String username) {
        User user = findUser(username);
        Category cat = categoryRepository.findById(request.categoryId())
                .orElseThrow(() -> new ResourceNotFoundException("Category not found"));
        if (budgetRepository.findByUserIdAndCategoryIdAndMonthAndYear(
                user.getId(), request.categoryId(), request.month(), request.year()).isPresent()) {
            throw new BadRequestException("Budget already exists for this category and month");
        }
        Budget b = new Budget();
        b.setCategory(cat);
        b.setAmount(request.amount());
        b.setMonth(request.month());
        b.setYear(request.year());
        b.setUser(user);
        budgetRepository.save(b);
        BigDecimal spent = transactionRepository.sumByCategoryAndMonth(
                user.getId(), cat.getId(), request.month(), request.year());
        return BudgetResponse.from(b, spent);
    }

    @Transactional
    public BudgetResponse update(Long id, BudgetRequest request, String username) {
        Budget b = budgetRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Budget not found"));
        if (!b.getUser().getUsername().equals(username)) throw new UnauthorizedException("Not your budget");
        b.setAmount(request.amount());
        budgetRepository.save(b);
        BigDecimal spent = transactionRepository.sumByCategoryAndMonth(
                b.getUser().getId(), b.getCategory().getId(), b.getMonth(), b.getYear());
        return BudgetResponse.from(b, spent);
    }

    @Transactional
    public void delete(Long id, String username) {
        Budget b = budgetRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Budget not found"));
        if (!b.getUser().getUsername().equals(username)) throw new UnauthorizedException("Not your budget");
        budgetRepository.delete(b);
    }

    private User findUser(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found: " + username));
    }
}
