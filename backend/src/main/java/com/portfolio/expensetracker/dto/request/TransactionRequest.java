package com.portfolio.expensetracker.dto.request;

import com.portfolio.expensetracker.enums.RecurrenceType;
import com.portfolio.expensetracker.enums.TransactionType;
import java.math.BigDecimal;
import java.time.LocalDate;

public record TransactionRequest(
    BigDecimal amount,
    String description,
    String descriptionPt,
    TransactionType type,
    Long categoryId,
    LocalDate date,
    Boolean recurring,
    RecurrenceType recurrenceType
) {}
