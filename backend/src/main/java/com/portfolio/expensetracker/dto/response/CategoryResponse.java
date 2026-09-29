package com.portfolio.expensetracker.dto.response;

import com.portfolio.expensetracker.entity.Category;
import com.portfolio.expensetracker.enums.TransactionType;

public record CategoryResponse(Long id, String name, String namePt, String icon, TransactionType type, boolean isSystem) {
    public static CategoryResponse from(Category c) {
        return new CategoryResponse(c.getId(), c.getName(), c.getNamePt(), c.getIcon(), c.getType(), c.getUser() == null);
    }
}
