package com.groceryhub.service.interfaces;

import com.groceryhub.dto.response.DashboardStatsDTO;
import com.groceryhub.model.Order;
import com.groceryhub.model.Product;

import java.util.List;

public interface IDashboardService {
    DashboardStatsDTO getDashboardStats();
    List<Order> getRecentOrders();
    List<Product> getLowStockAlerts();
    Object getRevenueChart(String period);
}
