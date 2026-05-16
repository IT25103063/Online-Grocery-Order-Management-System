package com.smartgrocery.servlet;

import com.smartgrocery.dao.PendingAdminDao;
import com.smartgrocery.model.PendingAdmin;
import com.smartgrocery.model.User;
import com.smartgrocery.service.AuthService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.util.List;

@WebServlet("/admin-approvals")
public class AdminApprovalServlet extends HttpServlet {

    private PendingAdminDao pendingAdminDao;
    private AuthService authService;

    @Override
    public void init() {
        pendingAdminDao = new PendingAdminDao();
        authService = new AuthService();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("user") == null) {
            response.sendRedirect("login.jsp");
            return;
        }

        User user = (User) session.getAttribute("user");

        // Only SUPER_ADMIN can approve admins
        if (!"ADMIN".equals(user.getRole()) ||
                !"SUPER_ADMIN".equals(((com.smartgrocery.model.AdminUser) user).getAdminCode())) {
            response.sendRedirect("dashboard.jsp?error=AccessDenied");
            return;
        }

        String action = request.getParameter("action");

        if ("approve".equals(action)) {
            String id = request.getParameter("id");
            String result = authService.approveAdmin(id);
            if ("SUCCESS".equals(result)) {
                response.sendRedirect("admin-approvals.jsp?msg=Approved");
            } else {
                response.sendRedirect("admin-approvals.jsp?error=Failed");
            }
        } else {
            List<PendingAdmin> pendingList = pendingAdminDao.findByStatus("PENDING");
            request.setAttribute("pendingList", pendingList);
            request.getRequestDispatcher("admin-approvals.jsp").forward(request, response);
        }
    }
}