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

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class PricingPlanService {

    @Autowired
    private PricingPlanRepository pricingPlanRepository;

    @Autowired
    private SubjectRepository subjectRepository;  // Needed for managing subjects

    @Autowired
    private ModelMapper modelMapper;

    public PricingPlanDTO createPricingPlan(PricingPlanDTO pricingPlanDTO) {
        PricingPlan pricingPlan = modelMapper.map(pricingPlanDTO, PricingPlan.class);

        // Convert SubjectDTOs to Subject entities
        List<Subject> subjects = pricingPlanDTO.getSubjects().stream()
                .map(subjectDTO -> modelMapper.map(subjectDTO, Subject.class))
                .collect(Collectors.toList());

        pricingPlan.setSubjects(subjects);
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
        PricingPlan pricingPlan = modelMapper.map(pricingPlanDTO, PricingPlan.class);

        // Convert SubjectDTOs to Subject entities
        List<Subject> subjects = pricingPlanDTO.getSubjects().stream()
                .map(subjectDTO -> modelMapper.map(subjectDTO, Subject.class))
                .collect(Collectors.toList());

        pricingPlan.setSubjects(subjects);
        pricingPlan.setId(id);  // Make sure the ID is set for the update operation
        PricingPlan updatedPricingPlan = pricingPlanRepository.save(pricingPlan);
        return modelMapper.map(updatedPricingPlan, PricingPlanDTO.class);
    }

    public void deletePricingPlan(String id) {
        pricingPlanRepository.deleteById(id);
    }
}
