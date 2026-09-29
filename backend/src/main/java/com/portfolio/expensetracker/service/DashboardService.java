package com.portfolio.expensetracker.service;

import com.portfolio.expensetracker.dto.response.DashboardResponse;
import com.portfolio.expensetracker.dto.response.TransactionResponse;
import com.portfolio.expensetracker.entity.Transaction;
import com.portfolio.expensetracker.entity.User;
import com.portfolio.expensetracker.enums.TransactionType;
import com.portfolio.expensetracker.repository.TransactionRepository;
import com.portfolio.expensetracker.repository.UserRepository;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Service
public class DashboardService {

    private final TransactionRepository transactionRepository;
    private final UserRepository userRepository;

    public DashboardService(TransactionRepository transactionRepository, UserRepository userRepository) {
        this.transactionRepository = transactionRepository;
        this.userRepository = userRepository;
    }

    public DashboardResponse getSummary(String username, int month, int year) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        BigDecimal totalIncome = transactionRepository.sumByTypeAndMonth(
                user.getId(), TransactionType.INCOME, month, year);
        BigDecimal totalExpenses = transactionRepository.sumByTypeAndMonth(
                user.getId(), TransactionType.EXPENSE, month, year);
        BigDecimal balance = totalIncome.subtract(totalExpenses);

        List<Transaction> monthlyTransactions = transactionRepository.findByMonthYear(user.getId(), month, year);

        Map<Long, DashboardResponse.CategoryBreakdown> breakdownMap = new LinkedHashMap<>();
        for (Transaction t : monthlyTransactions) {
            if (t.getType() == TransactionType.EXPENSE) {
                breakdownMap.merge(t.getCategory().getId(),
                    new DashboardResponse.CategoryBreakdown(
                        t.getCategory().getId(), t.getCategory().getName(),
                        t.getCategory().getNamePt(), t.getCategory().getIcon(), t.getAmount()),
                    (existing, incoming) -> new DashboardResponse.CategoryBreakdown(
                        existing.categoryId(), existing.categoryName(),
                        existing.categoryNamePt(), existing.categoryIcon(),
                        existing.amount().add(incoming.amount())));
            }
        }

        List<TransactionResponse> recent = monthlyTransactions.stream()
                .limit(10).map(TransactionResponse::from).toList();

        return new DashboardResponse(totalIncome, totalExpenses, balance,
                new ArrayList<>(breakdownMap.values()), recent);
    }
}
