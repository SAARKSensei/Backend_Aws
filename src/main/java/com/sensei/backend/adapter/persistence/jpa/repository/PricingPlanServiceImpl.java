package com.sensei.backend.adapter.persistence.jpa.repository;

import com.sensei.backend.adapter.persistence.jpa.entity.PricingPlan;
import com.sensei.backend.application.dto.pricingplan.*;
import com.sensei.backend.domain.port.repository.PricingPlanRepository;
import com.sensei.backend.domain.service.PricingPlanService;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PricingPlanServiceImpl implements PricingPlanService {

    private final PricingPlanRepository repository;

    private PricingPlanResponseDTO map(PricingPlan p) {
        PricingPlanResponseDTO dto = new PricingPlanResponseDTO();
        dto.setId(p.getId());
        dto.setName(p.getName());
        dto.setPrice(p.getPrice());
        dto.setDurationInMonths(p.getDurationInMonths());
        dto.setGrade(p.getGrade());
        dto.setStatus(p.getStatus());
        dto.setDescription(p.getDescription());
        return dto;
    }

    @Override
    public PricingPlanResponseDTO create(PricingPlanRequestDTO dto) {
        PricingPlan p = PricingPlan.builder()
                .name(dto.getName())
                .price(dto.getPrice())
                .durationInMonths(dto.getDurationInMonths())
                .grade(dto.getGrade())
                .status(dto.getStatus())
                .description(dto.getDescription())
                .build();
        return map(repository.save(p));
    }

    @Override
    public PricingPlanResponseDTO update(UUID id, PricingPlanRequestDTO dto) {
        PricingPlan p = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Plan not found"));
        p.setName(dto.getName());
        p.setPrice(dto.getPrice());
        p.setDurationInMonths(dto.getDurationInMonths());
        p.setGrade(dto.getGrade());
        p.setStatus(dto.getStatus());
        p.setDescription(dto.getDescription());
        return map(repository.save(p));
    }

    @Override
    public PricingPlanResponseDTO getById(UUID id) {
        return map(repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Plan not found")));
    }

    @Override
    public List<PricingPlanResponseDTO> getAll() {
        return repository.findAll().stream().map(this::map).collect(Collectors.toList());
    }

    @Override
    public List<PricingPlanResponseDTO> getByStatus(String status) {
        return repository.findByStatus(status).stream().map(this::map).collect(Collectors.toList());
    }

    @Override
    public void delete(UUID id) {
        repository.deleteById(id);
    }
}
