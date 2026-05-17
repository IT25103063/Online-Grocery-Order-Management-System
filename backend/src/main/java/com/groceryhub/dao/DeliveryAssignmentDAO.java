package com.groceryhub.dao;

import com.groceryhub.enums.DeliveryStatus;
import com.groceryhub.model.DeliveryAssignment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DeliveryAssignmentDAO extends JpaRepository<DeliveryAssignment, Integer> {
    
    List<DeliveryAssignment> findByPersonnel_PersonnelIdAndStatusIn(Integer personnelId, List<DeliveryStatus> statuses);
    
    List<DeliveryAssignment> findByStatus(DeliveryStatus status);
}
