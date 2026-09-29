package com.portfolio.expensetracker.dto.request;

import java.math.BigDecimal;

public record BudgetRequest(Long categoryId, BigDecimal amount, Integer month, Integer year) {}
