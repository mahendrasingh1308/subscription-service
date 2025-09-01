package com.subscription.service.dto;

import lombok.*;

import java.time.LocalDate;

@Getter @Setter @Builder
@AllArgsConstructor @NoArgsConstructor
public class SubscriptionListItemDTO {
    private String fanName;

    // Creator info (from Plan)
    private String creatorId;
    private String creatorName;

    // Plan card
    private String planName;
    private Double price;
    private Integer durationInDays;

    // Dates
    private LocalDate startDate;
    private LocalDate endDate;

    // For UI cells
    private String durationText;
    private String status;
    private String autoRenewal;
    private Long remainingDays;
}
