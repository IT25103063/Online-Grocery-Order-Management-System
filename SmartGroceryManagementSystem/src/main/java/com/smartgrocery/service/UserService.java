package com.smartgrocery.service;

import com.smartgrocery.dao.UserDao;
import com.smartgrocery.model.AdminUser;
import com.smartgrocery.model.CustomerUser;
import com.smartgrocery.model.User;

import java.util.ArrayList;
import java.util.List;

public class UserService {
    private UserDao userDao;

    public UserService() {
        this.userDao = new UserDao();
    }

    public List<User> getAllUsers() {
        return userDao.findAll();
    }

    /**
     * Searches users by keyword (matches username, email, or role).
     */
    public List<User> searchUsers(String keyword) {
        List<User> allUsers = getAllUsers();
        if (keyword == null || keyword.trim().isEmpty()) {
            return allUsers;
        }

        String lowerKeyword = keyword.toLowerCase();
        List<User> filteredList = new ArrayList<>();

        for (User user : allUsers) {
            if (user.getUsername().toLowerCase().contains(lowerKeyword) ||
                    user.getEmail().toLowerCase().contains(lowerKeyword) ||
                    user.getRole().toLowerCase().contains(lowerKeyword)) {
                filteredList.add(user);
            }
        }
        return filteredList;
    }

    public User getUserById(String id) {
        return userDao.findById(id);
    }

    /**
     * Validates and updates an existing user.
     */
    public String updateUser(String id, String username, String email, String password, String specificField) {
        if (username == null || username.trim().isEmpty()) return "Username is required.";
        if (email == null || email.trim().isEmpty()) return "Email is required.";

        User existingUser = userDao.findById(id);
        if (existingUser == null) return "User not found.";

        // Check if username is taken by someone else
        User checkUser = userDao.findByUsername(username);
        if (checkUser != null && !checkUser.getId().equals(id)) {
            return "Username is already in use by another account.";
        }

        existingUser.setUsername(username);
        existingUser.setEmail(email);

        // Update password only if provided
        if (password != null && !password.trim().isEmpty()) {
            existingUser.setPassword(password);
        }

        // Update specific fields based on polymorphism
        if (existingUser instanceof AdminUser) {
            ((AdminUser) existingUser).setAdminCode(specificField);
        } else if (existingUser instanceof CustomerUser) {
            ((CustomerUser) existingUser).setMembershipLevel(specificField);
        }

        boolean success = userDao.update(existingUser);
        return success ? "SUCCESS" : "Failed to update user in file.";
    }

    public boolean deleteUser(String id) {
        return userDao.delete(id);
    }
}