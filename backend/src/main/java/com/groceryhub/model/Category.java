package com.groceryhub.model;


import java.time.LocalDateTime;

public class Category {

    private Integer id;
    private String name;
    private String description;
    private String image;
    private String status;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public Category() {
    }

    public Category(Integer id, String name, String description, String image, String status, LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.image = image;
        this.status = status;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    // Modern exact JSON field Getters & Setters
    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public String getImage() { return image; }
    public void setImage(String image) { this.image = image; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }

    // Compatibility Adapters for older Java DAO / Service method signatures
    public Integer getCategoryId() { return id; }
    public void setCategoryId(Integer categoryId) { this.id = categoryId; }

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

