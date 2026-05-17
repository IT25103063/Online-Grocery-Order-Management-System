package com.freshgrocer.servlet;

import com.freshgrocer.service.UserService;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;
import java.io.*;

@WebServlet("/ChangePasswordServlet")
public class ChangePasswordServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request,
                          HttpServletResponse response)
            throws ServletException, IOException {

        // Check if user is logged in
        HttpSession session = request.getSession(false);
        if (session == null ||
                session.getAttribute("loggedInUser") == null) {
            response.sendRedirect(
                    request.getContextPath() +
                            "/user-management/login.html");
            return;
        }

        // Get form data
        String email           = (String) session
                .getAttribute("loggedInUser");
        String currentPassword = request
                .getParameter("currentPassword");
        String newPassword     = request
                .getParameter("newPassword");
        String confirmPassword = request
                .getParameter("confirmPassword");

        // Check new passwords match
        if (!newPassword.equals(confirmPassword)) {
            response.sendRedirect(
                    request.getContextPath() +
                            "/user-management/profile.html" +
                            "?error=Passwords+do+not+match");
            return;
        }

        // Check minimum length
        if (newPassword.length() < 8) {
            response.sendRedirect(
                    request.getContextPath() +
                            "/user-management/profile.html" +
                            "?error=Password+must+be+8+characters");
            return;
        }

        // Call service to change password
        UserService userService = new UserService();
        boolean changed = userService.changePassword(
                email, currentPassword, newPassword);

        if (changed) {
            // Success
            response.sendRedirect(
                    request.getContextPath() +
                            "/user-management/profile.html" +
                            "?success=Password+changed+successfully");
        } else {
            // Current password wrong
            response.sendRedirect(
                    request.getContextPath() +
                            "/user-management/profile.html" +
                            "?error=Current+password+is+incorrect");
        }
    }
}