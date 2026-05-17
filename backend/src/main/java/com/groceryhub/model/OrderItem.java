package com.groceryhub.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderItem {

                private Integer orderItemId;

            private Order order;

            private Product product;

        private Integer quantity;

        private BigDecimal unitPrice;

        private BigDecimal totalPrice;

        private LocalDateTime createdAt;

        protected void onCreate() {
        this.createdAt = LocalDateTime.now();
    }
}

