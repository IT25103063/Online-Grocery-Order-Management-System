package com.smartgrocery.servlet;

import com.smartgrocery.service.OrderService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;

@WebServlet("/payment")
public class PaymentServlet extends HttpServlet {

    private OrderService orderService;

    @Override
    public void init() {
        orderService = new OrderService();
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("user") == null) {
            response.sendRedirect("login.jsp");
            return;
        }

        String orderId = request.getParameter("orderId");
        String cardNumber = request.getParameter("cardNumber");
        String cvv = request.getParameter("cvv");

        // Remove spaces from card number
        cardNumber = cardNumber.replaceAll("\\s", "");

        // Simple validation
        if (cardNumber == null || cardNumber.length() < 13) {
            response.sendRedirect("payment.jsp?error=Invalid Card Number&orderId=" + orderId + "&amount=" + request.getParameter("amount"));
            return;
        }

        if (cvv == null || cvv.length() < 3) {
            response.sendRedirect("payment.jsp?error=Invalid CVV&orderId=" + orderId + "&amount=" + request.getParameter("amount"));
            return;
        }

        // Simulate successful payment
        boolean updated = orderService.updateOrderStatus(orderId, "COMPLETED");

        if (updated) {
            response.sendRedirect("orders?action=history&msg=Payment Successful! Order Completed");
        } else {
            response.sendRedirect("payment.jsp?error=Payment Failed&orderId=" + orderId + "&amount=" + request.getParameter("amount"));
        }
    }
}