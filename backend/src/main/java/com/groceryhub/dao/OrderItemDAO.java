package com.groceryhub.dao;

import com.groceryhub.model.OrderItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface OrderItemDAO extends JpaRepository<OrderItem, Integer> {
    
    List<OrderItem> findByOrder_OrderId(Integer orderId);

    @Query(value = "SELECT p.name as productName, SUM(oi.quantity) as totalSold, SUM(oi.total_price) as totalRevenue " +
                   "FROM order_items oi " +
                   "JOIN orders o ON oi.order_id = o.order_id " +
                   "JOIN products p ON oi.product_id = p.product_id " +
                   "WHERE o.order_status != 'cancelled' AND o.order_date >= ?1 AND o.order_date <= ?2 " +
                   "GROUP BY p.product_id, p.name " +
                   "ORDER BY totalSold DESC " +
                   "LIMIT ?3", nativeQuery = true)
    List<Object[]> findTopSellingProducts(LocalDateTime startDate, LocalDateTime endDate, int limit);

    @Query(value = "SELECT c.name as categoryName, COUNT(DISTINCT o.order_id) as totalOrders, SUM(oi.total_price) as totalRevenue " +
                   "FROM order_items oi " +
                   "JOIN orders o ON oi.order_id = o.order_id " +
                   "JOIN products p ON oi.product_id = p.product_id " +
                   "JOIN categories c ON p.category_id = c.category_id " +
                   "WHERE o.order_status != 'cancelled' AND o.order_date >= ?1 AND o.order_date <= ?2 " +
                   "GROUP BY c.category_id, c.name", nativeQuery = true)
    List<Object[]> findCategorySales(LocalDateTime startDate, LocalDateTime endDate);
}
