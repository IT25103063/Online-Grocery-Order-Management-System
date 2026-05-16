package com.smartgrocery.model;

/**
 * Represents a regular customer.
 * Demonstrates Inheritance (extends User).
 */
public class CustomerUser extends User {

    private String membershipLevel;

    public CustomerUser() {
        super();
        this.setRole("CUSTOMER");
        this.membershipLevel = "Standard";
    }

    public CustomerUser(String id, String username, String password, String email, String membershipLevel) {
        super(id, username, password, email, "CUSTOMER");
        this.membershipLevel = membershipLevel;
    }

    public String getMembershipLevel() {
        return membershipLevel;
    }

    public void setMembershipLevel(String membershipLevel) {
        this.membershipLevel = membershipLevel;
    }

    @Override
    public String getDisplayRole() {
        return "Valued Customer (" + membershipLevel + ")";
    }
}
