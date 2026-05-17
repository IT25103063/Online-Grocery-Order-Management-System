package com.groceryhub.controller;

import com.groceryhub.dto.response.ApiResponse;
import com.groceryhub.dto.response.DashboardStatsDTO;
import com.groceryhub.model.Order;
import com.groceryhub.model.Product;
import com.groceryhub.service.interfaces.IDashboardService;
import com.groceryhub.util.ResponseUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/dashboard")
@RequiredArgsConstructor
public class DashboardController {

    private final IDashboardService dashboardService;

    @GetMapping("/stats")
    public ResponseEntity<ApiResponse<DashboardStatsDTO>> getStats() {
        return ResponseUtil.success(dashboardService.getDashboardStats());
    }

    @GetMapping("/recent-orders")
    public ResponseEntity<ApiResponse<List<Order>>> getRecentOrders() {
        return ResponseUtil.success(dashboardService.getRecentOrders());
    }

    @GetMapping("/low-stock-alerts")
    public ResponseEntity<ApiResponse<List<Product>>> getLowStockAlerts() {
        return ResponseUtil.success(dashboardService.getLowStockAlerts());
    }

    @GetMapping("/charts")
    public ResponseEntity<ApiResponse<Object>> getCharts(@RequestParam(defaultValue = "weekly") String period) {
        return ResponseUtil.success(dashboardService.getRevenueChart(period));
    }
}
