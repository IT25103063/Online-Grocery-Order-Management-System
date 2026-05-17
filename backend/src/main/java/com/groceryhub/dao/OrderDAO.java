package com.groceryhub.dao;

import com.groceryhub.enums.OrderStatus;
import com.groceryhub.model.Order;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface OrderDAO extends JpaRepository<Order, Integer> {
    
    Optional<Order> findByOrderNumber(String orderNumber);
    
    List<Order> findByCustomer_CustomerId(Integer customerId);

    @Query("SELECT o FROM Order o WHERE " +
           "(:status IS NULL OR o.orderStatus = :status) AND " +
           "(:search IS NULL OR o.orderNumber LIKE %:search% OR LOWER(o.customer.fullName) LIKE LOWER(CONCAT('%', :search, '%'))) AND " +
           "(cast(:startDate as timestamp) IS NULL OR o.orderDate >= :startDate) AND " +
           "(cast(:endDate as timestamp) IS NULL OR o.orderDate <= :endDate)")
    Page<Order> filterOrders(OrderStatus status, String search, LocalDateTime startDate, LocalDateTime endDate, Pageable pageable);

    @Query("SELECT o FROM Order o WHERE o.orderStatus IN :statuses")
    List<Order> findByOrderStatusIn(List<OrderStatus> statuses);

    @Query(value = "SELECT SUM(grand_total) FROM orders WHERE order_status != 'cancelled' AND order_date >= ?1 AND order_date <= ?2", nativeQuery = true)
    Double calculateRevenue(LocalDateTime startDate, LocalDateTime endDate);

    @Query(value = "SELECT COUNT(*) FROM orders WHERE order_date >= ?1 AND order_date <= ?2", nativeQuery = true)
    Integer countOrders(LocalDateTime startDate, LocalDateTime endDate);
}
