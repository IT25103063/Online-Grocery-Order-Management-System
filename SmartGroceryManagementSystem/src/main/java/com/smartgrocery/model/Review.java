package com.smartgrocery.model;

/**
 * Abstract base class for Reviews.
 * Demonstrates Abstraction and Encapsulation.
 */
public abstract class Review {
    private String id;
    private String productId;
    private String userId;
    private int rating; // 1 to 5
    private String comment;
    private String date;

    public Review() {}

    public Review(String id, String productId, String userId, int rating, String comment, String date) {
        this.id = id;
        this.productId = productId;
        this.userId = userId;
        this.rating = rating;
        this.comment = comment;
        this.date = date;
    }

    /**
     * Abstract method to demonstrate Polymorphism.
     * Each subclass will provide its own HTML badge.
     */
    public abstract String getDisplayBadge();

    // Getters and Setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getProductId() { return productId; }
    public void setProductId(String productId) { this.productId = productId; }

    public String getUserId() { return userId; }
    public void setUserId(String userId) { this.userId = userId; }

    public int getRating() { return rating; }
    public void setRating(int rating) { this.rating = rating; }

    public String getComment() { return comment; }
    public void setComment(String comment) { this.comment = comment; }

    public String getDate() { return date; }
    public void setDate(String date) { this.date = date; }
}
