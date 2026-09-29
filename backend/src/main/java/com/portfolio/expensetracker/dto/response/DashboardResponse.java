package com.portfolio.expensetracker.dto.response;

import java.math.BigDecimal;
import java.util.List;

public record DashboardResponse(
    BigDecimal totalIncome,
    BigDecimal totalExpenses,
    BigDecimal balance,
    List<CategoryBreakdown> categoryBreakdown,
    List<TransactionResponse> recentTransactions
) {
    public record CategoryBreakdown(
        Long categoryId,
        String categoryName,
        String categoryNamePt,
        String categoryIcon,
        BigDecimal amount
    ) {}
}
