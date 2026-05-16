package com.smartgrocery.servlet;

import com.smartgrocery.model.Product;
import com.smartgrocery.model.Review;
import com.smartgrocery.model.User;
import com.smartgrocery.service.ProductService;
import com.smartgrocery.service.ReviewService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.util.List;

@WebServlet("/reviews")
public class ReviewServlet extends HttpServlet {

    private ReviewService reviewService;
    private ProductService productService;

    @Override
    public void init() throws ServletException {
        this.reviewService = new ReviewService();
        this.productService = new ProductService();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getParameter("action");
        if (action == null) {
            response.sendRedirect("dashboard.jsp");
            return;
        }

        switch (action) {
            case "product":
                showProductReviews(request, response);
                break;
            case "new":
                showSubmissionForm(request, response);
                break;
            case "management":
                showManagement(request, response);
                break;
            case "delete":
                deleteReview(request, response);
                break;
            case "write":
                showWriteReviewPage(request, response);
                break;
            default:
                response.sendRedirect("dashboard.jsp");
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getParameter("action");
        if ("save".equals(action)) {
            saveReview(request, response);
        } else {
            response.sendRedirect("dashboard.jsp");
        }
    }

    private void showProductReviews(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String productId = request.getParameter("id");
        Product p = productService.getProductById(productId);

        if (p == null) {
            response.sendRedirect("products?action=list&error=ProductNotFound");
            return;
        }

        List<Review> reviews = reviewService.getReviewsForProduct(productId);
        double avgRating = reviewService.calculateAverageRating(productId);

        request.setAttribute("productObj", p);
        request.setAttribute("reviews", reviews);
        request.setAttribute("avgRating", String.format("%.1f", avgRating));

        request.getRequestDispatcher("view-reviews.jsp").forward(request, response);
    }

    private void showSubmissionForm(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("user") == null) {
            response.sendRedirect("login.jsp?msg=LoginToReview");
            return;
        }

        String productId = request.getParameter("productId");
        Product p = productService.getProductById(productId);

        if (p != null) {
            request.setAttribute("productObj", p);
            request.getRequestDispatcher("review-submission.jsp").forward(request, response);
        } else {
            response.sendRedirect("products?action=list");
        }
    }

    private void showManagement(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("user") == null) {
            response.sendRedirect("login.jsp");
            return;
        }
        User user = (User) session.getAttribute("user");
        if (!"ADMIN".equals(user.getRole())) {
            response.sendRedirect("dashboard.jsp?error=AccessDenied");
            return;
        }

        List<Review> allReviews = reviewService.getAllReviews();
        request.setAttribute("allReviews", allReviews);
        request.getRequestDispatcher("review-management.jsp").forward(request, response);
    }

    private void saveReview(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("user") == null) {
            response.sendRedirect("login.jsp");
            return;
        }

        User user = (User) session.getAttribute("user");
        String productId = request.getParameter("productId");
        String rating = request.getParameter("rating");
        String comment = request.getParameter("comment");
        String orderId = request.getParameter("orderId");

        String result = reviewService.saveReview(productId, user.getId(), rating, comment, orderId);

        if ("SUCCESS".equals(result)) {
            response.sendRedirect("reviews?action=product&id=" + productId + "&msg=ReviewAdded");
        } else {
            response.sendRedirect("reviews?action=new&productId=" + productId + "&error=SaveFailed");
        }
    }

    private void deleteReview(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        User user = (session != null) ? (User) session.getAttribute("user") : null;

        if (user == null || !"ADMIN".equals(user.getRole())) {
            response.sendRedirect("dashboard.jsp?error=AccessDenied");
            return;
        }

        String id = request.getParameter("id");
        if (reviewService.deleteReview(id)) {
            response.sendRedirect("reviews?action=management&msg=ReviewDeleted");
        } else {
            response.sendRedirect("reviews?action=management&error=DeleteFailed");
        }
    }

    // NEW METHOD: Show Write Review page (common for all users)
    private void showWriteReviewPage(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("user") == null) {
            response.sendRedirect("login.jsp");
            return;
        }

        User user = (User) session.getAttribute("user");

        // Get all products
        List<Product> products = productService.getAllProducts();
        request.setAttribute("products", products);

        // Get user's existing reviews
        List<Review> userReviews = reviewService.getReviewsByUser(user.getId());
        request.setAttribute("userReviews", userReviews);

        request.getRequestDispatcher("write-review.jsp").forward(request, response);
    }
}