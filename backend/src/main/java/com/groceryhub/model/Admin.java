package com.groceryhub.model;

import com.groceryhub.enums.AdminRole;

import java.time.LocalDateTime;

public class Admin extends User {

    private Integer adminId;
    private AdminRole role = AdminRole.staff;
    private String avatarUrl;
    private Boolean isActive = true;
    private LocalDateTime lastLogin;

    public Admin() {
        super();
    }

    public Admin(String fullName, String email, String passwordHash, String phone, AdminRole role) {
        super(fullName, email, passwordHash, phone, null, null);
        this.role = role;
    }

    public Integer getAdminId() { return adminId; }
    public void setAdminId(Integer adminId) { this.adminId = adminId; }

    public AdminRole getRole() { return role; }
    public void setRole(AdminRole role) { this.role = role; }

    public String getAvatarUrl() { return avatarUrl; }
    public void setAvatarUrl(String avatarUrl) { this.avatarUrl = avatarUrl; }

    public Boolean getIsActive() { return isActive; }
    public void setIsActive(Boolean isActive) { this.isActive = isActive; }

    public LocalDateTime getLastLogin() { return lastLogin; }
    public void setLastLogin(LocalDateTime lastLogin) { this.lastLogin = lastLogin; }
}

