package com.freshgrocer.service;

import com.freshgrocer.model.CustomerUser;
import com.freshgrocer.model.AdminUser;
import com.freshgrocer.util.UserFileHandler;
import java.io.*;
import java.util.*;

public class UserService {

    private UserFileHandler fileHandler = new UserFileHandler();

    public boolean registerCustomer(CustomerUser customer) {
        try {
            if (fileHandler.emailExists(customer.getEmail())) return false;
            String id = "CUST-" + UUID.randomUUID().toString().substring(0,8).toUpperCase();
            customer.setUserId(id);
            customer.setStatus("active");
            fileHandler.writeCustomer(customer);
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean validateCustomerLogin(String email, String password) {
        try {
            for (String line : fileHandler.readAllUserLines()) {
                CustomerUser c = CustomerUser.fromFileString(line);
                if (c != null && c.login(email, password)) return true;
            }
        } catch (IOException e) { e.printStackTrace(); }
        return false;
    }

    public boolean validateAdminLogin(String email, String password) {
        try {
            for (String line : fileHandler.readAllAdminLines()) {
                AdminUser a = AdminUser.fromFileString(line);
                if (a != null && a.login(email, password)) return true;
            }
        } catch (IOException e) { e.printStackTrace(); }
        return false;
    }

    public CustomerUser getCustomerByEmail(String email) {
        try {
            for (String line : fileHandler.readAllUserLines()) {
                String[] parts = line.split(",");
                if (parts.length >= 4 && parts[3].equalsIgnoreCase(email))
                    return CustomerUser.fromFileString(line);
            }
        } catch (IOException e) { e.printStackTrace(); }
        return null;
    }

    public boolean updateCustomer(String email, String newUsername,
                                  String newPhone, String newAddress) {
        try {
            List<String> lines   = fileHandler.readAllUserLines();
            List<String> updated = new ArrayList<>();
            boolean found = false;
            for (String line : lines) {
                String[] parts = line.split(",");
                if (parts.length >= 4 && parts[3].equalsIgnoreCase(email)) {
                    parts[1] = newUsername;
                    parts[6] = newPhone;
                    parts[7] = newAddress;
                    updated.add(String.join(",", parts));
                    found = true;
                } else {
                    updated.add(line);
                }
            }
            if (found) fileHandler.overwriteUsers(updated);
            return found;
        } catch (IOException e) { e.printStackTrace(); return false; }
    }

    public boolean changePassword(String email, String currentPass, String newPass) {
        try {
            List<String> lines   = fileHandler.readAllUserLines();
            List<String> updated = new ArrayList<>();
            boolean found = false;
            for (String line : lines) {
                String[] parts = line.split(",");
                if (parts.length >= 4 && parts[3].equalsIgnoreCase(email)
                        && parts[2].equals(currentPass)) {
                    parts[2] = newPass;
                    updated.add(String.join(",", parts));
                    found = true;
                } else {
                    updated.add(line);
                }
            }
            if (found) fileHandler.overwriteUsers(updated);
            return found;
        } catch (IOException e) { e.printStackTrace(); return false; }
    }

    public boolean resetPassword(String email, String newPass) {
        try {
            List<String> lines   = fileHandler.readAllUserLines();
            List<String> updated = new ArrayList<>();
            boolean found = false;
            for (String line : lines) {
                String[] parts = line.split(",");
                if (parts.length >= 4 && parts[3].equalsIgnoreCase(email)) {
                    parts[2] = newPass;
                    updated.add(String.join(",", parts));
                    found = true;
                } else { updated.add(line); }
            }
            if (found) fileHandler.overwriteUsers(updated);
            return found;
        } catch (IOException e) { e.printStackTrace(); return false; }
    }

    public boolean deleteCustomer(String email) {
        try {
            List<String> lines   = fileHandler.readAllUserLines();
            List<String> updated = new ArrayList<>();
            boolean found = false;
            for (String line : lines) {
                String[] parts = line.split(",");
                if (parts.length >= 4 && parts[3].equalsIgnoreCase(email)) {
                    found = true;
                } else { updated.add(line); }
            }
            if (found) fileHandler.overwriteUsers(updated);
            return found;
        } catch (IOException e) { e.printStackTrace(); return false; }
    }
}