package com.groceryhub.model;

import com.groceryhub.enums.AdminRole;
import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@Entity
@Table(name = "admins")
public class Admin extends User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "admin_id")
    private Integer adminId;

    @Enumerated(EnumType.STRING)
    @Column(name = "role")
    private AdminRole role = AdminRole.staff;

    @Column(name = "avatar_url")
    private String avatarUrl;

    @Column(name = "is_active")
    private Boolean isActive = true;

    @Column(name = "last_login")
    private LocalDateTime lastLogin;

    public Admin(String fullName, String email, String passwordHash, String phone, AdminRole role) {
        super(fullName, email, passwordHash, phone, null, null);
        this.role = role;
    }
}
