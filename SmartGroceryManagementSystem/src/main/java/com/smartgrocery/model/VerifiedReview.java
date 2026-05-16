package com.smartgrocery.model;

/**
 * Represents a verified purchase review.
 * Demonstrates Inheritance (extends Review).
 */
public class VerifiedReview extends Review {

    private String orderId;

    public VerifiedReview() {
        super();
    }

    public VerifiedReview(String id, String productId, String userId, int rating, String comment, String date, String orderId) {
        super(id, productId, userId, rating, comment, date);
        this.orderId = orderId;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    @Override
    public String getDisplayBadge() {
        return "<span class=\"badge bg-success\"><i class=\"fa-solid fa-check-circle me-1\"></i> Verified Purchase</span>";
    }
}
