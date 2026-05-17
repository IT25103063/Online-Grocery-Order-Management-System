package com.groceryhub.model;

import com.groceryhub.enums.OrderStatus;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Order {

    private String id;
    private String customerId;
    private String customer;
    private String phone;
    private String address;
    private List<Map<String, Object>> items = new ArrayList<>();
    private BigDecimal subtotal = BigDecimal.ZERO;
    private BigDecimal delivery = BigDecimal.ZERO;
    private BigDecimal discount = BigDecimal.ZERO;
    private BigDecimal total = BigDecimal.ZERO;
    private String payment;
    private String status;
    private String date;
    private List<Map<String, Object>> timeline = new ArrayList<>();
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public Order() {
    }

    public Order(String id, String customerId, String customer, String phone, String address, List<Map<String, Object>> items, BigDecimal subtotal, BigDecimal delivery, BigDecimal discount, BigDecimal total, String payment, String status, String date, List<Map<String, Object>> timeline, LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.id = id;
        this.customerId = customerId;
        this.customer = customer;
        this.phone = phone;
        this.address = address;
        this.items = items;
        this.subtotal = subtotal;
        this.delivery = delivery;
        this.discount = discount;
        this.total = total;
        this.payment = payment;
        this.status = status;
        this.date = date;
        this.timeline = timeline;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    // Modern exact JSON field Getters & Setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getCustomerId() { return customerId; }
    public void setCustomerId(String customerId) { this.customerId = customerId; }

    public String getCustomerName() { return customer; }
    public void setCustomerName(String customer) { this.customer = customer; }

    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }

    public String getAddressStr() { return address; }
    public void setAddressStr(String address) { this.address = address; }

    public List<Map<String, Object>> getItems() { return items; }
    public void setItems(List<Map<String, Object>> items) { this.items = items; }

    public BigDecimal getSubtotal() { return subtotal; }
    public void setSubtotal(BigDecimal subtotal) { this.subtotal = subtotal; }

    public BigDecimal getDelivery() { return delivery; }
    public void setDelivery(BigDecimal delivery) { this.delivery = delivery; }

    public BigDecimal getDiscount() { return discount; }
    public void setDiscount(BigDecimal discount) { this.discount = discount; }

    public BigDecimal getTotal() { return total; }
    public void setTotal(BigDecimal total) { this.total = total; }

    public String getPayment() { return payment; }
    public void setPayment(String payment) { this.payment = payment; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public String getDate() { return date; }
    public void setDate(String date) { this.date = date; }

    public List<Map<String, Object>> getTimeline() { return timeline; }
    public void setTimeline(List<Map<String, Object>> timeline) { this.timeline = timeline; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }

    // Compatibility Adapters for older Java DAO / Service method signatures
    public Integer getOrderId() {
        if (id == null) return null;
        try {
            return Integer.parseInt(id.replaceAll("\\D+", ""));
        } catch (Exception e) {
            return null;
        }
    }
    public void setOrderId(Integer orderId) {
        this.id = "ORD" + String.format("%04d", orderId);
    }

    public String getOrderNumber() { return id; }
    public void setOrderNumber(String orderNumber) { this.id = orderNumber; }

    public Customer getCustomer() {
        Customer c = new Customer();
        c.setId(customerId);
        c.setName(customer);
        c.setPhone(phone);
        return c;
    }

    public LocalDateTime getOrderDate() {
        if (date == null) return LocalDateTime.now();
        try {
            return LocalDateTime.parse(date + "T00:00:00");
        } catch (Exception e) {
            return LocalDateTime.now();
        }
    }
    public void setOrderDate(LocalDateTime orderDate) {
        if (orderDate != null) {
            this.date = orderDate.toLocalDate().toString();
        }
    }

    public OrderStatus getOrderStatus() {
        if (status == null) return OrderStatus.pending;
        try {
            return OrderStatus.valueOf(status.toLowerCase());
        } catch (Exception e) {
            return OrderStatus.pending;
        }
    }
    public void setOrderStatus(OrderStatus orderStatus) {
        this.status = orderStatus != null ? orderStatus.name() : "pending";
    }

    public BigDecimal getGrandTotal() { return total; }
    public void setGrandTotal(BigDecimal grandTotal) { this.total = grandTotal; }

    public BigDecimal getDeliveryCharge() { return delivery; }
    public void setDeliveryCharge(BigDecimal deliveryCharge) { this.delivery = deliveryCharge; }

    public BigDecimal getDiscountAmount() { return discount; }
    public void setDiscountAmount(BigDecimal discountAmount) { this.discount = discountAmount; }

    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    protected void onUpdate() {
        this.updatedAt = LocalDateTime.now();
    }
}

