package com.subscription.service.controller;

import com.subscription.service.dto.SubscriptionListItemDTO;
import com.subscription.service.dto.UpdateSubscriptionRequest;
import com.subscription.service.service.DashboardService;
import com.subscription.service.service.SubscriptionStatsService;
import com.subscription.service.service.ExportService;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

/**
 * REST Controller for handling subscription statistics,
 * admin-related subscription management, and data export functionality.
 */
@RestController
@RequestMapping("/api/dashboard")
@RequiredArgsConstructor
@Slf4j
public class SubscriptionStatsController {

    private final SubscriptionStatsService subscriptionStatsService;
    private final DashboardService dashboardService;
    private final ExportService exportService;

    // ----------------- Stats Endpoints -----------------

    /**
     * Get the total number of active subscriptions.
     *
     * @return total active subscriptions
     */
    @GetMapping("/total-active")
    public Integer getTotalActive() {
        return subscriptionStatsService.getTotalActiveSubscriptions();
    }

    /**
     * Get the total monthly revenue from subscriptions.
     *
     * @return monthly revenue in Double
     */
    @GetMapping("/monthly-revenue")
    public Double getMonthlyRevenue() {
        return subscriptionStatsService.getMonthlyRevenue();
    }

    /**
     * Get the count of subscriptions expiring soon.
     *
     * @return number of subscriptions expiring soon
     */
    @GetMapping("/expiring-soon")
    public Integer getExpiringSoon() {
        return subscriptionStatsService.getExpiringSoonCount();
    }

    /**
     * Get the number of new subscriptions created today.
     *
     * @return count of new subscriptions today
     */
    @GetMapping("/new-today")
    public Integer getNewToday() {
        return subscriptionStatsService.getNewTodayCount();
    }

    // ----------------- Admin Subscriptions Endpoints -----------------

    /**
     * Get a list of subscriptions filtered by status.
     *
     * @param filter subscription filter (active, cancelled, expired, soon-expired, all)
     * @return list of {@link SubscriptionListItemDTO}
     */
    @GetMapping("/subscriptions")
    public List<SubscriptionListItemDTO> list(@RequestParam(defaultValue = "all") String filter) {
        switch (filter.toLowerCase()) {
            case "active":
                return dashboardService.getActiveSubscriptions();
            case "cancelled":
                return dashboardService.getCancelledSubscriptions();
            case "expired":
                return dashboardService.getExpiredSubscriptions();
            case "soon-expired":
                return dashboardService.getSoonExpiredSubscriptions();
            case "all":
                return dashboardService.getAllSubscriptions();
            default:
                log.warn("Invalid subscription filter requested: {}", filter);
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid filter: " + filter);
        }
    }

    /**
     * Get details of a subscription by ID.
     *
     * @param id subscription ID
     * @return subscription details
     */
    @GetMapping("/subscriptions/{id}")
    public SubscriptionListItemDTO view(@PathVariable Long id) {
        return dashboardService.getById(id);
    }

    /**
     * Update a subscription with given ID.
     *
     * @param id  subscription ID
     * @param req update request payload
     * @return updated subscription details
     */
    @PatchMapping("/subscriptions/{id}")
    public SubscriptionListItemDTO edit(@PathVariable Long id,
                                        @Valid @RequestBody UpdateSubscriptionRequest req) {
        log.info("Updating subscription with ID: {}", id);
        return dashboardService.update(id, req);
    }

    /**
     * Cancel a subscription by ID.
     *
     * @param id subscription ID
     * @return cancelled subscription details
     */
    @PatchMapping("/subscriptions/cancel/{id}")
    public SubscriptionListItemDTO cancel(@PathVariable Long id) {
        log.info("Cancelling subscription with ID: {}", id);
        return dashboardService.cancelSubscription(id);
    }

    // ----------------- Export Subscriptions Endpoints -----------------

    /**
     * Export subscription data as CSV file.
     *
     * @param filter   subscription filter (active, cancelled, expired, soon-expired, all)
     * @param response HTTP servlet response to write CSV output
     */
    @GetMapping("/subscriptions/export-csv")
    public void exportToCsv(@RequestParam(defaultValue = "all") String filter,
                            HttpServletResponse response) {
        log.info("Exporting subscriptions with filter: {}", filter);

        response.setContentType("text/csv");
        response.setHeader("Content-Disposition", "attachment; filename=\"subscriptions.csv\"");

        exportService.exportSubscriptions(filter, response);
    }

}
