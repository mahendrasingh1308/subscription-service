package com.subscription.service.controller;

import com.subscription.service.dto.SubscriptionResponse;
import com.subscription.service.security.JwtUtil;
import com.subscription.service.service.SubscriptionService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/subscriptions")
@RequiredArgsConstructor
public class SubscriptionController {

    private final SubscriptionService subscriptionService;
    private final JwtUtil jwtUtil;

    /**
     * Subscribe authenticated user to a plan.
     */
    @PostMapping("/subscribe/{planId}")
    public SubscriptionResponse subscribe(@PathVariable Long planId,
                                          HttpServletRequest request) {
        String token = request.getHeader("Authorization").substring(7);
        String userId = jwtUtil.extractUsername(token);
        String firstName = jwtUtil.extractFirstName(token);
        String lastName = jwtUtil.extractLastName(token);

        return subscriptionService.createSubscription(userId, planId, firstName, lastName, false);
    }

    /**
     * Get all subscriptions for a user.
     */
    @GetMapping("/{userId}")
    public List<SubscriptionResponse> getUserSubscription(@PathVariable String userId) {
        return subscriptionService.getUserSubscription(userId);
    }

    /**
     * Cancel a subscription by ID.
     */
    @PostMapping("/cancel/{subscriptionId}")
    public String cancelSubscription(@PathVariable Long subscriptionId) {
        subscriptionService.cancelSubscription(subscriptionId);
        return "Subscription cancelled successfully";
    }
}