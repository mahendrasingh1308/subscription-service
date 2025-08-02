package com.subscription.service.service.Impl;

import com.subscription.service.dto.SubscriptionRequest;
import com.subscription.service.dto.SubscriptionResponse;
import com.subscription.service.entity.Plan;
import com.subscription.service.entity.Subscription;
import com.subscription.service.repository.PlanRepository;
import com.subscription.service.repository.SubscriptionRepository;
import com.subscription.service.service.SubscriptionService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
@RequiredArgsConstructor
public class SubscriptionServiceImpl implements SubscriptionService {

    private final PlanRepository planRepository;
    private final SubscriptionRepository subscriptionRepository;

    @Override
    public SubscriptionResponse createSubscription(String userId, Long planId) {
        Plan plan = planRepository.findById(planId)
                .orElseThrow(() -> new RuntimeException("Plan not found"));

        Subscription subscription = Subscription.builder()
                .userId(userId)
                .plan(plan)
                .startDate(LocalDate.now())
                .endDate(LocalDate.now().plusDays(plan.getDurationInDays()))
                .active(true)
                .build();

        Subscription saved = subscriptionRepository.save(subscription);

        return SubscriptionResponse.builder()
                .id(saved.getId())
                .userId(saved.getUserId())
                .planName(plan.getName())
                .startDate(saved.getStartDate())
                .endDate(saved.getEndDate())
                .isActive(saved.isActive())
                .build();
    }

    @Override
    public SubscriptionResponse getUserSubscription(String userId) {
        Subscription subscription = subscriptionRepository.findByUserId(userId)
                .orElseThrow(() -> new RuntimeException("No subscription found for user"));

        return SubscriptionResponse.builder()
                .id(subscription.getId())
                .userId(subscription.getUserId())
                .planName(subscription.getPlan().getName())
                .startDate(subscription.getStartDate())
                .endDate(subscription.getEndDate())
                .isActive(subscription.isActive())
                .build();
    }
}
