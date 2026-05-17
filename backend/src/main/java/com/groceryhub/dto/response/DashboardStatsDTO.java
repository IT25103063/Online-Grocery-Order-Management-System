package com.groceryhub.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DashboardStatsDTO {
    private Integer totalOrdersToday;
    private Double revenueToday;
    private Integer pendingOrders;
    private Integer activeCustomers;
}
