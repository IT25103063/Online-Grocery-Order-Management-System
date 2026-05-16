package com.smartgrocery.service;

import com.smartgrocery.dao.ProductDao;
import com.smartgrocery.model.NonPerishableProduct;
import com.smartgrocery.model.PerishableProduct;
import com.smartgrocery.model.Product;

import java.util.ArrayList;
import java.util.List;

public class ProductService {
    
    private ProductDao productDao;

    public ProductService() {
        this.productDao = new ProductDao();
    }

    public List<Product> getAllProducts() {
        return productDao.findAll();
    }

    public List<Product> searchProducts(String keyword) {
        List<Product> all = getAllProducts();
        if (keyword == null || keyword.trim().isEmpty()) return all;

        String lower = keyword.toLowerCase();
        List<Product> filtered = new ArrayList<>();
        
        for (Product p : all) {
            if (p.getName().toLowerCase().contains(lower) || 
                p.getType().toLowerCase().contains(lower) ||
                p.getId().toLowerCase().contains(lower)) {
                filtered.add(p);
            }
        }
        return filtered;
    }

    public Product getProductById(String id) {
        return productDao.findById(id);
    }

    /**
     * Validates and saves or updates a product.
     */
    public String saveOrUpdate(String id, String name, String priceStr, String stockStr, String type, String specialField) {
        if (name == null || name.trim().isEmpty()) return "Product name is required.";
        
        double price;
        int stock;
        
        try {
            price = Double.parseDouble(priceStr);
            stock = Integer.parseInt(stockStr);
        } catch (NumberFormatException e) {
            return "Price and Stock must be valid numbers.";
        }

        // Stock Validation
        if (price < 0) return "Price cannot be negative.";
        if (stock < 0) return "Stock cannot be negative.";

        Product product;
        if ("PERISHABLE".equals(type)) {
            if (specialField == null || specialField.trim().isEmpty()) return "Expiration Date is required.";
            product = new PerishableProduct(id, name, price, stock, specialField);
        } else {
            int warranty;
            try {
                warranty = Integer.parseInt(specialField);
                if (warranty < 0) return "Warranty cannot be negative.";
            } catch (NumberFormatException e) {
                return "Warranty must be a valid integer number of months.";
            }
            product = new NonPerishableProduct(id, name, price, stock, warranty);
        }

        boolean success;
        if (id == null || id.trim().isEmpty()) {
            success = productDao.save(product);
        } else {
            success = productDao.update(product);
        }

        return success ? "SUCCESS" : "Failed to save product to file.";
    }

    public boolean deleteProduct(String id) {
        return productDao.delete(id);
    }
}
