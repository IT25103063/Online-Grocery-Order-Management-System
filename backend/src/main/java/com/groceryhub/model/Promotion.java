package com.groceryhub.model;

import com.groceryhub.enums.DiscountType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Promotion {

                private Integer promotionId;

        private String code;

            private DiscountType discountType;

        private BigDecimal discountValue;

        private BigDecimal minOrderAmount = BigDecimal.ZERO;

        private BigDecimal maxDiscountAmount;

        private LocalDateTime validFrom;

        private LocalDateTime validTo;

        private Integer usageLimit;

        private Integer usageCount = 0;

        private Boolean isActive = true;

            private Admin createdBy;

        private LocalDateTime createdAt;

        protected void onCreate() {
        this.createdAt = LocalDateTime.now();
    }
}

