package com.subscription.service.service;

import jakarta.servlet.http.HttpServletResponse;

/**
 * Service for exporting subscription data into downloadable formats.
 * Currently supports CSV export.
 */
public interface ExportService {

    /**
     * Exports subscription data as a CSV file based on the given filter.
     *
     * @param filter   the filter to apply (e.g., "all", "active", "cancelled", "expired", "soon-expired")
     * @param response HTTP response to write the CSV content into
     */
    void exportSubscriptions(String filter, HttpServletResponse response);
}
