package com.smartgrocery.model;

public abstract class Product {
    private String id;
    private String name;
    private double price;
    private int stock;
    private String type; // "PERISHABLE" or "NON_PERISHABLE"
    private String category; // e.g. "Milk & Dairy", "Baby", "Vegetables", etc.

    public Product() {}

    public Product(String id, String name, double price, int stock, String type, String category) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.stock = stock;
        this.type = type;
        this.category = category;
    }

    public abstract String getSpecialDetail();

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

    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }
}