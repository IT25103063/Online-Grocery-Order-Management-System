package com.groceryhub.model;

import com.groceryhub.enums.ChangeType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class InventoryLog {

                private Integer logId;

            private Product product;

        private Integer previousQuantity;

        private Integer newQuantity;

        private Integer changeAmount;

            private ChangeType changeType;

        private String reason;

            private Admin updatedBy;

        private LocalDateTime createdAt;

        protected void onCreate() {
        this.createdAt = LocalDateTime.now();
    }
}

