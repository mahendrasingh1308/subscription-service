package com.subscription.service.service.impl;

import com.subscription.service.dto.SubscriptionListItemDTO;
import com.subscription.service.service.DashboardService;
import com.subscription.service.service.ExportService;
import lombok.RequiredArgsConstructor;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.springframework.stereotype.Service;

import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

/**
 * {@link ExportService} implementation that provides functionality
 * to export subscription data into a CSV format file.
 * <p>
 * This class uses {@link DashboardService} to fetch subscription data
 * based on the given filter (active, cancelled, expired, etc.) and
 * writes the data into the HTTP response as a downloadable CSV file.
 * </p>
 *
 * <p>CSV columns include:</p>
 * <ul>
 *   <li>Fan Name</li>
 *   <li>Creator ID</li>
 *   <li>Creator Name</li>
 *   <li>Plan Name</li>
 *   <li>Price</li>
 *   <li>Duration (Days)</li>
 *   <li>Start Date</li>
 *   <li>End Date</li>
 *   <li>Duration Text</li>
 *   <li>Status</li>
 *   <li>Auto Renewal</li>
 *   <li>Remaining Days</li>
 * </ul>
 *
 * @author YourName
 * @since 1.0
 */
@Service
@RequiredArgsConstructor
public class ExportServiceImpl implements ExportService {

    private final DashboardService dashboardService;

    /**
     * Exports subscription data into a CSV file based on the provided filter.
     *
     * <p>Valid filters are:</p>
     * <ul>
     *   <li><b>active</b> - All active subscriptions</li>
     *   <li><b>cancelled</b> - All cancelled subscriptions</li>
     *   <li><b>expired</b> - All expired subscriptions</li>
     *   <li><b>soon-expired</b> - Subscriptions that are about to expire</li>
     *   <li><b>all</b> - All subscriptions</li>
     * </ul>
     *
     * <p>If an invalid filter is passed, an {@link IllegalArgumentException} is thrown.</p>
     *
     * @param filter   the filter for selecting subscriptions (e.g., "active", "all").
     * @param response the {@link HttpServletResponse} to which the CSV will be written.
     * @throws RuntimeException if an I/O error occurs while writing CSV data.
     */
    @Override
    public void exportSubscriptions(String filter, HttpServletResponse response) {
        List<SubscriptionListItemDTO> subscriptions = switch (filter.toLowerCase()) {
            case "active"       -> dashboardService.getActiveSubscriptions();
            case "cancelled"    -> dashboardService.getCancelledSubscriptions();
            case "expired"      -> dashboardService.getExpiredSubscriptions();
            case "soon-expired" -> dashboardService.getSoonExpiredSubscriptions();
            case "all"          -> dashboardService.getAllSubscriptions();
            default             -> throw new IllegalArgumentException("Invalid filter: " + filter);
        };

        try (
                PrintWriter writer = response.getWriter();
                CSVPrinter csvPrinter = new CSVPrinter(writer,
                        CSVFormat.DEFAULT.withHeader(
                                "Fan Name",
                                "Creator ID",
                                "Creator Name",
                                "Plan Name",
                                "Price",
                                "Duration (Days)",
                                "Start Date",
                                "End Date",
                                "Duration Text",
                                "Status",
                                "Auto Renewal",
                                "Remaining Days"
                        ))
        ) {
            for (SubscriptionListItemDTO sub : subscriptions) {
                csvPrinter.printRecord(
                        sub.getFanName(),
                        sub.getCreatorId(),
                        sub.getCreatorName(),
                        sub.getPlanName(),
                        sub.getPrice(),
                        sub.getDurationInDays(),
                        sub.getStartDate(),
                        sub.getEndDate(),
                        sub.getDurationText(),
                        sub.getStatus(),
                        sub.getAutoRenewal(),
                        sub.getRemainingDays()
                );
            }
            csvPrinter.flush();
        } catch (IOException e) {
            throw new RuntimeException("Failed to export subscriptions to CSV", e);
        }
    }
}
