package com.groceryhub.model;


import java.math.BigDecimal;

public class Customer extends User {

    private String id;
    private String name;
    private String status;
    private Integer orders;
    private BigDecimal totalSpent = BigDecimal.ZERO;
    private String address;
    private String joined;

    public Customer() {
        super();
    }

    public Customer(String fullName, String email, String passwordHash, String phone) {
        super(fullName, email, passwordHash, phone, null, null);
    }

    // Modern exact JSON field Getters & Setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public Integer getOrders() { return orders; }
    public void setOrders(Integer orders) { this.orders = orders; }

    public BigDecimal getTotalSpent() { return totalSpent; }
    public void setTotalSpent(BigDecimal totalSpent) { this.totalSpent = totalSpent; }

    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }

    public String getJoined() { return joined; }
    public void setJoined(String joined) { this.joined = joined; }

    // Compatibility Adapters for older Java DAO / Service method signatures
    public Integer getCustomerId() {
        if (id == null) return null;
        try {
            return Integer.parseInt(id.replaceAll("\\D+", ""));
        } catch (Exception e) {
            return null;
        }
    }
    public void setCustomerId(Integer customerId) {
        this.id = "CUST" + String.format("%03d", customerId);
    }

    public Boolean getIsBlocked() { return "Blocked".equalsIgnoreCase(status); }
    public void setIsBlocked(Boolean isBlocked) { this.status = isBlocked ? "Blocked" : "Active"; }

    public Integer getTotalOrders() { return orders != null ? orders : 0; }
    public void setTotalOrders(Integer totalOrders) { this.orders = totalOrders; }
}

