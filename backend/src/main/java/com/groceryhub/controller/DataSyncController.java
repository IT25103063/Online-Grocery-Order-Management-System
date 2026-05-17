package com.groceryhub.controller;


import com.groceryhub.util.TextFileDatabase;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/data")
@RequiredArgsConstructor
@CrossOrigin(origins = "*") // Allows the frontend to connect locally
public class DataSyncController {

    private final TextFileDatabase db;

    @GetMapping
    public ResponseEntity<Map<String, Object>> getAllData() {
        Map<String, Object> data = new HashMap<>();
        data.put("products", db.loadData("products", Map.class));
        data.put("categories", db.loadData("categories", Map.class));
        data.put("orders", db.loadData("orders", Map.class));
        data.put("customers", db.loadData("customers", Map.class));
        data.put("promotions", db.loadData("promotions", Map.class));
        data.put("deliveryPersonnel", db.loadData("deliveryPersonnel", Map.class));
        data.put("stockHistory", db.loadData("stockHistory", Map.class));
        return ResponseEntity.ok(data);
    }

    @PostMapping
    public ResponseEntity<Map<String, String>> saveAllData(@RequestBody Map<String, Object> data) {
        try {
            // Save each list directly using the dynamically resolved db path
            for (Map.Entry<String, Object> entry : data.entrySet()) {
                if (entry.getValue() instanceof List) {
                    db.saveData(entry.getKey(), (List<?>) entry.getValue());
                }
            }
            return ResponseEntity.ok(Map.of("success", "true", "message", "Data synchronized with Java backend successfully"));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body(Map.of("error", "Failed to sync data"));
        }
    }
}
