package com.smartgrocery.model;

public class PendingAdmin {
    private String id;
    private String username;
    private String password;
    private String email;
    private String requestDate;
    private String status; // PENDING, APPROVED, REJECTED

    public PendingAdmin() {}

    public PendingAdmin(String id, String username, String password, String email, String requestDate, String status) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.email = email;
        this.requestDate = requestDate;
        this.status = status;
    }

    // Getters and Setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getRequestDate() { return requestDate; }
    public void setRequestDate(String requestDate) { this.requestDate = requestDate; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
}