package com.smartgrocery.service;

import com.smartgrocery.model.AdminUser;

import java.util.List;

/**
 * Abstract class demonstrating Abstraction for Admin Management.
 * It hides the complex implementations of logging and CRUD operations.
 */
public abstract class AdminManager {
    
    // Abstract methods to be implemented by concrete classes
    public abstract void logActivity(String action);
    public abstract List<String> readLogs();
    
    public abstract List<AdminUser> getAllAdmins();
    public abstract AdminUser getAdminById(String id);
    public abstract boolean saveAdmin(AdminUser admin);
    public abstract boolean updateAdmin(AdminUser admin);
    public abstract boolean deleteAdmin(String id);

    /**
     * Common Permission Checking Logic (Encapsulated)
     */
    public boolean hasPermission(AdminUser admin, String requiredPermission) {
        if (admin == null) return false;
        // In our system, the 'adminCode' field acts as the permission level
        String currentLevel = admin.getAdminCode();
        
        // SUPER_ADMIN has access to everything
        if ("SUPER_ADMIN".equals(currentLevel)) return true;
        
        return currentLevel != null && currentLevel.equals(requiredPermission);
    }
}
