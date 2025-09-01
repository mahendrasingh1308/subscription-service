package com.subscription.service.service;

import com.subscription.service.dto.SubscriptionListItemDTO;
import com.subscription.service.dto.UpdateSubscriptionRequest;

import java.util.List;

public interface DashboardService {



    List<SubscriptionListItemDTO> getAllSubscriptions();
    List<SubscriptionListItemDTO> getActiveSubscriptions();
    List<SubscriptionListItemDTO> getCancelledSubscriptions();
    List<SubscriptionListItemDTO> getExpiredSubscriptions();
    List<SubscriptionListItemDTO> getSoonExpiredSubscriptions();

    SubscriptionListItemDTO getById(Long id); // View
    SubscriptionListItemDTO update(Long id, UpdateSubscriptionRequest req); // Edit (autoRenewal)
    SubscriptionListItemDTO cancelSubscription(Long subscriptionId);
}
