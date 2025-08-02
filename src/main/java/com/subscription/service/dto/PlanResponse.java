package com.subscription.service.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PlanResponse {
    private Long id;
    private String name;
    private String description;
    private Double price;
    private Integer durationInDays;

    private String creatorId;
}
