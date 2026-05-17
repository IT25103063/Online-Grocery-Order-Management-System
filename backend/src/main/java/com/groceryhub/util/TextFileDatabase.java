package com.groceryhub.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.stereotype.Component;
import jakarta.annotation.PostConstruct;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Component
public class TextFileDatabase {

    private final ObjectMapper mapper;
    private final String dataDir;

    public TextFileDatabase() {
        this.mapper = new ObjectMapper();
        this.mapper.registerModule(new JavaTimeModule());
        
        // Dynamically resolve data folder to the root project directory
        String userDir = System.getProperty("user.dir");
        if (userDir.endsWith("backend")) {
            this.dataDir = "../data/";
        } else {
            this.dataDir = "data/";
        }
    }

    @PostConstruct
    public void init() {
        File dir = new File(dataDir);
        if (!dir.exists()) {
            dir.mkdirs();
        }
        
        // Seed Admins if not present
        File adminFile = new File(dataDir + "admins.txt");
        if (!adminFile.exists() || adminFile.length() == 0) {
            List<com.groceryhub.model.Admin> defaultAdmins = new ArrayList<>();
            
            com.groceryhub.model.Admin admin = new com.groceryhub.model.Admin();
            admin.setAdminId(1);
            admin.setFullName("Admin User");
            admin.setEmail("admin@grocery.com");
            admin.setPasswordHash("$2a$10$N9qo8uLOickgx2ZMRZoMye.IjZQQp7E0CzGJOqHYk1NOBokTFnjXu");
            admin.setRole(com.groceryhub.enums.AdminRole.super_admin);
            admin.setPhone("+91 99999 00000");
            admin.setIsActive(true);
            admin.setCreatedAt(java.time.LocalDateTime.now());
            admin.setUpdatedAt(java.time.LocalDateTime.now());
            defaultAdmins.add(admin);

            com.groceryhub.model.Admin manager = new com.groceryhub.model.Admin();
            manager.setAdminId(2);
            manager.setFullName("Store Manager");
            manager.setEmail("manager@grocery.com");
            manager.setPasswordHash("$2a$10$N9qo8uLOickgx2ZMRZoMye.IjZQQp7E0CzGJOqHYk1NOBokTFnjXu");
            manager.setRole(com.groceryhub.enums.AdminRole.manager);
            manager.setPhone("+91 88888 00000");
            manager.setIsActive(true);
            manager.setCreatedAt(java.time.LocalDateTime.now());
            manager.setUpdatedAt(java.time.LocalDateTime.now());
            defaultAdmins.add(manager);

            saveData("admins", defaultAdmins);
        }

        // Seed Categories if not present
        File catFile = new File(dataDir + "categories.txt");
        if (!catFile.exists() || catFile.length() == 0) {
            List<com.groceryhub.model.Category> defaultCategories = new ArrayList<>();
            String[][] seedCats = {
                {"Fruits", "Fresh seasonal fruits", "🍎"},
                {"Vegetables", "Farm fresh vegetables", "🥦"},
                {"Dairy", "Milk, cheese, and dairy products", "🧀"},
                {"Bakery", "Fresh bread and baked goods", "🍞"},
                {"Beverages", "Drinks and juices", "🥤"},
                {"Snacks", "Chips, cookies and snacks", "🍪"}
            };
            for (int i = 0; i < seedCats.length; i++) {
                com.groceryhub.model.Category cat = new com.groceryhub.model.Category();
                cat.setCategoryId(i + 1);
                cat.setName(seedCats[i][0]);
                cat.setDescription(seedCats[i][1]);
                cat.setImageUrl(seedCats[i][2]);
                cat.setIsActive(true);
                cat.setCreatedAt(java.time.LocalDateTime.now());
                cat.setUpdatedAt(java.time.LocalDateTime.now());
                defaultCategories.add(cat);
            }
            saveData("categories", defaultCategories);
        }
    }

    public <T> List<T> loadData(String filename, Class<T> clazz) {
        File file = new File(dataDir + filename + ".txt");
        if (!file.exists()) {
            return new ArrayList<>();
        }
        try {
            return mapper.readValue(file, mapper.getTypeFactory().constructCollectionType(List.class, clazz));
        } catch (IOException e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    public <T> void saveData(String filename, List<T> data) {
        File file = new File(dataDir + filename + ".txt");
        try {
            mapper.writerWithDefaultPrettyPrinter().writeValue(file, data);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
