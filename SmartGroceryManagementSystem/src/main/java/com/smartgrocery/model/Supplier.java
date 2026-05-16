package com.smartgrocery.model;

/**
 * Model class representing a Supplier.
 * Demonstrates heavy Encapsulation with private fields and public accessors.
 */
public class Supplier {
    private String id;
    private String companyName;
    private String contactName;
    private String phone;
    private String email;
    private String address;

    public Supplier() {}

    public Supplier(String id, String companyName, String contactName, String phone, String email, String address) {
        this.id = id;
        this.companyName = companyName;
        this.contactName = contactName;
        this.phone = phone;
        this.email = email;
        this.address = address;
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getCompanyName() { return companyName; }
    public void setCompanyName(String companyName) { this.companyName = companyName; }

    public String getContactName() { return contactName; }
    public void setContactName(String contactName) { this.contactName = contactName; }

    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }
}
