// package com.sensei.backend.service;

// import com.sensei.backend.entity.ChildUser;
// import com.sensei.backend.enums.PlanStatus;
// import com.sensei.backend.repository.ChildUserRepository;
// import com.sensei.backend.repository.PricingPlanSubjectRepository;
// import lombok.RequiredArgsConstructor;
// import org.springframework.security.access.AccessDeniedException;
// import org.springframework.stereotype.Service;

// import java.time.LocalDate;
// import java.util.UUID;

// @Service
// @RequiredArgsConstructor
// public class AccessControlService {

//     private final ChildUserRepository childUserRepository;
//     private final PricingPlanSubjectRepository pricingPlanSubjectRepository;

//     public void validateSubjectAccess(UUID childId, UUID subjectId) {

//         ChildUser child = childUserRepository.findById(childId)
//                 .orElseThrow(() -> new RuntimeException("Child not found"));

//         // Auto-expire plan if needed
//         if (child.getPlanStatus() == PlanStatus.ACTIVE &&
//                 LocalDate.now().isAfter(child.getPlanEndDate())) {

//             child.setPlanStatus(PlanStatus.EXPIRED);
//             childUserRepository.save(child);
//         }

//         if (child.getPlanStatus() != PlanStatus.ACTIVE) {
//             throw new AccessDeniedException("No active plan for child");
//         }

//         boolean allowed =
//                 pricingPlanSubjectRepository.existsByPricingPlan_IdAndSubject_Id(
//                         child.getPricingPlan().getId(),
//                         subjectId
//                 );

//         if (!allowed) {
//             throw new AccessDeniedException("Subject not allowed in current plan");
//         }
//     }
// }
package com.sensei.backend.service;

import com.sensei.backend.entity.ChildUser;
import com.sensei.backend.entity.InteractiveActivity;
import com.sensei.backend.entity.InteractiveProcess;
import com.sensei.backend.enums.PlanStatus;
import com.sensei.backend.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AccessControlService {

    private final ChildUserRepository childUserRepository;
    private final PricingPlanSubjectRepository pricingPlanSubjectRepository;
    private final InteractiveActivityRepository interactiveActivityRepository;
    private final InteractiveProcessRepository interactiveProcessRepository;
    private final InteractiveProcessSubStepRepository interactiveProcessSubStepRepository;

    // ----------------------------------------
    // MAIN ACCESS CHECK
    // ----------------------------------------
    public void validateSubjectAccess(UUID childId, UUID subjectId) {

        ChildUser child = childUserRepository.findById(childId)
                .orElseThrow(() -> new RuntimeException("Child not found"));

        if (child.getPlanStatus() != PlanStatus.ACTIVE) {
            throw new RuntimeException("No active plan");
        }

        if (child.getPlanExpiryDate().isBefore(LocalDate.now())) {
            throw new RuntimeException("Plan expired");
        }

        boolean allowed =
                pricingPlanSubjectRepository.existsByPricingPlan_IdAndSubject_Id(
                        child.getActivePlanId(),
                        subjectId
                );

        if (!allowed) {
            throw new RuntimeException("Access denied to subject");
        }
    }

    // ----------------------------------------
    // SUBJECT RESOLVERS
    // ----------------------------------------
    public UUID getSubjectIdByActivity(UUID activityId) {

        InteractiveActivity activity =
                interactiveActivityRepository.findById(activityId)
                        .orElseThrow(() -> new RuntimeException("InteractiveActivity not found"));

        return activity.getSubModule()
                .getModule()
                .getSubject()
                .getId();
    }

    public UUID getSubjectIdByProcess(UUID processId) {

        InteractiveProcess process =
                interactiveProcessRepository.findById(processId)
                        .orElseThrow(() -> new RuntimeException("InteractiveProcess not found"));

        return process.getInteractiveActivity()
                .getSubModule()
                .getModule()
                .getSubject()
                .getId();
    }

    public UUID getSubjectIdBySubStep(UUID subStepId) {

        return interactiveProcessSubStepRepository.findById(subStepId)
                .orElseThrow(() -> new RuntimeException("Process SubStep not found"))
                .getProcess()
                .getInteractiveActivity()
                .getSubModule()
                .getModule()
                .getSubject()
                .getId();
    }
}

