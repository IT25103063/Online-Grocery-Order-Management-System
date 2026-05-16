package com.smartgrocery.model;

/**
 * Represents an Online Delivery Order.
 * Demonstrates Inheritance (extends Order).
 */
public class DeliveryOrder extends Order {

    private double shippingFee;

    public DeliveryOrder() {
        super();
        this.setType("DELIVERY");
    }

    public DeliveryOrder(String id, String userId, String orderDate, String status, double shippingFee) {
        super(id, userId, orderDate, status, "DELIVERY");
        this.shippingFee = shippingFee;
    }

    public double getShippingFee() {
        return shippingFee;
    }

    public void setShippingFee(double shippingFee) {
        this.shippingFee = shippingFee;
    }

    @Override
    public double calculateGrandTotal() {
        // Delivery orders add the shipping fee
        return calculateSubtotal() + calculateTax() + shippingFee;
    }
}
