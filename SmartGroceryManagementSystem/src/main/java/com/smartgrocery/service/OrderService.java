package com.smartgrocery.service;

import com.smartgrocery.dao.OrderDao;
import com.smartgrocery.dao.ProductDao;
import com.smartgrocery.model.*;

import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

public class OrderService {

    private OrderDao orderDao;
    private ProductDao productDao;

    public OrderService() {
        this.orderDao = new OrderDao();
        this.productDao = new ProductDao();
    }

    public List<Order> getAllOrders() {
        return orderDao.findAll();
    }

    public List<Order> getOrdersByUser(String userId) {
        return orderDao.findByUserId(userId);
    }

    public Order getOrderById(String id) {
        return orderDao.findById(id);
    }

    public boolean updateOrderStatus(String id, String newStatus) {
        Order order = orderDao.findById(id);
        if (order != null) {
            order.setStatus(newStatus);
            return orderDao.update(order);
        }
        return false;
    }

    // NEW: Get Order Statistics
    public Map<String, Object> getOrderStatistics() {
        List<Order> orders = orderDao.findAll();
        Map<String, Object> stats = new HashMap<>();

        int total = orders.size();
        int pending = 0;
        int completed = 0;
        int cancelled = 0;
        double totalRevenue = 0;

        for (Order o : orders) {
            switch (o.getStatus()) {
                case "PENDING": pending++; break;
                case "COMPLETED":
                    completed++;
                    totalRevenue += o.calculateGrandTotal();
                    break;
                case "CANCELLED": cancelled++; break;
            }
        }

        stats.put("total", total);
        stats.put("pending", pending);
        stats.put("completed", completed);
        stats.put("cancelled", cancelled);
        stats.put("revenue", totalRevenue);

        return stats;
    }

    // NEW: Search Orders with filters
    public List<Order> searchOrders(String searchTerm, String statusFilter, String dateFrom, String dateTo) {
        List<Order> all = orderDao.findAll();
        return all.stream()
                .filter(order -> {
                    if (searchTerm != null && !searchTerm.isEmpty()) {
                        if (!order.getId().toLowerCase().contains(searchTerm.toLowerCase()) &&
                                !order.getUserId().toLowerCase().contains(searchTerm.toLowerCase())) {
                            return false;
                        }
                    }
                    if (statusFilter != null && !statusFilter.isEmpty() && !"ALL".equals(statusFilter)) {
                        if (!order.getStatus().equals(statusFilter)) {
                            return false;
                        }
                    }
                    if (dateFrom != null && !dateFrom.isEmpty()) {
                        if (order.getOrderDate().compareTo(dateFrom) < 0) return false;
                    }
                    if (dateTo != null && !dateTo.isEmpty()) {
                        if (order.getOrderDate().compareTo(dateTo + " 23:59") > 0) return false;
                    }
                    return true;
                })
                .collect(Collectors.toList());
    }

    // NEW: Bulk update status
    public boolean bulkUpdateStatus(List<String> orderIds, String newStatus) {
        boolean allSuccess = true;
        for (String id : orderIds) {
            boolean success = updateOrderStatus(id, newStatus);
            if (!success) allSuccess = false;
        }
        return allSuccess;
    }

    // NEW: Get orders with pagination
    public List<Order> getOrdersByPage(int page, int size) {
        List<Order> all = orderDao.findAll();
        int start = (page - 1) * size;
        int end = Math.min(start + size, all.size());
        if (start >= all.size()) return new ArrayList<>();
        return all.subList(start, end);
    }

    // NEW: Get total pages
    public int getTotalPages(int size) {
        int total = orderDao.findAll().size();
        return (int) Math.ceil((double) total / size);
    }

    public String placeOrder(String userId, String type, String[] productIds, String[] quantities) {
        if (productIds == null || productIds.length == 0) return "Cart is empty.";

        Order newOrder;
        String dateStr = new SimpleDateFormat("yyyy-MM-dd HH:mm").format(new Date());

        if ("DELIVERY".equals(type)) {
            newOrder = new DeliveryOrder(null, userId, dateStr, "PENDING", 1800);
        } else {
            newOrder = new PickupOrder(null, userId, dateStr, "PENDING");
        }

        for (int i = 0; i < productIds.length; i++) {
            String pid = productIds[i];
            int qty;
            try {
                qty = Integer.parseInt(quantities[i]);
            } catch (NumberFormatException e) { continue; }

            if (qty <= 0) continue;

            Product p = productDao.findById(pid);
            if (p == null) return "Product " + pid + " not found.";
            if (p.getStock() < qty) return "Insufficient stock for " + p.getName() + " (Available: " + p.getStock() + ")";

            p.setStock(p.getStock() - qty);
            productDao.update(p);

            newOrder.addItem(new OrderItem(p.getId(), p.getName(), qty, p.getPrice()));
        }

        if (newOrder.getItems().isEmpty()) {
            return "No valid items selected.";
        }

        boolean saved = orderDao.save(newOrder);
        return saved ? "SUCCESS" : "Failed to save order.";
    }

    public boolean cancelOrder(String orderId) {
        Order order = orderDao.findById(orderId);
        if (order != null && "PENDING".equals(order.getStatus())) {
            order.setStatus("CANCELLED");

            for (OrderItem item : order.getItems()) {
                Product p = productDao.findById(item.getProductId());
                if (p != null) {
                    p.setStock(p.getStock() + item.getQuantity());
                    productDao.update(p);
                }
            }
            return orderDao.update(order);
        }
        return false;
    }
}