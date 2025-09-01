package com.subscription.service.service;

import com.subscription.service.dto.SubscriptionResponse;
import java.util.List;

/**
 * Service interface for managing user subscriptions.
 */
public interface SubscriptionService {

    /**
     * Creates a subscription for a user to a specific plan.
     *
     * @param userId      the ID of the user
     * @param planId      the ID of the plan
     * @param firstName   user's first name
     * @param lastName    user's last name
     * @param autoRenewal whether subscription should auto-renew
     * @return the created subscription response
     */
    SubscriptionResponse createSubscription(
            String userId, Long planId, String firstName,
            String lastName, Boolean autoRenewal);

    /**
     * Retrieves all subscriptions for a user.
     *
     * @param userId the ID of the user
     * @return list of subscription responses
     */
    List<SubscriptionResponse> getUserSubscription(String userId);

    /**
     * Cancels a subscription by its ID.
     *
     * @param subscriptionId the ID of the subscription
     */
    void cancelSubscription(Long subscriptionId);
}