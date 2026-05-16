package com.smartgrocery.model;

/**
 * Represents an Administrator.
 * Demonstrates Inheritance (extends User).
 */
public class AdminUser extends User {

    private String adminCode;

    public AdminUser() {
        super();
        this.setRole("ADMIN");
    }

    public AdminUser(String id, String username, String password, String email, String adminCode) {
        super(id, username, password, email, "ADMIN");
        this.adminCode = adminCode;
    }

    public String getAdminCode() {
        return adminCode;
    }

    public void setAdminCode(String adminCode) {
        this.adminCode = adminCode;
    }

    @Override
    public String getDisplayRole() {
        return "System Administrator";
    }
}
