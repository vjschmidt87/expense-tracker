package com.portfolio.expensetracker.repository;

import com.portfolio.expensetracker.entity.Category;
import com.portfolio.expensetracker.enums.TransactionType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.List;

public interface CategoryRepository extends JpaRepository<Category, Long> {

    @Query("SELECT c FROM Category c WHERE c.user IS NULL OR c.user.id = :userId")
    List<Category> findAllForUser(@Param("userId") Long userId);

    List<Category> findByType(TransactionType type);

    @Query("SELECT c FROM Category c WHERE (c.user IS NULL OR c.user.id = :userId) AND c.type = :type")
    List<Category> findByTypeForUser(@Param("type") TransactionType type, @Param("userId") Long userId);
}
