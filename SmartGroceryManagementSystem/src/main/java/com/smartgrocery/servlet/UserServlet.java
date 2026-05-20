package com.smartgrocery.servlet;

import com.smartgrocery.model.AdminUser;
import com.smartgrocery.model.CustomerUser;
import com.smartgrocery.model.User;
import com.smartgrocery.service.AuthService;
import com.smartgrocery.service.UserService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.util.List;

@WebServlet("/users")
public class UserServlet extends HttpServlet {

    private UserService userService;
    private AuthService authService;

    @Override
    public void init() throws ServletException {
        userService = new UserService();
        authService = new AuthService();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("user") == null) {
            response.sendRedirect("login.jsp");
            return;
        }

        User loggedInUser = (User) session.getAttribute("user");

        // ONLY ADMIN can access user management
        // Allow customer to delete their own account only
        if (!"ADMIN".equals(loggedInUser.getRole())) {
            String action = request.getParameter("action");
            if ("delete".equals(action)) {
                String idToDelete = request.getParameter("id");
                if (idToDelete.equals(loggedInUser.getId())) {
                    userService.deleteUser(idToDelete);
                    session.invalidate();
                    response.sendRedirect(request.getContextPath() + "/login");
                    return;
                }
            }
            response.sendRedirect("dashboard.jsp?error=AccessDenied");
            return;
        }

        String action = request.getParameter("action");
        if (action == null) action = "list";

        switch (action) {
            case "new":
                showNewForm(request, response);
                break;
            case "edit":
                showEditForm(request, response);
                break;
            case "delete":
                deleteUser(request, response);
                break;
            case "list":
            default:
                listUsers(request, response);
                break;
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("user") == null) {
            response.sendRedirect("login.jsp");
            return;
        }

        User loggedInUser = (User) session.getAttribute("user");

        // ONLY ADMIN can save or update users
        if (!"ADMIN".equals(loggedInUser.getRole())) {
            response.sendRedirect("dashboard.jsp?error=AccessDenied");
            return;
        }

        String action = request.getParameter("action");
        if ("save".equals(action)) {
            insertUser(request, response);
        } else if ("update".equals(action)) {
            updateUser(request, response);
        } else {
            response.sendRedirect("users?action=list");
        }
    }

    private void listUsers(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String search = request.getParameter("search");
        List<User> listUser;

        if (search != null && !search.trim().isEmpty()) {
            listUser = userService.searchUsers(search);
            request.setAttribute("searchQuery", search);
        } else {
            listUser = userService.getAllUsers();
        }

        request.setAttribute("listUser", listUser);
        request.getRequestDispatcher("user-management.jsp").forward(request, response);
    }

    private void showNewForm(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.getRequestDispatcher("user-form.jsp").forward(request, response);
    }

    private void showEditForm(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String id = request.getParameter("id");
        User existingUser = userService.getUserById(id);

        if (existingUser != null) {
            request.setAttribute("userObj", existingUser);

            if (existingUser instanceof AdminUser) {
                request.setAttribute("specificField", ((AdminUser) existingUser).getAdminCode());
            } else if (existingUser instanceof CustomerUser) {
                request.setAttribute("specificField", ((CustomerUser) existingUser).getMembershipLevel());
            }

            request.getRequestDispatcher("user-form.jsp").forward(request, response);
        } else {
            response.sendRedirect("users?action=list&error=UserNotFound");
        }
    }

    private void insertUser(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String username = request.getParameter("username");
        String email = request.getParameter("email");
        String password = request.getParameter("password");
        String role = request.getParameter("role");

        String result = authService.register(username, password, email, role);

        if ("SUCCESS".equals(result)) {
            response.sendRedirect("users?action=list&msg=UserCreated");
        } else {
            request.setAttribute("errorMessage", result);
            request.getRequestDispatcher("user-form.jsp").forward(request, response);
        }
    }

    private void updateUser(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String id = request.getParameter("id");
        String username = request.getParameter("username");
        String email = request.getParameter("email");
        String password = request.getParameter("password");
        String specificField = request.getParameter("specificField");

        String result = userService.updateUser(id, username, email, password, specificField);

        if ("SUCCESS".equals(result)) {
            response.sendRedirect("users?action=list&msg=UserUpdated");
        } else {
            User user = userService.getUserById(id);
            request.setAttribute("userObj", user);
            request.setAttribute("specificField", specificField);
            request.setAttribute("errorMessage", result);
            request.getRequestDispatcher("user-form.jsp").forward(request, response);
        }
    }

    private void deleteUser(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String id = request.getParameter("id");
        boolean deleted = userService.deleteUser(id);
        if (deleted) {
            response.sendRedirect("users?action=list&msg=UserDeleted");
        } else {
            response.sendRedirect("users?action=list&error=DeleteFailed");
        }
    }
}