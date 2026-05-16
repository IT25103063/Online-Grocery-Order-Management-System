package com.smartgrocery.model;

/**
 * Represents an In-Store Pickup Order.
 * Demonstrates Inheritance (extends Order).
 */
public class PickupOrder extends Order {

    public PickupOrder() {
        super();
        this.setType("PICKUP");
    }

    public PickupOrder(String id, String userId, String orderDate, String status) {
        super(id, userId, orderDate, status, "PICKUP");
    }

    @Override
    public double calculateGrandTotal() {
        // Pickup orders have no shipping fee
        return calculateSubtotal() + calculateTax();
    }
}
