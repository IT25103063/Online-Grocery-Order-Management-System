package com.groceryhub.dao;

import com.groceryhub.model.Product;
import com.groceryhub.util.TextFileDatabase;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Repository
@RequiredArgsConstructor
@SuppressWarnings("null")
public class ProductDAO {

    private final TextFileDatabase db;
    private final String FILE_NAME = "products";

    public List<Product> findAll() {
        return db.loadData(FILE_NAME, Product.class);
    }

    public Optional<Product> findById(Integer id) {
        return findAll().stream().filter(p -> p.getProductId().equals(id)).findFirst();
    }

    public Product save(Product product) {
        List<Product> products = findAll();
        if (product.getProductId() == null) {
            product.setProductId(products.size() > 0 ? products.stream().mapToInt(Product::getProductId).max().orElse(0) + 1 : 1);
            products.add(product);
        } else {
            for (int i = 0; i < products.size(); i++) {
                if (products.get(i).getProductId().equals(product.getProductId())) {
                    products.set(i, product);
                    break;
                }
            }
        }
        db.saveData(FILE_NAME, products);
        return product;
    }

    public void delete(Product product) {
        List<Product> products = findAll();
        products.removeIf(p -> p.getProductId().equals(product.getProductId()));
        db.saveData(FILE_NAME, products);
    }

    public Page<Product> filterProducts(String category, String search, Pageable pageable) {
        List<Product> all = findAll().stream()
            .filter(p -> category == null || (p.getCategory() != null && category.equals(p.getCategory().getName())))
            .filter(p -> search == null || p.getName().toLowerCase().contains(search.toLowerCase()))
            .collect(Collectors.toList());
        int start = (int) pageable.getOffset();
        int end = Math.min((start + pageable.getPageSize()), all.size());
        if (start > all.size()) return new PageImpl<>(List.of(), pageable, all.size());
        return new PageImpl<>(all.subList(start, end), pageable, all.size());
    }

    public List<Product> findLowStockProducts() {
        return findAll().stream()
            .filter(p -> p.getStockQuantity() <= p.getMinStockThreshold())
            .collect(Collectors.toList());
    }

    public List<Product> findOutOfStockProducts() {
        return findAll().stream()
            .filter(p -> p.getStockQuantity() == 0)
            .collect(Collectors.toList());
    }

    public boolean existsByCategory_CategoryId(Integer categoryId) {
        return findAll().stream().anyMatch(p -> p.getCategory() != null && categoryId.equals(p.getCategory().getCategoryId()));
    }
}
