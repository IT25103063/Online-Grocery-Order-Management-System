package com.groceryhub.dao;

import com.groceryhub.enums.PersonnelStatus;
import com.groceryhub.model.DeliveryPersonnel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DeliveryPersonnelDAO extends JpaRepository<DeliveryPersonnel, Integer> {
    
    List<DeliveryPersonnel> findByCurrentStatus(PersonnelStatus status);
    
    List<DeliveryPersonnel> findByIsActiveTrue();
}
