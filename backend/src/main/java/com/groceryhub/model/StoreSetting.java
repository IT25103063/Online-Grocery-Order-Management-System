package com.groceryhub.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class StoreSetting {

                private Integer settingId;

        private String settingKey;

        private String settingValue;

        private String description;

        private LocalDateTime updatedAt;

        protected void onCreate() {
        this.updatedAt = LocalDateTime.now();
    }

        protected void onUpdate() {
        this.updatedAt = LocalDateTime.now();
    }
}

