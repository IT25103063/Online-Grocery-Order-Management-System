package com.smartgrocery.service;

import com.smartgrocery.dao.PendingAdminDao;
import com.smartgrocery.dao.UserDao;
import com.smartgrocery.model.AdminUser;
import com.smartgrocery.model.CustomerUser;
import com.smartgrocery.model.PendingAdmin;
import com.smartgrocery.model.User;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;  // ADD THIS LINE

public class AuthService {

    private UserDao userDao;
    private PendingAdminDao pendingAdminDao;

    public AuthService() {
        this.userDao = new UserDao();
        this.pendingAdminDao = new PendingAdminDao();
    }

    public User login(String username, String password) {
        User user = userDao.findByUsername(username);
        if (user != null && user.getPassword().equals(password)) {
            return user;
        }
        return null;
    }

    public String register(String username, String password, String email, String role) {
        if (username == null || username.trim().isEmpty()) return "Username cannot be empty.";
        if (password == null || password.trim().isEmpty()) return "Password cannot be empty.";

        if (userDao.findByUsername(username) != null) {
            return "Username is already taken.";
        }

        // If registering as ADMIN, put in pending approval
        if ("ADMIN".equalsIgnoreCase(role)) {
            // Check if already pending
            for (PendingAdmin pending : pendingAdminDao.findByStatus("PENDING")) {
                if (pending.getUsername().equals(username)) {
                    return "Admin registration already pending approval.";
                }
            }

            String dateStr = new SimpleDateFormat("yyyy-MM-dd HH:mm").format(new Date());
            PendingAdmin pendingAdmin = new PendingAdmin(null, username, password, email, dateStr, "PENDING");

            if (pendingAdminDao.save(pendingAdmin)) {
                return "PENDING_APPROVAL";
            } else {
                return "Failed to save admin request.";
            }
        }
        // Register as CUSTOMER - instant access
        else {
            User newUser = new CustomerUser(null, username, password, email, "Standard");
            boolean saved = userDao.save(newUser);
            return saved ? "SUCCESS" : "Failed to save user.";
        }
    }

    public String approveAdmin(String pendingId) {
        List<PendingAdmin> pendingList = pendingAdminDao.findAll();
        for (PendingAdmin pending : pendingList) {
            if (pending.getId().equals(pendingId) && "PENDING".equals(pending.getStatus())) {
                // Create actual admin
                AdminUser newAdmin = new AdminUser(null, pending.getUsername(),
                        pending.getPassword(), pending.getEmail(), "MANAGER");
                boolean saved = userDao.save(newAdmin);

                if (saved) {
                    pending.setStatus("APPROVED");
                    pendingAdminDao.update(pending);
                    return "SUCCESS";
                }
            }
        }
        return "FAILED";
    }
}