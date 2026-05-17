package com.groceryhub.service.interfaces;

import com.groceryhub.model.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface IProductService {
    Page<Product> getProducts(String category, String search, Pageable pageable);
    Product getProductById(Integer id);
    Product createProduct(Product product);
    Product updateProduct(Integer id, Product productDetails);
    void deleteProduct(Integer id);
    Product toggleProductStatus(Integer id);
}
