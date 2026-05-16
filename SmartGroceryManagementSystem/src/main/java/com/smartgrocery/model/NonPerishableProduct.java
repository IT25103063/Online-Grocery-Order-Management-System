package com.smartgrocery.model;

/**
 * Represents a non-perishable grocery item.
 * Demonstrates Inheritance (extends Product).
 */
public class NonPerishableProduct extends Product {

    private int warrantyMonths;

    public NonPerishableProduct() {
        super();
        this.setType("NON_PERISHABLE");
    }

    public NonPerishableProduct(String id, String name, double price, int stock, int warrantyMonths) {
        super(id, name, price, stock, "NON_PERISHABLE");
        this.warrantyMonths = warrantyMonths;
    }

    public int getWarrantyMonths() {
        return warrantyMonths;
    }

    public void setWarrantyMonths(int warrantyMonths) {
        this.warrantyMonths = warrantyMonths;
    }

    @Override
    public String getSpecialDetail() {
        return "Warranty: " + warrantyMonths + " months";
    }
}
