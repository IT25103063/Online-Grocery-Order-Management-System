package com.smartgrocery.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Abstract base class for Orders.
 * Demonstrates Abstraction and Encapsulation.
 */
public abstract class Order {
    private String id;
    private String userId;
    private String orderDate;
    private String status; // PENDING, COMPLETED, CANCELLED
    private String type; // PICKUP or DELIVERY
    private List<OrderItem> items;
    
    private double taxRate = 0.05; // 5% tax

    public Order() {
        this.items = new ArrayList<>();
    }

    public Order(String id, String userId, String orderDate, String status, String type) {
        this.id = id;
        this.userId = userId;
        this.orderDate = orderDate;
        this.status = status;
        this.type = type;
        this.items = new ArrayList<>();
    }

    // Abstract method demonstrating Polymorphism
    public abstract double calculateGrandTotal();

    public double calculateSubtotal() {
        double subtotal = 0;
        for (OrderItem item : items) {
            subtotal += item.getSubtotal();
        }
        return subtotal;
    }

    public double calculateTax() {
        return calculateSubtotal() * taxRate;
    }

    // Getters and Setters
    public void addItem(OrderItem item) { this.items.add(item); }
    public List<OrderItem> getItems() { return items; }
    public void setItems(List<OrderItem> items) { this.items = items; }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getUserId() { return userId; }
    public void setUserId(String userId) { this.userId = userId; }

    public String getOrderDate() { return orderDate; }
    public void setOrderDate(String orderDate) { this.orderDate = orderDate; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public String getType() { return type; }
    public void setType(String type) { this.type = type; }
}
