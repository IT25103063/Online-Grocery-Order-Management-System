package com.freshgrocer.model;

public class AdminUser extends User {

    private String adminId;
    private String permissions;
    private String lastLogin;

    public AdminUser() {
        super();
        this.setRole("admin");
    }

    public AdminUser(String userId, String username,
                     String password, String email,
                     String status, String adminId,
                     String permissions, String lastLogin) {
        super(userId, username, password, email, "admin", status);
        this.adminId     = adminId;
        this.permissions = permissions;
        this.lastLogin   = lastLogin;
    }

    @Override
    public boolean login(String email, String password) {
        return this.getEmail().equals(email) &&
                this.getPassword().equals(password);
    }

    @Override
    public String displayInfo() {
        return super.displayInfo() + " | Permissions: " + permissions;
    }

    @Override
    public String toFileString() {
        return super.toFileString() + "," + adminId
                + "," + permissions + "," + lastLogin;
    }

    public static AdminUser fromFileString(String line) {
        String[] p = line.split(",");
        if (p.length < 9) return null;
        return new AdminUser(p[0],p[1],p[2],p[3],p[5],p[6],p[7],p[8]);
    }

    public String getAdminId()     { return adminId;     }
    public String getPermissions() { return permissions; }
    public String getLastLogin()   { return lastLogin;   }

    public void setAdminId(String adminId)         { this.adminId     = adminId;     }
    public void setPermissions(String permissions) { this.permissions = permissions; }
    public void setLastLogin(String lastLogin)     { this.lastLogin   = lastLogin;   }
}