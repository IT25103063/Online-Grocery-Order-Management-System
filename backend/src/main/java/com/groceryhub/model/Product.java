package com.groceryhub.model;



import java.math.BigDecimal;
import java.time.LocalDateTime;

public class Product {

    private Integer id;
    private String category;
    private String name;
    private String description;
    private String unit;
    private BigDecimal mrp;
    private BigDecimal price;
    private Integer stock;
    private Integer minStock;
    private String image;
    private String status;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public Product() {
    }

    public Product(Integer id, String category, String name, String description, String unit, BigDecimal mrp, BigDecimal price, Integer stock, Integer minStock, String image, String status, LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.id = id;
        this.category = category;
        this.name = name;
        this.description = description;
        this.unit = unit;
        this.mrp = mrp;
        this.price = price;
        this.stock = stock;
        this.minStock = minStock;
        this.image = image;
        this.status = status;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    // Modern exact JSON field Getters & Setters
    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }

    public String getCategoryName() { return category; }
    public void setCategoryName(String category) { this.category = category; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public String getUnit() { return unit; }
    public void setUnit(String unit) { this.unit = unit; }

    public BigDecimal getMrp() { return mrp; }
    public void setMrp(BigDecimal mrp) { this.mrp = mrp; }

    public BigDecimal getPrice() { return price; }
    public void setPrice(BigDecimal price) { this.price = price; }

    public Integer getStock() { return stock; }
    public void setStock(Integer stock) { this.stock = stock; }

    public Integer getMinStock() { return minStock; }
    public void setMinStock(Integer minStock) { this.minStock = minStock; }

    public String getImage() { return image; }
    public void setImage(String image) { this.image = image; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }

    // Compatibility Adapters for older Java DAO / Service method signatures
    public Integer getProductId() { return id; }
    public void setProductId(Integer productId) { this.id = productId; }

    public Category getCategory() {
        if (category == null) return null;
        Category cat = new Category();
        cat.setName(category);
        return cat;
    }
    public void setCategory(Category cat) {
        this.category = (cat != null) ? cat.getName() : null;
    }

    public BigDecimal getSellingPrice() { return price; }
    public void setSellingPrice(BigDecimal sellingPrice) { this.price = sellingPrice; }

    public Integer getStockQuantity() { return stock != null ? stock : 0; }
    public void setStockQuantity(Integer stockQuantity) { this.stock = stockQuantity; }

    public Integer getMinStockThreshold() { return minStock != null ? minStock : 10; }
    public void setMinStockThreshold(Integer minStockThreshold) { this.minStock = minStockThreshold; }

    public String getImageUrl() { return image; }
    public void setImageUrl(String imageUrl) { this.image = imageUrl; }

    public Boolean getIsActive() { return "Active".equalsIgnoreCase(status); }
    public void setIsActive(Boolean isActive) { this.status = isActive ? "Active" : "Inactive"; }

    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    protected void onUpdate() {
        this.updatedAt = LocalDateTime.now();
    }
}

