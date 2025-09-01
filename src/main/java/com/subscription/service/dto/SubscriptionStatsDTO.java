    package com.subscription.service.dto;

    import lombok.AllArgsConstructor;
    import lombok.Data;
    import lombok.NoArgsConstructor;

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public class SubscriptionStatsDTO {
        private int totalActive;
        private double monthlyRevenue;
        private int expiringSoon;
        private int newToday;
    }