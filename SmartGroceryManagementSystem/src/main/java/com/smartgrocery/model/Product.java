package com.smartgrocery.model;

/**
 * Abstract base class for all products.
 * Demonstrates Abstraction and Encapsulation.
 */
public abstract class Product {
    private String id;
    private String name;
    private double price;
    private int stock;
    private String type; // "PERISHABLE" or "NON_PERISHABLE"

    public Product() {}

    public Product(String id, String name, double price, int stock, String type) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.stock = stock;
        this.type = type;
    }

    // Abstract method demonstrating Polymorphism
    public abstract String getSpecialDetail();

    // Getters and Setters (Encapsulation)
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public double getPrice() { return price; }
    public void setPrice(double price) { this.price = price; }

    public int getStock() { return stock; }
    public void setStock(int stock) { this.stock = stock; }

    public String getType() { return type; }
    public void setType(String type) { this.type = type; }
}
