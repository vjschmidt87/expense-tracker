package com.portfolio.expensetracker.dto.response;

import com.portfolio.expensetracker.entity.Budget;
import java.math.BigDecimal;

public record BudgetResponse(
    Long id,
    Long categoryId,
    String categoryName,
    String categoryNamePt,
    String categoryIcon,
    BigDecimal amount,
    BigDecimal spent,
    Integer month,
    Integer year
) {
    public static BudgetResponse from(Budget b, BigDecimal spent) {
        return new BudgetResponse(
            b.getId(), b.getCategory().getId(), b.getCategory().getName(),
            b.getCategory().getNamePt(), b.getCategory().getIcon(),
            b.getAmount(), spent, b.getMonth(), b.getYear());
    }
}
