package com.smartgrocery.service;

import com.smartgrocery.dao.UserDao;
import com.smartgrocery.model.AdminUser;
import com.smartgrocery.model.User;
import com.smartgrocery.util.Constants;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Concrete implementation demonstrating Inheritance.
 * Heavily encapsulates file I/O logic for logging.
 */
public class FileAdminManager extends AdminManager {

    private UserDao userDao;

    public FileAdminManager() {
        this.userDao = new UserDao();
    }

    @Override
    public void logActivity(String action) {
        String timestamp = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
        String logEntry = "[" + timestamp + "] " + action;

        try (BufferedWriter bw = new BufferedWriter(new FileWriter(Constants.ADMIN_LOGS_FILE, true))) {
            bw.write(logEntry);
            bw.newLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public List<String> readLogs() {
        List<String> logs = new ArrayList<>();
        File file = new File(Constants.ADMIN_LOGS_FILE);
        if (!file.exists()) return logs;

        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = br.readLine()) != null) {
                logs.add(0, line); // Add to top to show newest first
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return logs;
    }

    @Override
    public List<AdminUser> getAllAdmins() {
        List<AdminUser> admins = new ArrayList<>();
        for (User u : userDao.findAll()) {
            if (u instanceof AdminUser) {
                admins.add((AdminUser) u);
            }
        }
        return admins;
    }

    @Override
    public AdminUser getAdminById(String id) {
        User u = userDao.findById(id);
        if (u instanceof AdminUser) {
            return (AdminUser) u;
        }
        return null;
    }

    @Override
    public boolean saveAdmin(AdminUser admin) {
        boolean success = userDao.save(admin);
        if (success) logActivity("Created new Admin: " + admin.getUsername() + " with permission: " + admin.getAdminCode());
        return success;
    }

    @Override
    public boolean updateAdmin(AdminUser admin) {
        boolean success = userDao.update(admin);
        if (success) logActivity("Updated Admin profile/permissions for: " + admin.getUsername());
        return success;
    }

    @Override
    public boolean deleteAdmin(String id) {
        AdminUser target = getAdminById(id);
        boolean success = userDao.delete(id);
        if (success && target != null) {
            logActivity("Deleted Admin account: " + target.getUsername());
        }
        return success;
    }
}
