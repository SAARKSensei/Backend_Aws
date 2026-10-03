// package com.sensei.backend.service.impl;

// import com.sensei.backend.dto.subject.SubjectRequestDTO;
// import com.sensei.backend.dto.subject.SubjectResponseDTO;


// import com.sensei.backend.entity.Subject;
// import com.sensei.backend.repository.SubjectRepository;
// import com.sensei.backend.service.SubjectService;
// import lombok.RequiredArgsConstructor;
// import org.springframework.stereotype.Service;

// import java.util.List;
// import java.util.UUID;
// import java.util.stream.Collectors;

// @Service
// @RequiredArgsConstructor
// public class SubjectServiceImpl implements SubjectService {

//     private final SubjectRepository subjectRepository;

//     @Override
//     public SubjectResponseDTO createSubject(SubjectRequestDTO dto) {

//         Subject subject = Subject.builder()
//                 .name(dto.getName())
//                 .description(dto.getDescription())
//                 .iconUrl(dto.getIconUrl())
//                 .isActive(true)
//                 .build();

//         Subject saved = subjectRepository.save(subject);
//         return mapToResponse(saved);
//     }

//     @Override
//     public List<SubjectResponseDTO> getAllSubjects() {
//         return subjectRepository.findAll()
//                 .stream()
//                 .map(this::mapToResponse)
//                 .collect(Collectors.toList());
//     }

//     @Override
//     public SubjectResponseDTO getSubjectById(UUID id) {
//         Subject subject = subjectRepository.findById(id)
//                 .orElseThrow(() -> new RuntimeException("Subject not found"));
//         return mapToResponse(subject);
//     }

//     @Override
//     public void deleteSubject(UUID id) {
//         subjectRepository.deleteById(id);
//     }

//     private SubjectResponseDTO mapToResponse(Subject subject) {
//         return SubjectResponseDTO.builder()
//                 .id(subject.getId())
//                 .name(subject.getName())
//                 .description(subject.getDescription())
//                 .iconUrl(subject.getIconUrl())
//                 .isActive(subject.getIsActive())
//                 .createdAt(subject.getCreatedAt())
//                 .build();
//     }
// }
package com.sensei.backend.service.impl;

import com.sensei.backend.dto.subject.SubjectRequestDTO;
import com.sensei.backend.dto.subject.SubjectResponseDTO;
import com.sensei.backend.entity.Subject;
import com.sensei.backend.entity.ChildUser;
import com.sensei.backend.enums.PlanStatus;
import com.sensei.backend.repository.ChildUserRepository;
import com.sensei.backend.repository.PricingPlanSubjectRepository;
import com.sensei.backend.repository.SubjectRepository;
import com.sensei.backend.repository.ChildSubjectProgressRepository;
import com.sensei.backend.dto.progress.HierarchicalProgressDTO;
import com.sensei.backend.service.SubjectService;
import com.sensei.backend.exception.SubscriptionException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.beans.factory.annotation.Value;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class SubjectServiceImpl implements SubjectService {

    private final SubjectRepository subjectRepository;
    private final ChildUserRepository childUserRepository;
    private final PricingPlanSubjectRepository pricingPlanSubjectRepository;
    private final ChildSubjectProgressRepository childSubjectProgressRepository;

    @Value("${app.freemium.subject-id:}")
    private String freemiumSubjectId;

    @Override
    @Transactional
    public SubjectResponseDTO create(SubjectRequestDTO dto) {
        Subject subject = Subject.builder()
                .name(dto.getName())
                .description(dto.getDescription())
                .iconUrl(dto.getIconUrl())
                .isActive(true)
                .build();

        return mapToResponse(subjectRepository.save(subject));
    }

    @Override
    public List<SubjectResponseDTO> getAll() {
        return subjectRepository.findByIsActiveTrue()
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Override
    public List<SubjectResponseDTO> getAllForChild(UUID childId) {
        ChildUser child = childUserRepository.findById(childId)
                .orElseThrow(() -> new RuntimeException("Child not found"));

        boolean hasActivePlan = child.getPlanStatus() == PlanStatus.ACTIVE &&
                (child.getPlanExpiryDate() == null || !child.getPlanExpiryDate().isBefore(LocalDate.now()));

        List<SubjectResponseDTO> responseList = new java.util.ArrayList<>();

        if (hasActivePlan) {
            List<SubjectResponseDTO> planSubjects = pricingPlanSubjectRepository.findByPricingPlan_Id(child.getActivePlanId())
                    .stream()
                    .filter(pps -> pps.getSubject().getIsActive())
                    .map(pps -> {
                        SubjectResponseDTO dto = mapToResponse(pps.getSubject());
                        dto.setIsLocked(false);

                        childSubjectProgressRepository.findByChildIdAndSubjectId(childId, pps.getSubject().getId()).ifPresent(progress -> {
                            String status = progress.getIsCompleted() ? "COMPLETED" : (progress.getCompletedModules() > 0 ? "STARTED" : "NOT_STARTED");
                            dto.setProgress(HierarchicalProgressDTO.builder()
                                    .completedCount(progress.getCompletedModules())
                                    .totalCount(progress.getTotalModules())
                                    .isCompleted(progress.getIsCompleted())
                                    .status(status)
                                    .build());
                        });
                        return dto;
                    })
                    .collect(Collectors.toList());
            
            responseList.addAll(planSubjects);
        }

        // Always ensure the freemium subject is included (for all users, plan or no plan)
        if (freemiumSubjectId != null && !freemiumSubjectId.isEmpty()) {
            boolean alreadyIncluded = responseList.stream().anyMatch(s -> s.getId().toString().equals(freemiumSubjectId));
            if (!alreadyIncluded) {
                subjectRepository.findById(UUID.fromString(freemiumSubjectId)).ifPresent(subject -> {
                    if (subject.getIsActive()) {
                        SubjectResponseDTO dto = mapToResponse(subject);
                        dto.setIsLocked(false); // The free subject itself is accessible

                        childSubjectProgressRepository.findByChildIdAndSubjectId(childId, subject.getId()).ifPresent(progress -> {
                            String status = progress.getIsCompleted() ? "COMPLETED" : (progress.getCompletedModules() > 0 ? "STARTED" : "NOT_STARTED");
                            dto.setProgress(HierarchicalProgressDTO.builder()
                                    .completedCount(progress.getCompletedModules())
                                    .totalCount(progress.getTotalModules())
                                    .isCompleted(progress.getIsCompleted())
                                    .status(status)
                                    .build());
                        });
                        responseList.add(dto);
                    }
                });
            }
        }

        return responseList;
    }

    @Override
    public SubjectResponseDTO getById(UUID id) {
        Subject subject = subjectRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Subject not found"));
        return mapToResponse(subject);
    }

    @Override
    @Transactional
    public SubjectResponseDTO update(UUID id, SubjectRequestDTO dto) {
        Subject subject = subjectRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Subject not found"));

        subject.setName(dto.getName());
        subject.setDescription(dto.getDescription());
        subject.setIconUrl(dto.getIconUrl());

        return mapToResponse(subjectRepository.save(subject));
    }

    @Override
    @Transactional
    public void delete(UUID id) {
        Subject subject = subjectRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Subject not found"));
        subject.setIsActive(false);
        subjectRepository.save(subject);
    }

    private SubjectResponseDTO mapToResponse(Subject s) {
        return SubjectResponseDTO.builder()
                .id(s.getId())
                .name(s.getName())
                .description(s.getDescription())
                .iconUrl(s.getIconUrl())
                .isActive(s.getIsActive())
                .createdAt(s.getCreatedAt())
                .build();
    }
}
