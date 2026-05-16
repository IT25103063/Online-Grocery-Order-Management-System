package com.smartgrocery.model;

/**
 * Represents a standard public review.
 * Demonstrates Inheritance (extends Review).
 */
public class PublicReview extends Review {

    public PublicReview() {
        super();
    }

    public PublicReview(String id, String productId, String userId, int rating, String comment, String date) {
        super(id, productId, userId, rating, comment, date);
    }

    @Override
    public String getDisplayBadge() {
        return "<span class=\"badge bg-secondary\"><i class=\"fa-solid fa-globe me-1\"></i> Public Review</span>";
    }
}
