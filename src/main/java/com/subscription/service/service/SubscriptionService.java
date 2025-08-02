package com.subscription.service.service;


import com.subscription.service.dto.SubscriptionRequest;
import com.subscription.service.dto.SubscriptionResponse;

public interface SubscriptionService {
    SubscriptionResponse createSubscription(String userId, Long planId);
    SubscriptionResponse getUserSubscription(String userId);

}