package com.smartgrocery.servlet;

import com.smartgrocery.model.AdminUser;
import com.smartgrocery.model.User;
import com.smartgrocery.service.AdminManager;
import com.smartgrocery.service.FileAdminManager;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.util.List;

@WebServlet("/admins")
public class AdminServlet extends HttpServlet {

    private AdminManager adminManager;

    @Override
    public void init() throws ServletException {
        // Utilizing Abstraction: The Servlet only knows about AdminManager
        this.adminManager = new FileAdminManager();
    }

    private boolean checkSuperAdmin(HttpServletRequest request, HttpServletResponse response) throws IOException {
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("user") == null) {
            response.sendRedirect("login.jsp");
            return false;
        }

        User user = (User) session.getAttribute("user");
        if (!(user instanceof AdminUser)) {
            response.sendRedirect("dashboard.jsp");
            return false;
        }

        AdminUser admin = (AdminUser) user;
        if (!adminManager.hasPermission(admin, "SUPER_ADMIN")) {
            response.sendRedirect("dashboard.jsp?error=AccessDenied");
            return false;
        }
        return true;
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        if (!checkSuperAdmin(request, response)) return;

        String action = request.getParameter("action");
        if (action == null) action = "dashboard";

        switch (action) {
            case "dashboard":
                showDashboard(request, response);
                break;
            case "management":
                showManagement(request, response);
                break;
            case "new":
                showNewForm(request, response);
                break;
            case "edit":
                showEditForm(request, response);
                break;
            case "delete":
                deleteAdmin(request, response);
                break;
            default:
                showDashboard(request, response);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        if (!checkSuperAdmin(request, response)) return;

        String action = request.getParameter("action");
        if ("save".equals(action) || "update".equals(action)) {
            saveOrUpdateAdmin(request, response);
        } else {
            response.sendRedirect("admins?action=dashboard");
        }
    }

    private void showDashboard(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        List<String> logs = adminManager.readLogs();
        request.setAttribute("logs", logs);
        
        List<AdminUser> admins = adminManager.getAllAdmins();
        request.setAttribute("adminCount", admins.size());
        
        request.getRequestDispatcher("admin-dashboard.jsp").forward(request, response);
    }

    private void showManagement(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        List<AdminUser> admins = adminManager.getAllAdmins();
        request.setAttribute("admins", admins);
        request.getRequestDispatcher("admin-management.jsp").forward(request, response);
    }

    private void showNewForm(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.getRequestDispatcher("admin-form.jsp").forward(request, response);
    }

    private void showEditForm(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String id = request.getParameter("id");
        AdminUser admin = adminManager.getAdminById(id);
        
        if (admin != null) {
            request.setAttribute("adminObj", admin);
            request.getRequestDispatcher("admin-form.jsp").forward(request, response);
        } else {
            response.sendRedirect("admins?action=management&error=NotFound");
        }
    }

    private void saveOrUpdateAdmin(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String id = request.getParameter("id");
        String username = request.getParameter("username");
        String email = request.getParameter("email");
        String password = request.getParameter("password");
        String permission = request.getParameter("permission"); // Super admin or regular manager

        AdminUser admin;
        boolean isUpdate = (id != null && !id.isEmpty());

        if (isUpdate) {
            admin = adminManager.getAdminById(id);
            if (admin == null) {
                response.sendRedirect("admins?action=management&error=NotFound");
                return;
            }
            admin.setUsername(username);
            admin.setEmail(email);
            if (password != null && !password.isEmpty()) {
                admin.setPassword(password); // in real app, hash this
            }
            admin.setAdminCode(permission); // We use adminCode to store permission
        } else {
            admin = new AdminUser(null, username, email, password, permission);
        }

        boolean success;
        if (isUpdate) {
            success = adminManager.updateAdmin(admin);
        } else {
            success = adminManager.saveAdmin(admin);
        }

        if (success) {
            String msg = isUpdate ? "AdminUpdated" : "AdminCreated";
            response.sendRedirect("admins?action=management&msg=" + msg);
        } else {
            response.sendRedirect("admins?action=management&error=SaveFailed");
        }
    }

    private void deleteAdmin(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String id = request.getParameter("id");
        
        // Prevent deleting yourself
        AdminUser currentUser = (AdminUser) request.getSession().getAttribute("user");
        if (currentUser.getId().equals(id)) {
            response.sendRedirect("admins?action=management&error=CannotDeleteSelf");
            return;
        }

        if (adminManager.deleteAdmin(id)) {
            response.sendRedirect("admins?action=management&msg=AdminDeleted");
        } else {
            response.sendRedirect("admins?action=management&error=DeleteFailed");
        }
    }
}
