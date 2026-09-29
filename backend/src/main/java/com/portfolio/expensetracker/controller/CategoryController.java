package com.portfolio.expensetracker.controller;

import com.portfolio.expensetracker.dto.request.CategoryRequest;
import com.portfolio.expensetracker.dto.response.CategoryResponse;
import com.portfolio.expensetracker.service.CategoryService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/categories")
public class CategoryController {

    private final CategoryService categoryService;

    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @GetMapping
    public ResponseEntity<List<CategoryResponse>> getAll(@AuthenticationPrincipal UserDetails user) {
        return ResponseEntity.ok(categoryService.findAll(user.getUsername()));
    }

    @PostMapping
    public ResponseEntity<CategoryResponse> create(@RequestBody CategoryRequest request,
                                                    @AuthenticationPrincipal UserDetails user) {
        return ResponseEntity.ok(categoryService.create(request, user.getUsername()));
    }

    @PutMapping("/{id}")
    public ResponseEntity<CategoryResponse> update(@PathVariable Long id, @RequestBody CategoryRequest request,
                                                    @AuthenticationPrincipal UserDetails user) {
        return ResponseEntity.ok(categoryService.update(id, request, user.getUsername()));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id, @AuthenticationPrincipal UserDetails user) {
        categoryService.delete(id, user.getUsername());
        return ResponseEntity.noContent().build();
    }
}
