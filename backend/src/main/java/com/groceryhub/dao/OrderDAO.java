package com.groceryhub.dao;

import com.groceryhub.enums.OrderStatus;
import com.groceryhub.model.Order;
import com.groceryhub.util.TextFileDatabase;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Repository
@RequiredArgsConstructor
@SuppressWarnings("null")
public class OrderDAO {

    private final TextFileDatabase db;
    private final String FILE_NAME = "orders";

    public List<Order> findAll() {
        return db.loadData(FILE_NAME, Order.class);
    }
    
    public Page<Order> findAll(Pageable pageable) {
        List<Order> all = findAll().stream()
            .sorted(Comparator.comparing(Order::getOrderDate).reversed())
            .collect(Collectors.toList());
        int start = (int) pageable.getOffset();
        int end = Math.min((start + pageable.getPageSize()), all.size());
        if (start > all.size()) return new PageImpl<>(List.of(), pageable, all.size());
        return new PageImpl<>(all.subList(start, end), pageable, all.size());
    }

    public Optional<Order> findById(Integer id) {
        return findAll().stream().filter(o -> o.getOrderId().equals(id)).findFirst();
    }

    public Optional<Order> findByOrderNumber(String orderNumber) {
        return findAll().stream().filter(o -> o.getOrderNumber().equals(orderNumber)).findFirst();
    }

    public List<Order> findByCustomer_CustomerId(Integer customerId) {
        return findAll().stream().filter(o -> o.getCustomer() != null && customerId.equals(o.getCustomer().getCustomerId())).collect(Collectors.toList());
    }

    public Page<Order> filterOrders(OrderStatus status, String search, LocalDateTime startDate, LocalDateTime endDate, Pageable pageable) {
        List<Order> all = findAll().stream()
            .filter(o -> status == null || o.getOrderStatus() == status)
            .filter(o -> search == null || o.getOrderNumber().contains(search) || (o.getCustomer() != null && o.getCustomer().getFullName().toLowerCase().contains(search.toLowerCase())))
            .filter(o -> startDate == null || o.getOrderDate().compareTo(startDate) >= 0)
            .filter(o -> endDate == null || o.getOrderDate().compareTo(endDate) <= 0)
            .collect(Collectors.toList());
        
        int start = (int) pageable.getOffset();
        int end = Math.min((start + pageable.getPageSize()), all.size());
        if (start > all.size()) return new PageImpl<>(List.of(), pageable, all.size());
        return new PageImpl<>(all.subList(start, end), pageable, all.size());
    }

    public List<Order> findByOrderStatusIn(List<OrderStatus> statuses) {
        return findAll().stream().filter(o -> statuses.contains(o.getOrderStatus())).collect(Collectors.toList());
    }

    public Double calculateRevenue(LocalDateTime startDate, LocalDateTime endDate) {
        return findAll().stream()
            .filter(o -> o.getOrderStatus() != OrderStatus.cancelled)
            .filter(o -> o.getOrderDate().compareTo(startDate) >= 0 && o.getOrderDate().compareTo(endDate) <= 0)
            .mapToDouble(o -> o.getGrandTotal() != null ? o.getGrandTotal().doubleValue() : 0.0)
            .sum();
    }

    public Integer countOrders(LocalDateTime startDate, LocalDateTime endDate) {
        return (int) findAll().stream()
            .filter(o -> o.getOrderDate().compareTo(startDate) >= 0 && o.getOrderDate().compareTo(endDate) <= 0)
            .count();
    }

    public Order save(Order order) {
        List<Order> orders = findAll();
        if (order.getOrderId() == null) {
            order.setOrderId(orders.size() > 0 ? orders.stream().mapToInt(Order::getOrderId).max().orElse(0) + 1 : 1);
            orders.add(order);
        } else {
            for (int i = 0; i < orders.size(); i++) {
                if (orders.get(i).getOrderId().equals(order.getOrderId())) {
                    orders.set(i, order);
                    break;
                }
            }
        }
        db.saveData(FILE_NAME, orders);
        return order;
    }
}
