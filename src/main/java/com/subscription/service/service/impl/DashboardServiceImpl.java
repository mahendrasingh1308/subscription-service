package com.subscription.service.service.impl;
import com.subscription.service.dto.SubscriptionListItemDTO;
import com.subscription.service.dto.UpdateSubscriptionRequest;
import com.subscription.service.entity.Plan;
import com.subscription.service.entity.Subscription;
import com.subscription.service.entity.SubscriptionStatus;
import com.subscription.service.repository.DashboardRepository;
import com.subscription.service.service.DashboardService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DashboardServiceImpl implements DashboardService {

    private final DashboardRepository dashboardRepository;

    @Override
    public List<SubscriptionListItemDTO> getAllSubscriptions() {
        return dashboardRepository.findAll().stream()
                .map(this::toListItem)
                .collect(Collectors.toList());
    }

    @Override
    public List<SubscriptionListItemDTO> getActiveSubscriptions() {
        return dashboardRepository.findAllActive().stream()
                .map(this::toListItem)
                .collect(Collectors.toList());
    }

    @Override
    public List<SubscriptionListItemDTO> getCancelledSubscriptions() {
        return dashboardRepository.findAllCancelled().stream()
                .map(this::toListItem)
                .collect(Collectors.toList());
    }

    @Override
    public List<SubscriptionListItemDTO> getExpiredSubscriptions() {
        return dashboardRepository.findAllExpired().stream()
                .map(this::toListItem)
                .collect(Collectors.toList());
    }

    // ✅ Soon-expired ka fix
    public List<SubscriptionListItemDTO> getSoonExpiredSubscriptions() {
        LocalDate today = LocalDate.now();
        LocalDate after7 = today.plusDays(7);
        return dashboardRepository.findAllSoonExpired(today, after7).stream()
                .map(this::toListItem)
                .collect(Collectors.toList());
    }

    @Override
    public SubscriptionListItemDTO getById(Long id) {
        Subscription s = dashboardRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Subscription not found"));
        return toListItem(s);
    }

    @Override
    public SubscriptionListItemDTO update(Long id, UpdateSubscriptionRequest req) {
        Subscription s = dashboardRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Subscription not found"));

        if (req.getAutoRenewal() != null) {
            s.setAutoRenewal(req.getAutoRenewal());
        }
        dashboardRepository.save(s);
        return toListItem(s);
    }

    @Override
    public SubscriptionListItemDTO cancelSubscription(Long subscriptionId) {
        Subscription s = dashboardRepository.findById(subscriptionId)
                .orElseThrow(() -> new RuntimeException("Subscription not found"));

        s.setStatus(SubscriptionStatus.CANCELLED);
        s.setActive(false);
        dashboardRepository.save(s);
        return toListItem(s);
    }

    // -------- Mapper for dashboard table rows --------
    private SubscriptionListItemDTO toListItem(Subscription s) {
        Plan p = s.getPlan();

        // Creator Name
        String creatorName = ((p.getFirstName() != null ? p.getFirstName().trim() : "")
                + (p.getLastName() != null ? (" " + p.getLastName().trim()) : "")).trim();
        if (creatorName.isEmpty()) creatorName = p.getCreatorId();

        // Fan Name
        String fanName = ((s.getFirstName() != null ? s.getFirstName().trim() : "")
                + (s.getLastName() != null ? (" " + s.getLastName().trim()) : "")).trim();

        // Remaining Days
        Long remainingDays = null;
        if (s.getEndDate() != null) {
            remainingDays = s.getEndDate().toEpochDay() - LocalDate.now().toEpochDay();
            if (remainingDays < 0) remainingDays = 0L;
        }

        // Status Label
        String statusLabel;
        if (s.getStatus() == SubscriptionStatus.CANCELLED) {
            statusLabel = "cancelled";
        } else if (s.getEndDate() != null) {
            if (s.getEndDate().isBefore(LocalDate.now())) {
                statusLabel = "expired";
            } else if (remainingDays != null && remainingDays <= 7) {
                statusLabel = "soon_expired";
            } else {
                statusLabel = "active";
            }
        } else {
            statusLabel = "active";
        }

        // Duration text
        DateTimeFormatter fmt = DateTimeFormatter.ISO_DATE;
        String durationText = (s.getStartDate() != null && s.getEndDate() != null)
                ? (fmt.format(s.getStartDate()) + " - " + fmt.format(s.getEndDate()))
                : "-";

        return SubscriptionListItemDTO.builder()
                .fanName(fanName)
                .creatorId(p.getCreatorId())
                .creatorName(creatorName)
                .planName(p.getName())
                .price(p.getPrice())
                .durationInDays(p.getDurationInDays())
                .startDate(s.getStartDate())
                .endDate(s.getEndDate())
                .durationText(durationText)
                .status(statusLabel)
                .autoRenewal(Boolean.TRUE.equals(s.getAutoRenewal()) ? "On" : "Off")
                .remainingDays(remainingDays)
                .build();
    }
}
