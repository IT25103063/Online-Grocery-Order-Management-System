package com.groceryhub.dao;

import com.groceryhub.model.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductDAO extends JpaRepository<Product, Integer> {

    @Query("SELECT p FROM Product p WHERE " +
           "(:category IS NULL OR p.category.name = :category) AND " +
           "(:search IS NULL OR LOWER(p.name) LIKE LOWER(CONCAT('%', :search, '%')))")
    Page<Product> filterProducts(String category, String search, Pageable pageable);

    @Query(value = "SELECT * FROM products WHERE stock_quantity <= min_stock_threshold", nativeQuery = true)
    List<Product> findLowStockProducts();

    @Query(value = "SELECT * FROM products WHERE stock_quantity = 0", nativeQuery = true)
    List<Product> findOutOfStockProducts();
    
    boolean existsByCategory_CategoryId(Integer categoryId);
}
