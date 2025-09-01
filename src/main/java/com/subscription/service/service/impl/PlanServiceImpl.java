package com.subscription.service.service.impl;

import com.subscription.service.dto.PlanRequest;
import com.subscription.service.dto.PlanResponse;
import com.subscription.service.entity.Plan;
import com.subscription.service.repository.PlanRepository;
import com.subscription.service.service.PlanService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PlanServiceImpl implements PlanService {

    private final PlanRepository planRepository;

    @Override
    public PlanResponse createPlan(PlanRequest request, String creatorId,String firstName,String lastName) {
        Plan plan = Plan.builder()
                .name(request.getName())
                .description(request.getDescription())
                .price(request.getPrice())
                .durationInDays(request.getDurationInDays())
                .creatorId(creatorId)
                .firstName(firstName)
                .lastName(lastName)
                .build();

        Plan saved = planRepository.save(plan);
        return mapToResponse(saved);
    }

    @Override
    public List<PlanResponse> getAllPlans() {
        return planRepository.findAll()
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Override
    public List<PlanResponse> getPlansByCreator(String creatorId) {
        return planRepository.findByCreatorId(creatorId)
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    private PlanResponse mapToResponse(Plan plan) {
        return PlanResponse.builder()
                .id(plan.getId())
                .name(plan.getName())
                .description(plan.getDescription())
                .price(plan.getPrice())
                .durationInDays(plan.getDurationInDays())
                .creatorId(plan.getCreatorId())
                .build();
    }
}
