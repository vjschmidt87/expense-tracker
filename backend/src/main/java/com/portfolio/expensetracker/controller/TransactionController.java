package com.portfolio.expensetracker.controller;

import com.portfolio.expensetracker.dto.request.TransactionRequest;
import com.portfolio.expensetracker.dto.response.TransactionResponse;
import com.portfolio.expensetracker.enums.TransactionType;
import com.portfolio.expensetracker.service.TransactionService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/transactions")
public class TransactionController {

    private final TransactionService transactionService;

    public TransactionController(TransactionService transactionService) {
        this.transactionService = transactionService;
    }

    @GetMapping
    public ResponseEntity<List<TransactionResponse>> getAll(
            @AuthenticationPrincipal UserDetails user,
            @RequestParam(required = false) TransactionType type,
            @RequestParam(required = false) Long categoryId,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate,
            @RequestParam(required = false) Integer month,
            @RequestParam(required = false) Integer year) {
        return ResponseEntity.ok(transactionService.findAll(
                user.getUsername(), type, categoryId, startDate, endDate, month, year));
    }

    @GetMapping("/{id}")
    public ResponseEntity<TransactionResponse> getById(@PathVariable Long id,
                                                        @AuthenticationPrincipal UserDetails user) {
        return ResponseEntity.ok(transactionService.findById(id, user.getUsername()));
    }

    @PostMapping
    public ResponseEntity<TransactionResponse> create(@RequestBody TransactionRequest request,
                                                       @AuthenticationPrincipal UserDetails user) {
        return ResponseEntity.ok(transactionService.create(request, user.getUsername()));
    }

    @PutMapping("/{id}")
    public ResponseEntity<TransactionResponse> update(@PathVariable Long id,
                                                       @RequestBody TransactionRequest request,
                                                       @AuthenticationPrincipal UserDetails user) {
        return ResponseEntity.ok(transactionService.update(id, request, user.getUsername()));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id, @AuthenticationPrincipal UserDetails user) {
        transactionService.delete(id, user.getUsername());
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/export/csv")
    public ResponseEntity<String> exportCsv(@AuthenticationPrincipal UserDetails user) {
        String csv = transactionService.exportCsv(user.getUsername());
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=transactions.csv")
                .contentType(MediaType.parseMediaType("text/csv"))
                .body(csv);
    }
}
