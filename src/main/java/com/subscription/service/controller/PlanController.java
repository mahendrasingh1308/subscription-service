package com.subscription.service.controller;

import com.subscription.service.dto.PlanRequest;
import com.subscription.service.dto.PlanResponse;
import com.subscription.service.security.JwtUtil;
import com.subscription.service.service.PlanService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * REST controller for managing subscription plans.
 * <p>
 * This controller provides endpoints for:
 * <ul>
 *   <li>Creating subscription plans (for creators)</li>
 *   <li>Retrieving all available plans (for users)</li>
 *   <li>Retrieving plans by a specific creator (for creator dashboards)</li>
 * </ul>
 *
 * <p>Security considerations:
 * <ul>
 *   <li>Plan creation requires a valid JWT token to identify the creator.</li>
 *   <li>Fetching plans can be public, but sensitive endpoints should be secured as per {@link com.subscription.service.config.SecurityConfig}.</li>
 * </ul>
 *
 * @author Chandan
 * @see com.subscription.service.service.PlanService
 * @see com.subscription.service.dto.PlanRequest
 * @see com.subscription.service.dto.PlanResponse
 */
@RestController
@RequestMapping("/api/plans")
@RequiredArgsConstructor
public class PlanController {

    private final PlanService planService;
    private final JwtUtil jwtUtil;

    /**
     * Creates a new subscription plan for a creator.
     * <p>
     * The creator ID is extracted from the JWT token in the {@code Authorization} header.
     * Only authenticated creators should be able to access this endpoint.
     *
     * @param request      the plan details (title, price, duration, etc.)
     * @param httpRequest  the HTTP request containing the JWT token
     * @return the created plan details
     */
    @PostMapping
    public PlanResponse createPlan(@RequestBody PlanRequest request,
                                   HttpServletRequest httpRequest) {
        String token = httpRequest.getHeader("Authorization").substring(7);
        String creatorId = jwtUtil.extractUsername(token);
        String firstName = jwtUtil.extractFirstName(token);
        String lastName = jwtUtil.extractLastName(token);
        return planService.createPlan(request, creatorId,firstName,lastName);
    }

    /**
     * Retrieves all available subscription plans.
     * <p>
     * This is typically used on the user side to browse available creator plans.
     *
     * @return a list of all available plans
     */
    @GetMapping
    public List<PlanResponse> getAllPlans() {
        return planService.getAllPlans();
    }

    /**
     * Retrieves all subscription plans created by a specific creator.
     * <p>
     * This is typically used in the creator's dashboard to view and manage their plans.
     *
     * @param creatorId the unique identifier of the creator
     * @return a list of plans created by the specified creator
     */
    @GetMapping("/creator/{creatorId}")
    public List<PlanResponse> getByCreator(@PathVariable String creatorId) {
        return planService.getPlansByCreator(creatorId);
    }
}
