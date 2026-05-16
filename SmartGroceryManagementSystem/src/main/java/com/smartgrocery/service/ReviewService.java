package com.smartgrocery.service;

import com.smartgrocery.dao.ReviewDao;
import com.smartgrocery.model.PublicReview;
import com.smartgrocery.model.Review;
import com.smartgrocery.model.VerifiedReview;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ReviewService {

    private ReviewDao reviewDao;

    public ReviewService() {
        this.reviewDao = new ReviewDao();
    }

    public List<Review> getAllReviews() {
        return reviewDao.findAll();
    }

    public List<Review> getReviewsForProduct(String productId) {
        return reviewDao.findByProductId(productId);
    }

    public double calculateAverageRating(String productId) {
        List<Review> reviews = getReviewsForProduct(productId);
        if (reviews.isEmpty()) return 0.0;

        double sum = 0;
        for (Review r : reviews) {
            sum += r.getRating();
        }
        return sum / reviews.size();
    }

    // NEW METHOD: Get reviews by user ID
    public List<Review> getReviewsByUser(String userId) {
        List<Review> all = reviewDao.findAll();
        List<Review> userReviews = new ArrayList<>();
        for (Review r : all) {
            if (r.getUserId().equals(userId)) {
                userReviews.add(r);
            }
        }
        return userReviews;
    }

    public String saveReview(String productId, String userId, String ratingStr, String comment, String orderId) {
        if (comment == null || comment.trim().isEmpty()) {
            return "Review comment cannot be empty.";
        }

        int rating;
        try {
            rating = Integer.parseInt(ratingStr);
            if (rating < 1 || rating > 5) return "Rating must be between 1 and 5.";
        } catch (NumberFormatException e) {
            return "Invalid rating value.";
        }

        String dateStr = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
        Review review;

        if (orderId != null && !orderId.trim().isEmpty()) {
            review = new VerifiedReview(null, productId, userId, rating, comment, dateStr, orderId.trim());
        } else {
            review = new PublicReview(null, productId, userId, rating, comment, dateStr);
        }

        boolean success = reviewDao.save(review);
        return success ? "SUCCESS" : "Failed to save review.";
    }

    public boolean deleteReview(String id) {
        return reviewDao.delete(id);
    }
}