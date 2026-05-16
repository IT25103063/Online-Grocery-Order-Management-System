package com.smartgrocery.dao;

import com.smartgrocery.model.PublicReview;
import com.smartgrocery.model.Review;
import com.smartgrocery.model.VerifiedReview;
import com.smartgrocery.util.Constants;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class ReviewDao {

    public List<Review> findAll() {
        List<Review> reviews = new ArrayList<>();
        File file = new File(Constants.REVIEWS_FILE);
        if (!file.exists()) return reviews;

        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split("\\|");
                if (parts.length >= 8) {
                    String id = parts[0];
                    String productId = parts[1];
                    String userId = parts[2];
                    int rating = Integer.parseInt(parts[3]);
                    String comment = parts[4];
                    String type = parts[5];
                    String orderId = parts[6];
                    String date = parts[7];

                    if ("VERIFIED".equals(type)) {
                        reviews.add(new VerifiedReview(id, productId, userId, rating, comment, date, orderId));
                    } else {
                        reviews.add(new PublicReview(id, productId, userId, rating, comment, date));
                    }
                }
            }
        } catch (IOException | NumberFormatException e) {
            e.printStackTrace();
        }
        return reviews;
    }

    public List<Review> findByProductId(String productId) {
        List<Review> productReviews = new ArrayList<>();
        for (Review r : findAll()) {
            if (r.getProductId().equals(productId)) {
                productReviews.add(r);
            }
        }
        return productReviews;
    }

    public Review findById(String id) {
        return findAll().stream().filter(r -> r.getId().equals(id)).findFirst().orElse(null);
    }

    public boolean save(Review review) {
        if (review.getId() == null || review.getId().isEmpty()) {
            review.setId("REV-" + UUID.randomUUID().toString().substring(0, 5).toUpperCase());
        }

        try (BufferedWriter bw = new BufferedWriter(new FileWriter(Constants.REVIEWS_FILE, true))) {
            bw.write(formatLine(review));
            bw.newLine();
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean delete(String id) {
        List<Review> reviews = findAll();
        boolean removed = reviews.removeIf(r -> r.getId().equals(id));
        
        if (removed) {
            return rewriteFile(reviews);
        }
        return false;
    }

    private boolean rewriteFile(List<Review> reviews) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(Constants.REVIEWS_FILE, false))) {
            for (Review r : reviews) {
                bw.write(formatLine(r));
                bw.newLine();
            }
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    private String formatLine(Review r) {
        String type = (r instanceof VerifiedReview) ? "VERIFIED" : "PUBLIC";
        String orderId = (r instanceof VerifiedReview) ? ((VerifiedReview) r).getOrderId() : "NONE";
        
        return r.getId() + "|" + 
               r.getProductId() + "|" + 
               r.getUserId() + "|" + 
               r.getRating() + "|" + 
               r.getComment() + "|" + 
               type + "|" + 
               orderId + "|" + 
               r.getDate();
    }
}
