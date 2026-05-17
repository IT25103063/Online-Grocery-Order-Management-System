package com.freshgrocer.servlet;

import com.freshgrocer.service.UserService;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;
import java.io.*;

@WebServlet("/ResetPasswordServlet")
public class ResetPasswordServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String email       = request.getParameter("email");
        String newPassword = request.getParameter("newPassword");

        UserService userService = new UserService();
        boolean reset = userService.resetPassword(email, newPassword);

        if (reset) {
            // BUG FIX: was "/user-management/login" (missing .html) — broken redirect
            response.sendRedirect(request.getContextPath() + "/user-management/login.html?registered=true");
        } else {
            // BUG FIX: was forwarding to a JSP that doesn't exist — now redirects to static HTML
            response.sendRedirect(request.getContextPath() + "/user-management/forgot.html?error=notfound");
        }
    }
}
