package com.subscription.service.service.impl;

import com.subscription.service.dto.SubscriptionResponse;
import com.subscription.service.entity.Plan;
import com.subscription.service.entity.Subscription;
import com.subscription.service.entity.SubscriptionStatus;
import com.subscription.service.repository.PlanRepository;
import com.subscription.service.repository.SubscriptionRepository;
import com.subscription.service.service.SubscriptionService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Implementation of {@link SubscriptionService} for managing user subscriptions.
 * <p>
 * Provides functionality for:
 * <ul>
 *     <li>Creating new subscriptions</li>
 *     <li>Fetching all subscriptions of a user</li>
 *     <li>Cancelling subscriptions</li>
 * </ul>
 * <p>
 * Auto-renewal status is handled as a boolean in the entity, but returned as "on"/"off" in responses.
 * Only one active subscription per creator is allowed for a user.
 */
@Service
@RequiredArgsConstructor
public class SubscriptionServiceImpl implements SubscriptionService {

    private final PlanRepository planRepository;
    private final SubscriptionRepository subscriptionRepository;

    @Override
    public SubscriptionResponse createSubscription(String userId, Long planId, String firstName, String lastName, Boolean autoRenewal) {

        Plan plan = planRepository.findById(planId)
                .orElseThrow(() -> new RuntimeException("Plan not found"));

        // Check if user already has an active subscription for this creator
        subscriptionRepository.findByUserId(userId).stream()
                .filter(s -> s.getStatus() == SubscriptionStatus.ACTIVE
                        && s.getPlan().getCreatorId().equals(plan.getCreatorId()))
                .findFirst()
                .ifPresent(s -> {
                    throw new RuntimeException("User already has an active subscription to this creator");
                });

        Subscription subscription = Subscription.builder()
                .userId(userId)
                .plan(plan)
                .startDate(LocalDate.now())
                .endDate(LocalDate.now().plusDays(plan.getDurationInDays()))
                .firstName(firstName)
                .lastName(lastName)
                .status(SubscriptionStatus.ACTIVE)
                .active(true)
                .autoRenewal(autoRenewal != null && autoRenewal)
                .build();

        Subscription saved = subscriptionRepository.save(subscription);
        return buildResponse(saved);
    }

    @Override
    public List<SubscriptionResponse> getUserSubscription(String userId) {
        List<Subscription> subscriptions = subscriptionRepository.findByUserId(userId);

        if (subscriptions.isEmpty()) {
            throw new RuntimeException("No subscription found for user");
        }

        return subscriptions.stream()
                .map(this::buildResponse)
                .collect(Collectors.toList());
    }

    @Override
    public void cancelSubscription(Long subscriptionId) {
        Subscription subscription = subscriptionRepository.findById(subscriptionId)
                .orElseThrow(() -> new RuntimeException("Subscription not found"));

        subscription.setStatus(SubscriptionStatus.CANCELLED);
        subscription.setActive(false);
        subscriptionRepository.save(subscription);
    }

    /**
     * Helper method to convert Subscription entity to SubscriptionResponse.
     */
    private SubscriptionResponse buildResponse(Subscription subscription) {
        Plan plan = subscription.getPlan();
        return SubscriptionResponse.builder()
                .id(subscription.getId())
                .userId(subscription.getUserId())
                .planName(plan.getName())
                .price(plan.getPrice())
                .durationInDays(plan.getDurationInDays())
                .status(subscription.getStatus().name())
                .startDate(subscription.getStartDate())
                .endDate(subscription.getEndDate())
                .isActive(subscription.getStatus() == SubscriptionStatus.ACTIVE)
                .autoRenewal(subscription.getAutoRenewal() != null && subscription.getAutoRenewal() ? "on" : "off")
                .build();
    }
}