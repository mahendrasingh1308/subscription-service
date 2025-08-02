package com.subscription.service.controller;


import com.subscription.service.dto.SubscriptionResponse;
import com.subscription.service.security.JwtUtil;
import com.subscription.service.service.SubscriptionService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/subscriptions")
@RequiredArgsConstructor
public class SubscriptionController {

    private final SubscriptionService subscriptionService;
    private final JwtUtil jwtUtil;

    @PostMapping("/subscribe/{planId}")
    public SubscriptionResponse subscribe(@PathVariable Long planId,
                                          HttpServletRequest request) {
        String token = request.getHeader("Authorization").substring(7);
        String userId = jwtUtil.extractUsername(token);
        return subscriptionService.createSubscription(userId, planId);
    }

    @GetMapping("/{userId}")
    public SubscriptionResponse getUserSubscription(@PathVariable String userId) {
        return subscriptionService.getUserSubscription(userId);
    }
}