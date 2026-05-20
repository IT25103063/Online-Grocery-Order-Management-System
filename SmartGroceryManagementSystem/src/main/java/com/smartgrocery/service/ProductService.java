package com.smartgrocery.service;

import com.smartgrocery.dao.ProductDao;
import com.smartgrocery.model.NonPerishableProduct;
import com.smartgrocery.model.PerishableProduct;
import com.smartgrocery.model.Product;

import java.util.List;

public class ProductService {

    private ProductDao productDao;

    public ProductService() {
        this.productDao = new ProductDao();
    }

    public List<Product> getAllProducts() {
        return productDao.findAll();
    }

    public List<Product> getProductsByCategory(String category) {
        return productDao.findByCategory(category);
    }

    public Product getProductById(String id) {
        return productDao.findById(id);
    }

    public String saveOrUpdate(String id, String name, String priceStr, String stockStr,
                               String type, String specialField, String category) {
        if (name == null || name.trim().isEmpty()) return "Product name is required.";
        if (category == null || category.trim().isEmpty()) return "Category is required.";

        double price;
        int stock;
        try {
            price = Double.parseDouble(priceStr);
            stock = Integer.parseInt(stockStr);
        } catch (NumberFormatException e) {
            return "Price and Stock must be valid numbers.";
        }
        if (price < 0) return "Price cannot be negative.";
        if (stock < 0) return "Stock cannot be negative.";

        Product product;
        if ("PERISHABLE".equals(type)) {
            if (specialField == null || specialField.trim().isEmpty()) return "Expiration Date is required.";
            product = new PerishableProduct(id, name, price, stock, specialField, category);
        } else {
            int warranty;
            try {
                warranty = Integer.parseInt(specialField);
                if (warranty < 0) return "Warranty cannot be negative.";
            } catch (NumberFormatException e) {
                return "Warranty must be a valid number of months.";
            }
            product = new NonPerishableProduct(id, name, price, stock, warranty, category);
        }

        boolean success = (id == null || id.trim().isEmpty())
                ? productDao.save(product)
                : productDao.update(product);

        return success ? "SUCCESS" : "Failed to save product to file.";
    }

    public boolean deleteProduct(String id) {
        return productDao.delete(id);
    }
}
