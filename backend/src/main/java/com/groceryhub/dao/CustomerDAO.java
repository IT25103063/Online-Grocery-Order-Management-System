package com.groceryhub.dao;

import com.groceryhub.model.Customer;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CustomerDAO extends JpaRepository<Customer, Integer> {
    
    Optional<Customer> findByEmail(String email);
    
    boolean existsByEmail(String email);
    
    boolean existsByPhone(String phone);

    @Query(value = "SELECT * FROM customers WHERE full_name LIKE %?1% OR email LIKE %?1% OR phone LIKE %?1%", nativeQuery = true)
    Page<Customer> searchCustomers(String search, Pageable pageable);
}
