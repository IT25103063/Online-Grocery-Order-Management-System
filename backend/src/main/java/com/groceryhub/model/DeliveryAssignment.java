package com.groceryhub.model;

import com.groceryhub.enums.DeliveryStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DeliveryAssignment {

                private Integer assignmentId;

            private Order order;

            private DeliveryPersonnel personnel;

        private LocalDateTime assignedAt;

        private LocalDateTime estimatedDelivery;

        private LocalDateTime actualDelivery;

            private DeliveryStatus status = DeliveryStatus.assigned;

        private LocalDateTime createdAt;

        protected void onCreate() {
        this.createdAt = LocalDateTime.now();
        if (this.assignedAt == null) {
            this.assignedAt = LocalDateTime.now();
        }
    }
}

