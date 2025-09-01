package com.subscription.service.scheduler;import com.subscription.service.entity.Subscription;
import com.subscription.service.entity.SubscriptionStatus;
import com.subscription.service.repository.SubscriptionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;

@Component
@RequiredArgsConstructor
public class SubscriptionScheduler {

    private final SubscriptionRepository subscriptionRepository;
    @Scheduled(cron = "0 0 0 * * ?")
    public void checkSubscriptions() {

        List<Subscription> subscriptions = subscriptionRepository.findAll();

        for (Subscription s : subscriptions) {
            if (s.getStatus() == SubscriptionStatus.ACTIVE && s.getEndDate().isBefore(LocalDate.now())) {

                if (Boolean.TRUE.equals(s.getAutoRenewal())) {
                    s.setStartDate(LocalDate.now());
                    s.setEndDate(LocalDate.now().plusDays(s.getPlan().getDurationInDays()));
                } else {
                    s.setStatus(SubscriptionStatus.EXPIRED);
                    s.setActive(false);
                }

                subscriptionRepository.save(s);
            }
        }
    }
}
