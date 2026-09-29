package com.portfolio.expensetracker.controller;

import com.portfolio.expensetracker.dto.request.BudgetRequest;
import com.portfolio.expensetracker.dto.response.BudgetResponse;
import com.portfolio.expensetracker.service.BudgetService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/budgets")
public class BudgetController {

    private final BudgetService budgetService;

    public BudgetController(BudgetService budgetService) {
        this.budgetService = budgetService;
    }

    @GetMapping
    public ResponseEntity<List<BudgetResponse>> getByMonthYear(
            @AuthenticationPrincipal UserDetails user,
            @RequestParam Integer month,
            @RequestParam Integer year) {
        return ResponseEntity.ok(budgetService.findByMonthYear(user.getUsername(), month, year));
    }

    @PostMapping
    public ResponseEntity<BudgetResponse> create(@RequestBody BudgetRequest request,
                                                  @AuthenticationPrincipal UserDetails user) {
        return ResponseEntity.ok(budgetService.create(request, user.getUsername()));
    }

    @PutMapping("/{id}")
    public ResponseEntity<BudgetResponse> update(@PathVariable Long id, @RequestBody BudgetRequest request,
                                                  @AuthenticationPrincipal UserDetails user) {
        return ResponseEntity.ok(budgetService.update(id, request, user.getUsername()));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id, @AuthenticationPrincipal UserDetails user) {
        budgetService.delete(id, user.getUsername());
        return ResponseEntity.noContent().build();
    }
}
