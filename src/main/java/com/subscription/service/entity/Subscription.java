package com.subscription.service.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDate;

/**
 * Entity representing a user's subscription to a specific plan.
 * <p>
 * This entity maps to the {@code subscription} table and stores
 * information about the plan subscribed to, subscription duration,
 * and its active status.
 * </p>
 */
@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "subscription")
public class Subscription {

    /**
     * Primary key for the subscription record.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false, updatable = false)
    private Long id;

    /**
     * The ID of the user who owns this subscription.
     */
    @Column(name = "user_id", nullable = false, length = 100)
    private String userId;

    /**
     * The subscription plan associated with this subscription.
     * Many subscriptions can reference the same plan.
     */
    @ManyToOne
    @JoinColumn(name = "plan_id")
    private Plan plan;

    /**
     * The date when the subscription starts.
     */
    @Column(name = "start_date")
    private LocalDate startDate;

    /**
     * The date when the subscription ends.
     */
    @Column(name = "end_date")
    private LocalDate endDate;

    /**
     * Indicates whether the subscription is currently active.
     */
    @Column(name = "active")
    private boolean active;

    @Column(name="first_name")
    private String firstName;

    @Column(name="last_name")
    private String lastName;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private SubscriptionStatus status;

    @Column(name = "auto_renewal")
    private Boolean autoRenewal;

}
