package com.portfolio.expensetracker.controller;

import com.portfolio.expensetracker.dto.response.DashboardResponse;
import com.portfolio.expensetracker.service.DashboardService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/dashboard")
public class DashboardController {

    private final DashboardService dashboardService;

    public DashboardController(DashboardService dashboardService) {
        this.dashboardService = dashboardService;
    }

    @GetMapping("/summary")
    public ResponseEntity<DashboardResponse> summary(
            @AuthenticationPrincipal UserDetails user,
            @RequestParam Integer month,
            @RequestParam Integer year) {
        return ResponseEntity.ok(dashboardService.getSummary(user.getUsername(), month, year));
    }
}
