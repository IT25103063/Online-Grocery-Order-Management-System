package com.groceryhub.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CustomerAddress {

                private Integer addressId;

            private Customer customer;

        private String addressLine1;

        private String addressLine2;

        private String city;

        private String state;

        private String zipCode;

        private String landmark;

        private String addressType = "home"; // ENUM: 'home', 'work', 'other'

        private Boolean isDefault = false;

        private LocalDateTime createdAt;

        protected void onCreate() {
        this.createdAt = LocalDateTime.now();
    }
}

