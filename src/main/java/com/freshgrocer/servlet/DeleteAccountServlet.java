package com.freshgrocer.servlet;

import com.freshgrocer.service.UserService;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;
import java.io.*;

/**
 * DeleteAccountServlet.java
 * DELETE operation — removes user from users.txt
 */
@WebServlet("/DeleteAccountServlet")
public class DeleteAccountServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request,
                          HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false);
        if (session == null) {
            response.sendRedirect(request.getContextPath() + "/user-management/login.html");
            return;
        }

        String email = (String) session
                .getAttribute("loggedInUser");

        UserService userService = new UserService();

        // CRUD: DELETE operation
        boolean deleted = userService.deleteCustomer(email);

        if (deleted) {
            // Clear session and redirect
            session.invalidate();
            response.sendRedirect(request.getContextPath() + "/user-management/login.html?deleted=true");
        } else {
            request.setAttribute("error",
                    "Delete failed. Please try again.");
            response.sendRedirect(request.getContextPath() + "/user-management/profile.html");
        }
    }
}