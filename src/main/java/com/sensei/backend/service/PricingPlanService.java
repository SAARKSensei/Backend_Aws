package com.sensei.backend.service;

import com.sensei.backend.dto.PricingPlanDTO;
import com.sensei.backend.dto.SubjectDTO;
import com.sensei.backend.entity.PricingPlan;
import com.sensei.backend.entity.Subject;
import com.sensei.backend.repository.PricingPlanRepository;
import com.sensei.backend.repository.SubjectRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional; // ✅ IMPORTED

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional // ✅ FIX 1: Keeps the database connection open (Prevents LazyInitializationException)
public class PricingPlanService {

    @Autowired
    private PricingPlanRepository pricingPlanRepository;

    @Autowired
    private SubjectRepository subjectRepository;

    @Autowired
    private ModelMapper modelMapper;

    public PricingPlanDTO createPricingPlan(PricingPlanDTO pricingPlanDTO) {
        PricingPlan pricingPlan = modelMapper.map(pricingPlanDTO, PricingPlan.class);

        // ✅ FIX 2: Fetch REAL existing subjects from the DB instead of creating fake ones
        if (pricingPlanDTO.getSubjects() != null && !pricingPlanDTO.getSubjects().isEmpty()) {
            List<String> subjectIds = pricingPlanDTO.getSubjects().stream()
                    .map(SubjectDTO::getSubjectId)
                    .collect(Collectors.toList());

            List<Subject> subjects = subjectRepository.findAllById(subjectIds);
            pricingPlan.setSubjects(subjects);
        } else {
            pricingPlan.setSubjects(new ArrayList<>());
        }

        PricingPlan savedPricingPlan = pricingPlanRepository.save(pricingPlan);
        return modelMapper.map(savedPricingPlan, PricingPlanDTO.class);
    }

    public List<PricingPlanDTO> getAllPricingPlans() {
        return pricingPlanRepository.findAll().stream()
                .map(pricingPlan -> modelMapper.map(pricingPlan, PricingPlanDTO.class))
                .collect(Collectors.toList());
    }

    public Optional<PricingPlanDTO> getPricingPlanById(String id) {
        return pricingPlanRepository.findById(id)
                .map(pricingPlan -> modelMapper.map(pricingPlan, PricingPlanDTO.class));
    }

    public PricingPlanDTO updatePricingPlan(String id, PricingPlanDTO pricingPlanDTO) {
        PricingPlan existingPlan = pricingPlanRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Pricing Plan not found"));

        // Manual update to ensure safety
        existingPlan.setName(pricingPlanDTO.getName());
        existingPlan.setPrice(pricingPlanDTO.getPrice());
        existingPlan.setDurationInMonths(pricingPlanDTO.getDurationInMonths());
        existingPlan.setGrade(pricingPlanDTO.getGrade());
        existingPlan.setStatus(pricingPlanDTO.getStatus());
        existingPlan.setDescription(pricingPlanDTO.getDescription());

        // ✅ FIX 3: Correctly update the list of subjects
        if (pricingPlanDTO.getSubjects() != null) {
            List<String> subjectIds = pricingPlanDTO.getSubjects().stream()
                    .map(SubjectDTO::getSubjectId)
                    .collect(Collectors.toList());

            List<Subject> subjects = subjectRepository.findAllById(subjectIds);
            existingPlan.setSubjects(subjects);
        }

        PricingPlan updatedPricingPlan = pricingPlanRepository.save(existingPlan);
        return modelMapper.map(updatedPricingPlan, PricingPlanDTO.class);
    }

    public void deletePricingPlan(String id) {
        pricingPlanRepository.deleteById(id);
    }
}
