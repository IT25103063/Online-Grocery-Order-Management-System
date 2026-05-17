package com.groceryhub.dao;

import com.groceryhub.model.Admin;
import com.groceryhub.util.TextFileDatabase;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class AdminDAO {

    private final TextFileDatabase db;
    private final String FILE_NAME = "admins";

    public List<Admin> findAll() {
        return db.loadData(FILE_NAME, Admin.class);
    }

    public Optional<Admin> findById(Integer id) {
        return findAll().stream().filter(a -> a.getAdminId().equals(id)).findFirst();
    }

    public Optional<Admin> findByEmail(String email) {
        return findAll().stream().filter(a -> a.getEmail().equals(email)).findFirst();
    }

    public Optional<Admin> findActiveByEmail(String email) {
        return findAll().stream().filter(a -> a.getEmail().equals(email) && a.getIsActive()).findFirst();
    }

    public boolean existsByEmail(String email) {
        return findAll().stream().anyMatch(a -> a.getEmail().equals(email));
    }

    public Admin save(Admin admin) {
        List<Admin> admins = findAll();
        if (admin.getAdminId() == null) {
            admin.setAdminId(admins.size() > 0 ? admins.stream().mapToInt(Admin::getAdminId).max().orElse(0) + 1 : 1);
            admins.add(admin);
        } else {
            for (int i = 0; i < admins.size(); i++) {
                if (admins.get(i).getAdminId().equals(admin.getAdminId())) {
                    admins.set(i, admin);
                    break;
                }
            }
        }
        db.saveData(FILE_NAME, admins);
        return admin;
    }
}
