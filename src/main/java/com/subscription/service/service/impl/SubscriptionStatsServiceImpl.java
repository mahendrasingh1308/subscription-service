package com.subscription.service.service.impl;

import com.subscription.service.service.SubscriptionStatsService;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SubscriptionStatsServiceImpl implements SubscriptionStatsService {

    private final JdbcTemplate jdbcTemplate;

    @Override
    public Integer getTotalActiveSubscriptions() {
        Integer result = jdbcTemplate.queryForObject(
                "SELECT COUNT(*) FROM subscription WHERE active = true",
                Integer.class);
        return result != null ? result : 0;
    }

    @Override
    public Double getMonthlyRevenue() {
        Double result = jdbcTemplate.queryForObject(
                "SELECT COALESCE(SUM(p.price), 0) FROM subscription s " +
                        "JOIN plan p ON s.plan_id = p.id " +
                        "WHERE s.active = true " +
                        "AND s.start_date >= DATE_TRUNC('month', CURRENT_DATE) " +
                        "AND s.start_date < DATE_TRUNC('month', CURRENT_DATE) + INTERVAL '1 month'",
                Double.class);
        return result != null ? result : 0.0;
    }

    @Override
    public Integer getExpiringSoonCount() {
        Integer result = jdbcTemplate.queryForObject(
                "SELECT COUNT(*) FROM subscription WHERE active = true " +
                        "AND end_date BETWEEN CURRENT_DATE AND (CURRENT_DATE + INTERVAL '7 days')",
                Integer.class);
        return result != null ? result : 0;
    }

    @Override
    public Integer getNewTodayCount() {
        Integer result = jdbcTemplate.queryForObject(
                "SELECT COUNT(*) FROM subscription WHERE DATE(start_date) = CURRENT_DATE",
                Integer.class);
        return result != null ? result : 0;
    }
}
