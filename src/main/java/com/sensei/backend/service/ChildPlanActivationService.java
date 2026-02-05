// package com.sensei.backend.service;

// import com.sensei.backend.entity.ChildUser;
// import com.sensei.backend.entity.PricingPlan;
// import com.sensei.backend.enums.PlanStatus;
// import com.sensei.backend.repository.ChildUserRepository;
// import com.sensei.backend.repository.PricingPlanRepository;
// import lombok.RequiredArgsConstructor;
// import org.springframework.stereotype.Service;
// import org.springframework.transaction.annotation.Transactional;

// import java.time.LocalDate;
// import java.util.UUID;

// @Service
// @RequiredArgsConstructor
// public class ChildPlanActivationService {

//     private final ChildUserRepository childUserRepository;
//     private final PricingPlanRepository pricingPlanRepository;

//     @Transactional
//     public void activatePlan(UUID childId, UUID pricingPlanId) {

//         ChildUser child = childUserRepository.findById(childId)
//                 .orElseThrow(() -> new RuntimeException("Child not found"));

//         PricingPlan plan = pricingPlanRepository.findById(pricingPlanId)
//                 .orElseThrow(() -> new RuntimeException("Pricing plan not found"));

//         LocalDate startDate = LocalDate.now();
//         LocalDate endDate = startDate.plusMonths(plan.getDurationMonths());

//         //child.setPricingPlan(plan);
//         child.setActivePlanId(plan.getId());
//         child.setPlanStartDate(LocalDate.now());
//         //child.setPlanEndDate(endDate);
//         child.setPlanStatus(PlanStatus.ACTIVE);
//         child.setPlanStartDate(startDate);
//        // child.setPlanEndDate(endDate);
//         child.setPlanStatus(PlanStatus.ACTIVE);

//         childUserRepository.save(child);
//     }
// }
// package com.sensei.backend.service;

// import com.sensei.backend.entity.ChildUser;
// import com.sensei.backend.entity.PricingPlan;
// import com.sensei.backend.enums.PlanStatus;
// import com.sensei.backend.repository.ChildUserRepository;
// import com.sensei.backend.repository.PricingPlanRepository;
// import lombok.RequiredArgsConstructor;
// import org.springframework.stereotype.Service;
// import org.springframework.transaction.annotation.Transactional;

// import java.time.LocalDate;
// import java.util.UUID;

// @Service
// @RequiredArgsConstructor
// public class ChildPlanActivationService {

//     private final ChildUserRepository childUserRepository;
//     private final PricingPlanRepository pricingPlanRepository;

//     @Transactional
//     public void activatePlan(UUID childId, UUID pricingPlanId) {

//         ChildUser child = childUserRepository.findById(childId)
//                 .orElseThrow(() -> new RuntimeException("Child not found"));

//         PricingPlan plan = pricingPlanRepository.findById(pricingPlanId)
//                 .orElseThrow(() -> new RuntimeException("Pricing plan not found"));

//         LocalDate startDate = LocalDate.now();
//         LocalDate expiryDate = startDate.plusMonths(plan.getDurationMonths());

//         child.setActivePlanId(plan.getId());
//         child.setPlanStartDate(startDate);
//         child.setPlanExpiryDate(expiryDate);
//         child.setPlanStatus(PlanStatus.ACTIVE);

//         childUserRepository.save(child);
//     }
// }

package com.sensei.backend.service;

import com.sensei.backend.entity.ChildUser;
import com.sensei.backend.entity.PricingPlan;
import com.sensei.backend.enums.PlanStatus;
import com.sensei.backend.repository.ChildUserRepository;
import com.sensei.backend.repository.PricingPlanRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ChildPlanActivationService {

    private final ChildUserRepository childUserRepository;
    private final PricingPlanRepository pricingPlanRepository;

    /**
     * ⚠️ Call this ONLY after payment SUCCESS
     */
    @Transactional
    public void activatePlan(UUID childId, UUID pricingPlanId) {

        ChildUser child = childUserRepository.findById(childId)
                .orElseThrow(() -> new RuntimeException("Child not found"));

        PricingPlan plan = pricingPlanRepository.findById(pricingPlanId)
                .orElseThrow(() -> new RuntimeException("Pricing plan not found"));

        if (!"ACTIVE".equalsIgnoreCase(plan.getStatus())) {
            throw new RuntimeException("Pricing plan is not active");
        }

        // If already active, overwrite (renewal / upgrade)
        LocalDate startDate = LocalDate.now();
        LocalDate expiryDate = startDate.plusMonths(plan.getDurationMonths());

        child.setActivePlanId(plan.getId());
        child.setPlanStartDate(startDate);
        child.setPlanExpiryDate(expiryDate);
        child.setPlanStatus(PlanStatus.ACTIVE);

        childUserRepository.save(child);
    }
}


