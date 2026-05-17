package com.groceryhub.dao;

import com.groceryhub.model.OrderStatusHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderStatusHistoryDAO extends JpaRepository<OrderStatusHistory, Integer> {
    
    List<OrderStatusHistory> findByOrder_OrderIdOrderByCreatedAtAsc(Integer orderId);
}
