package com.freshgrocer.servlet;

import com.freshgrocer.model.CustomerUser;
import com.freshgrocer.service.UserService;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;
import java.io.*;

/**
 * RegisterServlet.java
 * Handles POST from register.html form
 * CREATE operation — saves new user to users.txt
 */
@WebServlet("/RegisterServlet")
public class RegisterServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request,
                          HttpServletResponse response)
            throws ServletException, IOException {

        // Get all form fields
        String username = request.getParameter("username");
        String phone    = request.getParameter("phone");
        String address  = request.getParameter("address");
        String email    = request.getParameter("email");
        String password = request.getParameter("password");
        String birthday = request.getParameter("birthday");

        // OOP: Create CustomerUser object (ENCAPSULATION)
        CustomerUser customer = new CustomerUser(
                "",          // userId generated in service
                username,
                password,
                email,
                "active",
                phone,
                address,
                birthday
        );

        UserService userService = new UserService();

        // CRUD: CREATE operation
        if (userService.registerCustomer(customer)) {
            // Success - redirect to login
            response.sendRedirect(request.getContextPath() + "/user-management/login.html?registered=true");
        } else {
            // Email already exists
            request.setAttribute("error",
                    "Email already registered. Please login.");
            response.sendRedirect(request.getContextPath() + "/user-management/register.html?error=exists");
        }
    }
}