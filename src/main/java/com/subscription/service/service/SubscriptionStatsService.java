package com.subscription.service.service;
public interface SubscriptionStatsService {
    Integer getTotalActiveSubscriptions();

    Double getMonthlyRevenue();

    Integer getExpiringSoonCount();

    Integer getNewTodayCount();
}

