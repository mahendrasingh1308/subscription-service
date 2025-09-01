package com.subscription.service.service;

import com.subscription.service.dto.PlanRequest;
import com.subscription.service.dto.PlanResponse;

import java.util.List;

public interface PlanService {

    PlanResponse createPlan(PlanRequest request, String creatorId,String firstName,String lastName);

    List<PlanResponse> getAllPlans();

    List<PlanResponse> getPlansByCreator(String creatorId);
}
