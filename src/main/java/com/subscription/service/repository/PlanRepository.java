package com.subscription.service.repository;

import com.subscription.service.entity.Plan;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PlanRepository extends JpaRepository<Plan, Long> {
    List<Plan> findByCreatorId(String creatorId);

}
