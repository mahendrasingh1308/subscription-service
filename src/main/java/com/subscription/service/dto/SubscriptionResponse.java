package com.subscription.service.dto;

import lombok.*;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SubscriptionResponse {
    private Long id;
    private String userId;
    private String planName;
    private LocalDate startDate;
    private LocalDate endDate;
    private boolean isActive;
}