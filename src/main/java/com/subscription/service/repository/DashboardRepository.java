package com.subscription.service.repository;
import com.subscription.service.entity.Subscription;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import java.time.LocalDate;
import java.util.List;
public interface DashboardRepository extends JpaRepository<Subscription, Long> {
    @Query("""
           SELECT s FROM Subscription s
           WHERE s.status = com.subscription.service.entity.SubscriptionStatus.ACTIVE
             AND s.endDate >= CURRENT_DATE
           """)
    List<Subscription> findAllActive();

    @Query("""
           SELECT s FROM Subscription s
           WHERE s.status = com.subscription.service.entity.SubscriptionStatus.ACTIVE
             AND s.endDate BETWEEN :today AND :after7
           """)
    List<Subscription> findAllSoonExpired(LocalDate today, LocalDate after7);

    // Cancelled only
    @Query("""
           SELECT s FROM Subscription s
           WHERE s.status = com.subscription.service.entity.SubscriptionStatus.CANCELLED
           """)
    List<Subscription> findAllCancelled();

    @Query("""
           SELECT s FROM Subscription s
           WHERE s.status = com.subscription.service.entity.SubscriptionStatus.EXPIRED
           """)
    List<Subscription> findAllExpired();
}
