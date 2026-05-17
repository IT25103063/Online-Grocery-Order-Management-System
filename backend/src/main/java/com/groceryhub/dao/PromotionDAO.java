package com.groceryhub.dao;

import com.groceryhub.model.Promotion;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PromotionDAO extends JpaRepository<Promotion, Integer> {
    
    Optional<Promotion> findByCode(String code);
    
    Page<Promotion> findByIsActive(Boolean isActive, Pageable pageable);
}
