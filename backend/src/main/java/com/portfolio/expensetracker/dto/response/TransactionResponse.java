package com.portfolio.expensetracker.dto.response;

import com.portfolio.expensetracker.entity.Transaction;
import com.portfolio.expensetracker.enums.RecurrenceType;
import com.portfolio.expensetracker.enums.TransactionType;
import java.math.BigDecimal;
import java.time.LocalDate;

public record TransactionResponse(
    Long id,
    BigDecimal amount,
    String description,
    String descriptionPt,
    TransactionType type,
    Long categoryId,
    String categoryName,
    String categoryNamePt,
    String categoryIcon,
    LocalDate date,
    Boolean recurring,
    RecurrenceType recurrenceType
) {
    public static TransactionResponse from(Transaction t) {
        return new TransactionResponse(
            t.getId(), t.getAmount(), t.getDescription(), t.getDescriptionPt(),
            t.getType(), t.getCategory().getId(), t.getCategory().getName(),
            t.getCategory().getNamePt(), t.getCategory().getIcon(),
            t.getDate(), t.getRecurring(), t.getRecurrenceType());
    }
}
