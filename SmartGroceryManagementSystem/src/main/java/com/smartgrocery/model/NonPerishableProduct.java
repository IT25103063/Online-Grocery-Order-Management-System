package com.smartgrocery.model;

public class NonPerishableProduct extends Product {

    private int warrantyMonths;

    public NonPerishableProduct() {
        super();
        this.setType("NON_PERISHABLE");
    }

    public NonPerishableProduct(String id, String name, double price, int stock, int warrantyMonths, String category) {
        super(id, name, price, stock, "NON_PERISHABLE", category);
        this.warrantyMonths = warrantyMonths;
    }

    public int getWarrantyMonths() { return warrantyMonths; }
    public void setWarrantyMonths(int warrantyMonths) { this.warrantyMonths = warrantyMonths; }

    @Override
    public String getSpecialDetail() {
        return "Shelf life: " + warrantyMonths + " months";
    }
}