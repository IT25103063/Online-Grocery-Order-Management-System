package com.groceryhub.model;

import com.groceryhub.enums.PersonnelStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DeliveryPersonnel {

                private Integer personnelId;

        private String fullName;

        private String phone;

        private String email;

        private String vehicleType;

        private String vehicleNumber;

        private Boolean isActive = true;

            private PersonnelStatus currentStatus = PersonnelStatus.available;

        private LocalDateTime createdAt;

        protected void onCreate() {
        this.createdAt = LocalDateTime.now();
    }
}

