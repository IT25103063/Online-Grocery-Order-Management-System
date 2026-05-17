package com.groceryhub.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderStatusHistory {

                private Integer historyId;

            private Order order;

        private String status;

        private String notes;

            private Admin changedBy;

        private LocalDateTime createdAt;

        protected void onCreate() {
        this.createdAt = LocalDateTime.now();
    }
}

