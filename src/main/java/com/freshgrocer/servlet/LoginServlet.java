package com.freshgrocer.servlet;

import com.freshgrocer.model.CustomerUser;
import com.freshgrocer.service.UserService;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;
import java.io.*;

@WebServlet("/LoginServlet")
public class LoginServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String email    = request.getParameter("email");
        String password = request.getParameter("password");
        String role     = request.getParameter("role");

        UserService userService = new UserService();
        HttpSession session     = request.getSession();

        if ("admin".equals(role)) {
            if (userService.validateAdminLogin(email, password)) {
                session.setAttribute("loggedInUser", email);
                session.setAttribute("role", "admin");
                session.setAttribute("username", "Admin");
                // BUG FIX: was redirecting to login.html on success — now goes to profile
                response.sendRedirect(request.getContextPath() + "/user-management/profile.html");
            } else {
                response.sendRedirect(request.getContextPath() + "/user-management/login.html?error=invalid");
            }

        } else {
            if (userService.validateCustomerLogin(email, password)) {
                CustomerUser found = userService.getCustomerByEmail(email);
                session.setAttribute("loggedInUser", email);
                session.setAttribute("role", "customer");
                session.setAttribute("username", found != null ? found.getUsername() : "");
                session.setAttribute("customerData", found);

                if (found != null && found.isBirthdayToday()) {
                    session.setAttribute("birthdayDiscount", true);
                }

                response.sendRedirect(request.getContextPath() + "/user-management/profile.html");
            } else {
                response.sendRedirect(request.getContextPath() + "/user-management/login.html?error=invalid");
            }
        }
    }
}
