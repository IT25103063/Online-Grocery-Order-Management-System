package com.smartgrocery.dao;

import com.smartgrocery.model.AdminUser;
import com.smartgrocery.model.CustomerUser;
import com.smartgrocery.model.User;
import com.smartgrocery.util.Constants;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class UserDao {

    /**
     * Retrieves all users (both Admins and Customers) from their respective files.
     */
    public List<User> findAll() {
        List<User> users = new ArrayList<>();
        users.addAll(readFromFile(Constants.ADMINS_FILE, "ADMIN"));
        users.addAll(readFromFile(Constants.USERS_FILE, "CUSTOMER"));
        return users;
    }

    /**
     * Helper method to read a list of users from a specific file.
     */
    private List<User> readFromFile(String filePath, String role) {
        List<User> users = new ArrayList<>();
        File file = new File(filePath);
        if (!file.exists()) return users;

        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split("\\|");
                if (parts.length >= 5) {
                    if ("ADMIN".equals(role)) {
                        users.add(new AdminUser(parts[0], parts[1], parts[2], parts[3], parts[4]));
                    } else {
                        users.add(new CustomerUser(parts[0], parts[1], parts[2], parts[3], parts[4]));
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return users;
    }

    /**
     * Finds a user by ID by scanning both files.
     */
    public User findById(String id) {
        List<User> allUsers = findAll();
        for (User user : allUsers) {
            if (user.getId().equals(id)) {
                return user;
            }
        }
        return null;
    }

    /**
     * Finds a user by username by scanning both files.
     */
    public User findByUsername(String username) {
        List<User> allUsers = findAll();
        for (User user : allUsers) {
            if (user.getUsername().equals(username)) {
                return user;
            }
        }
        return null;
    }

    /**
     * Saves a new user to the appropriate file based on their specific type.
     */
    public boolean save(User user) {
        String filePath = (user instanceof AdminUser) ? Constants.ADMINS_FILE : Constants.USERS_FILE;
        
        if (user.getId() == null || user.getId().isEmpty()) {
            user.setId(UUID.randomUUID().toString().substring(0, 8));
        }

        try (BufferedWriter bw = new BufferedWriter(new FileWriter(filePath, true))) {
            bw.write(formatUserLine(user));
            bw.newLine();
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Updates an existing user. It reads all users of that type, modifies the target, and rewrites the file.
     */
    public boolean update(User user) {
        String filePath = (user instanceof AdminUser) ? Constants.ADMINS_FILE : Constants.USERS_FILE;
        List<User> users = readFromFile(filePath, user.getRole());
        
        boolean found = false;
        for (int i = 0; i < users.size(); i++) {
            if (users.get(i).getId().equals(user.getId())) {
                users.set(i, user);
                found = true;
                break;
            }
        }

        if (found) {
            return rewriteFile(filePath, users);
        }
        return false;
    }

    /**
     * Deletes a user by ID. It locates the user to determine the role, reads that file, removes the user, and rewrites.
     */
    public boolean delete(String id) {
        User target = findById(id);
        if (target == null) return false;

        String filePath = (target instanceof AdminUser) ? Constants.ADMINS_FILE : Constants.USERS_FILE;
        List<User> users = readFromFile(filePath, target.getRole());
        
        boolean removed = users.removeIf(u -> u.getId().equals(id));
        
        if (removed) {
            return rewriteFile(filePath, users);
        }
        return false;
    }

    /**
     * Helper method to rewrite an entire file with a list of users.
     */
    private boolean rewriteFile(String filePath, List<User> users) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(filePath, false))) { // false = overwrite
            for (User u : users) {
                bw.write(formatUserLine(u));
                bw.newLine();
            }
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Helper method to format a User object to a pipe-delimited string.
     */
    private String formatUserLine(User user) {
        if (user instanceof AdminUser) {
            AdminUser admin = (AdminUser) user;
            return admin.getId() + "|" + admin.getUsername() + "|" + admin.getPassword() + "|" + admin.getEmail() + "|" + admin.getAdminCode();
        } else {
            CustomerUser customer = (CustomerUser) user;
            return customer.getId() + "|" + customer.getUsername() + "|" + customer.getPassword() + "|" + customer.getEmail() + "|" + customer.getMembershipLevel();
        }
    }
}
