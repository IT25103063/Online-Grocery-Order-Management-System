package com.smartgrocery.model;

public class PerishableProduct extends Product {

    private String expirationDate;

    public PerishableProduct() {
        super();
        this.setType("PERISHABLE");
    }

    public PerishableProduct(String id, String name, double price, int stock, String expirationDate, String category) {
        super(id, name, price, stock, "PERISHABLE", category);
        this.expirationDate = expirationDate;
    }

    public String getExpirationDate() { return expirationDate; }
    public void setExpirationDate(String expirationDate) { this.expirationDate = expirationDate; }

    @Override
    public String getSpecialDetail() {
        return "Expires: " + expirationDate;
    }
}