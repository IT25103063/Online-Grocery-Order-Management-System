package com.groceryhub.dao;

import com.groceryhub.model.InventoryLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface InventoryLogDAO extends JpaRepository<InventoryLog, Integer> {
    
    List<InventoryLog> findByProduct_ProductIdOrderByCreatedAtDesc(Integer productId);
    
    List<InventoryLog> findTop50ByOrderByCreatedAtDesc();
}
