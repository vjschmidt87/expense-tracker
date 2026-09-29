package com.portfolio.expensetracker.repository;

import com.portfolio.expensetracker.entity.Transaction;
import com.portfolio.expensetracker.enums.TransactionType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.time.LocalDate;
import java.util.List;

public interface TransactionRepository extends JpaRepository<Transaction, Long> {

    @Query("SELECT t FROM Transaction t WHERE t.user.id = :userId " +
           "AND (:type IS NULL OR t.type = :type) " +
           "AND (:categoryId IS NULL OR t.category.id = :categoryId) " +
           "AND (:startDate IS NULL OR t.date >= :startDate) " +
           "AND (:endDate IS NULL OR t.date <= :endDate) " +
           "ORDER BY t.date DESC")
    List<Transaction> findByFilters(
            @Param("userId") Long userId,
            @Param("type") TransactionType type,
            @Param("categoryId") Long categoryId,
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate);

    @Query("SELECT t FROM Transaction t WHERE t.user.id = :userId " +
           "AND MONTH(t.date) = :month AND YEAR(t.date) = :year " +
           "ORDER BY t.date DESC")
    List<Transaction> findByMonthYear(
            @Param("userId") Long userId,
            @Param("month") int month,
            @Param("year") int year);

    @Query("SELECT t FROM Transaction t WHERE t.user.id = :userId ORDER BY t.date DESC")
    List<Transaction> findAllByUserOrderByDateDesc(@Param("userId") Long userId);

    @Query("SELECT COALESCE(SUM(t.amount), 0) FROM Transaction t WHERE t.user.id = :userId " +
           "AND t.type = :type AND MONTH(t.date) = :month AND YEAR(t.date) = :year")
    java.math.BigDecimal sumByTypeAndMonth(
            @Param("userId") Long userId,
            @Param("type") TransactionType type,
            @Param("month") int month,
            @Param("year") int year);

    @Query("SELECT COALESCE(SUM(t.amount), 0) FROM Transaction t WHERE t.user.id = :userId " +
           "AND t.type = 'EXPENSE' AND t.category.id = :categoryId " +
           "AND MONTH(t.date) = :month AND YEAR(t.date) = :year")
    java.math.BigDecimal sumByCategoryAndMonth(
            @Param("userId") Long userId,
            @Param("categoryId") Long categoryId,
            @Param("month") int month,
            @Param("year") int year);
}
