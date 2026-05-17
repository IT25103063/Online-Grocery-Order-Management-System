package com.groceryhub.dao;

import com.groceryhub.model.Admin;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AdminDAO extends JpaRepository<Admin, Integer> {
    
    Optional<Admin> findByEmail(String email);
    
    @Query(value = "SELECT * FROM admins WHERE email = ?1 AND is_active = true", nativeQuery = true)
    Optional<Admin> findActiveByEmail(String email);
    
    boolean existsByEmail(String email);
}
