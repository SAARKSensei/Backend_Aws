package com.sensei.backend.adapter.persistence.jpa.repository;

import com.sensei.backend.adapter.persistence.jpa.entity.PricingPlan;
import com.sensei.backend.adapter.persistence.jpa.entity.PricingPlanSubject;
import com.sensei.backend.adapter.persistence.jpa.entity.SubjectEntity;
import com.sensei.backend.application.dto.pricingplansubject.*;
import com.sensei.backend.domain.port.repository.*;
import com.sensei.backend.domain.port.repository.Subject.SubjectRepositoryPort;
import com.sensei.backend.domain.service.PricingPlanSubjectService;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PricingPlanSubjectServiceImpl implements PricingPlanSubjectService {

    private final PricingPlanSubjectRepository repository;
    private final PricingPlanRepository pricingPlanRepository;
    private final SubjectRepositoryPort subjectRepository;

    private PricingPlanSubjectResponseDTO map(PricingPlanSubject p) {
        PricingPlanSubjectResponseDTO dto = new PricingPlanSubjectResponseDTO();
        dto.setId(p.getId());
        dto.setPricingPlanId(p.getPricingPlan().getId());
        dto.setSubjectId(p.getSubject().getId());
        return dto;
    }

    // @Override
    // public PricingPlanSubjectResponseDTO add(PricingPlanSubjectRequestDTO dto) {
    //     PricingPlan plan = pricingPlanRepository.findById(dto.getPricingPlanId())
    //             .orElseThrow(() -> new RuntimeException("Plan not found"));

    //     Subject subject = subjectRepository.findById(dto.getSubjectId())
    //             .orElseThrow(() -> new RuntimeException("Subject not found"));

    //     PricingPlanSubject p = PricingPlanSubject.builder()
    //             .pricingPlan(plan)
    //             .subject(subject)
    //             .build();

    //     return map(repository.save(p));
    // }

    @SuppressWarnings("null")
    @Override
    public void remove(UUID id) {
        repository.deleteById(id);
    }

    @Override
    public List<PricingPlanSubjectResponseDTO> getByPlan(UUID planId) {
        return repository.findByPricingPlanId(planId)
                .stream().map(this::map).collect(Collectors.toList());
    }

    @Override
    public List<PricingPlanSubjectResponseDTO> getBySubject(UUID subjectId) {
        return repository.findBySubjectId(subjectId)
                .stream().map(this::map).collect(Collectors.toList());
    }

    @Override
    public PricingPlanSubjectResponseDTO add(PricingPlanSubjectRequestDTO dto) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'add'");
    }
}
