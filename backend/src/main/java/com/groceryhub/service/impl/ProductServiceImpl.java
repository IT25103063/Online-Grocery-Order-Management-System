package com.groceryhub.service.impl;

import com.groceryhub.dao.CategoryDAO;
import com.groceryhub.dao.ProductDAO;
import com.groceryhub.exception.ResourceNotFoundException;
import com.groceryhub.model.Category;
import com.groceryhub.model.Product;
import com.groceryhub.service.interfaces.IProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements IProductService {

    private final ProductDAO productDAO;
    private final CategoryDAO categoryDAO;

    @Override
    public Page<Product> getProducts(String category, String search, Pageable pageable) {
        return productDAO.filterProducts(category, search, pageable);
    }

    @Override
    public Product getProductById(Integer id) {
        return productDAO.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found with id: " + id));
    }

    @Override
        public Product createProduct(Product product) {
        if (product.getCategory() != null && product.getCategory().getCategoryId() != null) {
            Category category = categoryDAO.findById(product.getCategory().getCategoryId())
                    .orElseThrow(() -> new ResourceNotFoundException("Category not found"));
            product.setCategory(category);
        }
        return productDAO.save(product);
    }

    @Override
        public Product updateProduct(Integer id, Product productDetails) {
        Product product = getProductById(id);
        
        product.setName(productDetails.getName());
        product.setDescription(productDetails.getDescription());
        product.setUnit(productDetails.getUnit());
        product.setMrp(productDetails.getMrp());
        product.setSellingPrice(productDetails.getSellingPrice());
        product.setStockQuantity(productDetails.getStockQuantity());
        product.setMinStockThreshold(productDetails.getMinStockThreshold());
        product.setImageUrl(productDetails.getImageUrl());
        
        if (productDetails.getCategory() != null && productDetails.getCategory().getCategoryId() != null) {
            Category category = categoryDAO.findById(productDetails.getCategory().getCategoryId())
                    .orElseThrow(() -> new ResourceNotFoundException("Category not found"));
            product.setCategory(category);
        }

        return productDAO.save(product);
    }

    @Override
        public void deleteProduct(Integer id) {
        Product product = getProductById(id);
        productDAO.delete(product);
    }

    @Override
        public Product toggleProductStatus(Integer id) {
        Product product = getProductById(id);
        product.setIsActive(!product.getIsActive());
        return productDAO.save(product);
    }
}

