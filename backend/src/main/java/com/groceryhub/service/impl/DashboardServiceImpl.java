package com.groceryhub.service.impl;

import com.groceryhub.dao.CustomerDAO;
import com.groceryhub.dao.OrderDAO;
import com.groceryhub.dao.ProductDAO;
import com.groceryhub.dto.response.DashboardStatsDTO;
import com.groceryhub.enums.OrderStatus;
import com.groceryhub.model.Order;
import com.groceryhub.model.Product;
import com.groceryhub.service.interfaces.IDashboardService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class DashboardServiceImpl implements IDashboardService {

    private final OrderDAO orderDAO;
    private final ProductDAO productDAO;
    private final CustomerDAO customerDAO;

    @Override
    public DashboardStatsDTO getDashboardStats() {
        LocalDateTime startOfDay = LocalDate.now().atStartOfDay();
        LocalDateTime endOfDay = startOfDay.plusDays(1).minusNanos(1);

        Integer todayOrders = orderDAO.countOrders(startOfDay, endOfDay);
        Double todayRevenue = orderDAO.calculateRevenue(startOfDay, endOfDay);
        
        List<Order> pendingOrdersList = orderDAO.findByOrderStatusIn(List.of(OrderStatus.pending, OrderStatus.processing));
        Integer pendingOrders = pendingOrdersList.size();
        
        long activeCustomers = customerDAO.count();

        return DashboardStatsDTO.builder()
                .totalOrdersToday(todayOrders != null ? todayOrders : 0)
                .revenueToday(todayRevenue != null ? todayRevenue : 0.0)
                .pendingOrders(pendingOrders)
                .activeCustomers((int) activeCustomers)
                .build();
    }

    @Override
    public List<Order> getRecentOrders() {
        return orderDAO.findAll(PageRequest.of(0, 10, Sort.by(Sort.Direction.DESC, "orderDate"))).getContent();
    }

    @Override
    public List<Product> getLowStockAlerts() {
        return productDAO.findLowStockProducts();
    }

    @Override
    public Object getRevenueChart(String period) {
        // Mock implementation for charts depending on 'daily', 'weekly', 'monthly'
        // In a real scenario, this would group queries by day/week/month
        return null; // Handle properly in controller for now
    }
}
