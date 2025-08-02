package com.subscription.service.controller;

import com.subscription.service.dto.PlanRequest;
import com.subscription.service.dto.PlanResponse;
import com.subscription.service.security.JwtUtil;
import com.subscription.service.service.PlanService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/plans")
@RequiredArgsConstructor
public class PlanController {

    private final PlanService planService;
    private final JwtUtil jwtUtil;


    @PostMapping
    public PlanResponse createPlan(@RequestBody PlanRequest request,
                                   HttpServletRequest httpRequest) {
        String token = httpRequest.getHeader("Authorization").substring(7);
        String creatorId = jwtUtil.extractUsername(token);
        return planService.createPlan(request, creatorId);
    }

    // ✅ Get All Plans — (User side)
    @GetMapping
    public List<PlanResponse> getAllPlans() {
        return planService.getAllPlans();
    }

    // ✅ Get Plans by Creator — for creator dashboard
    @GetMapping("/creator/{creatorId}")
    public List<PlanResponse> getByCreator(@PathVariable String creatorId) {
        return planService.getPlansByCreator(creatorId);
    }
}
