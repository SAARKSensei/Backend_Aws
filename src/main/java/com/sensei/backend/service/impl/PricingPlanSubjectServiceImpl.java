package com.sensei.backend.service.impl;
import com.sensei.backend.dto.pricingplansubject.PricingPlanSubjectResponseDTO;
import com.sensei.backend.dto.pricingplansubject.PricingPlanSubjectUpdateDTO;
import com.sensei.backend.dto.pricingplansubjectbulkrequest.PricingPlanSubjectBulkRequestDTO;
import com.sensei.backend.dto.pricingplansubjectbulkrequest.PricingPlanSubjectBulkResponseDTO;
import com.sensei.backend.entity.*;
import com.sensei.backend.repository.*;
import com.sensei.backend.service.PricingPlanSubjectService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@RequiredArgsConstructor
public class PricingPlanSubjectServiceImpl implements PricingPlanSubjectService {

    private final PricingPlanSubjectRepository repo;
    private final PricingPlanRepository planRepo;
    private final SubjectRepository subjectRepo;

    // ================= BULK ATTACH =================
    @Override
    public PricingPlanSubjectBulkResponseDTO attachMultipleSubjects(
            PricingPlanSubjectBulkRequestDTO dto) {

        PricingPlan plan = planRepo.findById(dto.getPricingPlanId())
                .orElseThrow(() -> new RuntimeException("Pricing Plan not found"));

        List<PricingPlanSubjectBulkResponseDTO.SubjectSummaryDTO> attached = new ArrayList<>();

        for (UUID subjectId : dto.getSubjectIds()) {

            if (repo.existsByPricingPlan_IdAndSubject_Id(plan.getId(), subjectId)) {
                continue;
            }

            Subject subject = subjectRepo.findById(subjectId)
                    .orElseThrow(() -> new RuntimeException("Subject not found"));

            PricingPlanSubject mapping = PricingPlanSubject.builder()
                    .pricingPlan(plan)
                    .subject(subject)
                    .build();

            repo.save(mapping);

            attached.add(
                    PricingPlanSubjectBulkResponseDTO.SubjectSummaryDTO.builder()
                            .subjectId(subject.getId())
                            .subjectName(subject.getName())
                            .build()
            );
        }

        return PricingPlanSubjectBulkResponseDTO.builder()
                .pricingPlanId(plan.getId())
                .pricingPlanName(plan.getName())
                .totalSubjectsAttached(attached.size())
                .subjects(attached)
                .build();
    }

    // ================= READ =================
    @Override
    public List<PricingPlanSubjectResponseDTO> getAll() {
        return repo.findAll().stream().map(this::map).toList();
    }

    @Override
    public List<PricingPlanSubjectResponseDTO> getByPricingPlan(UUID pricingPlanId) {
        return repo.findByPricingPlan_Id(pricingPlanId).stream().map(this::map).toList();
    }

    @Override
    public List<PricingPlanSubjectResponseDTO> getBySubject(UUID subjectId) {
        return repo.findBySubject_Id(subjectId).stream().map(this::map).toList();
    }

    // ================= UPDATE =================
    @Override
    public PricingPlanSubjectResponseDTO update(UUID mappingId,
                                                PricingPlanSubjectUpdateDTO dto) {

        PricingPlanSubject mapping = repo.findById(mappingId)
                .orElseThrow(() -> new RuntimeException("Mapping not found"));

        PricingPlan plan = planRepo.findById(dto.getPricingPlanId())
                .orElseThrow(() -> new RuntimeException("Pricing Plan not found"));

        Subject subject = subjectRepo.findById(dto.getSubjectId())
                .orElseThrow(() -> new RuntimeException("Subject not found"));

        mapping.setPricingPlan(plan);
        mapping.setSubject(subject);

        return map(repo.save(mapping));
    }

    // ================= DELETE =================
    @Override
    public void delete(UUID mappingId) {
        repo.deleteById(mappingId);
    }

    // ================= MAPPER =================
    private PricingPlanSubjectResponseDTO map(PricingPlanSubject p) {
        return PricingPlanSubjectResponseDTO.builder()
                .mappingId(p.getId())
                .pricingPlanId(p.getPricingPlan().getId())
                .pricingPlanName(p.getPricingPlan().getName())
                .subjectId(p.getSubject().getId())
                .subjectName(p.getSubject().getName())
                .build();
    }
}
