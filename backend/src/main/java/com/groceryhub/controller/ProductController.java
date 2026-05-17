package com.groceryhub.controller;

import com.groceryhub.dto.response.ApiResponse;
import com.groceryhub.dto.response.PaginatedResponse;
import com.groceryhub.model.Product;
import com.groceryhub.service.interfaces.IProductService;
import com.groceryhub.util.PaginationUtil;
import com.groceryhub.util.ResponseUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/products")
@RequiredArgsConstructor
public class ProductController {

    private final IProductService productService;

    @GetMapping
    public ResponseEntity<ApiResponse<PaginatedResponse<Product>>> getProducts(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "12") int limit,
            @RequestParam(required = false) String category,
            @RequestParam(required = false) String search
    ) {
        Page<Product> productPage = productService.getProducts(category, search, PageRequest.of(page - 1, limit));
        return ResponseUtil.success(PaginationUtil.buildPaginatedResponse(productPage));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<Product>> getProduct(@PathVariable Integer id) {
        return ResponseUtil.success(productService.getProductById(id));
    }

    @PostMapping
    public ResponseEntity<ApiResponse<Product>> createProduct(@RequestBody Product product) {
        return ResponseUtil.created(productService.createProduct(product), "Product created successfully");
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<Product>> updateProduct(@PathVariable Integer id, @RequestBody Product product) {
        return ResponseUtil.success(productService.updateProduct(id, product), "Product updated successfully");
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<String>> deleteProduct(@PathVariable Integer id) {
        productService.deleteProduct(id);
        return ResponseUtil.success("Product deleted successfully");
    }

    @PatchMapping("/{id}/toggle-status")
    public ResponseEntity<ApiResponse<Product>> toggleStatus(@PathVariable Integer id) {
        return ResponseUtil.success(productService.toggleProductStatus(id), "Status updated successfully");
    }
}
