package com.freshgrocer.servlet;

import com.freshgrocer.service.UserService;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;
import java.io.*;

/**
 * UpdateProfileServlet.java
 * UPDATE operation — updates user data in users.txt
 */
@WebServlet("/UpdateProfileServlet")
public class UpdateProfileServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request,
                          HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false);
        if (session == null ||
                session.getAttribute("loggedInUser") == null) {
            response.sendRedirect(request.getContextPath() + "/user-management/login.html");
            return;
        }

        String email      = (String) session
                .getAttribute("loggedInUser");
        String newUsername = request.getParameter("username");
        String newPhone    = request.getParameter("phone");
        String newAddress  = request.getParameter("address");

        UserService userService = new UserService();

        // CRUD: UPDATE operation
        boolean updated = userService.updateCustomer(
                email, newUsername, newPhone, newAddress);

        if (updated) {
            session.setAttribute("username", newUsername);
            request.setAttribute("success",
                    "Profile updated successfully!");
        } else {
            request.setAttribute("error",
                    "Update failed. Please try again.");
        }

        response.sendRedirect(request.getContextPath() + "/user-management/profile.html");
    }
}