package com.smartgrocery.servlet;

import com.smartgrocery.model.AdminUser;
import com.smartgrocery.model.User;
import com.smartgrocery.service.AuthService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;

@WebServlet("/login")
public class LoginServlet extends HttpServlet {

    private AuthService authService;

    @Override
    public void init() throws ServletException {
        authService = new AuthService();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        if (request.getSession().getAttribute("user") != null) {
            response.sendRedirect("dashboard");
            return;
        }
        request.getRequestDispatcher("login.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String username  = request.getParameter("username");
        String password  = request.getParameter("password");
        String loginType = request.getParameter("loginType"); // "customer" or "admin"

        User user = authService.login(username, password);

        if (user != null) {
            // Validate correct login type
            boolean isAdmin    = user instanceof AdminUser;
            boolean wantsAdmin = "admin".equals(loginType);

            if (isAdmin && !wantsAdmin) {
                // Admin trying customer tab
                request.setAttribute("errorMessage", "Please use the Admin tab to sign in.");
                request.setAttribute("loginType", "customer");
                request.getRequestDispatcher("login.jsp").forward(request, response);
                return;
            }
            if (!isAdmin && wantsAdmin) {
                // Customer trying admin tab
                request.setAttribute("errorMessage", "No admin account found with these credentials.");
                request.setAttribute("loginType", "admin");
                request.getRequestDispatcher("login.jsp").forward(request, response);
                return;
            }

            // All good — create session
            HttpSession session = request.getSession();
            session.setAttribute("user", user);

            // Redirect based on role
            if (isAdmin) {
                response.sendRedirect("dashboard"); // admin sees same dashboard with admin features
            } else {
                response.sendRedirect("dashboard");
            }

        } else {
            request.setAttribute("errorMessage", "Invalid username or password.");
            request.getRequestDispatcher("login.jsp").forward(request, response);
        }
    }
}
