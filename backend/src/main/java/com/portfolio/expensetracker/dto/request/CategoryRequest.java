package com.portfolio.expensetracker.dto.request;

import com.portfolio.expensetracker.enums.TransactionType;

public record CategoryRequest(String name, String namePt, String icon, TransactionType type) {}
