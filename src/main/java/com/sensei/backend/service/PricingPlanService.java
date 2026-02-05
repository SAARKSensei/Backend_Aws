// package com.sensei.backend.service;

// import com.sensei.backend.dto.PricingPlanDTO;
// import com.sensei.backend.dto.subjectDTO;
// import com.sensei.backend.entity.PricingPlan;
// import com.sensei.backend.entity.Subject;
// import com.sensei.backend.repository.PricingPlanRepository;
// import com.sensei.backend.repository.SubjectRepository;
// import org.modelmapper.ModelMapper;
// import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.stereotype.Service;
// import org.springframework.transaction.annotation.Transactional;

// import java.util.List;
// import java.util.Optional;
// import java.util.stream.Collectors;

// @Service
// @Transactional
// public class PricingPlanService {

//     @Autowired
//     private PricingPlanRepository pricingPlanRepository;

//     @Autowired
//     private SubjectRepository subjectRepository;

//     @Autowired
//     private ModelMapper modelMapper;

//     // ================= CREATE =================
//     public PricingPlanDTO createPricingPlan(PricingPlanDTO pricingPlanDTO) {

//         PricingPlan pricingPlan = new PricingPlan();

//         pricingPlan.setName(pricingPlanDTO.getName());
//         pricingPlan.setPrice(pricingPlanDTO.getPrice());
//         pricingPlan.setDurationInMonths(pricingPlanDTO.getDurationInMonths());
//         pricingPlan.setGrade(pricingPlanDTO.getGrade());
//         pricingPlan.setStatus(pricingPlanDTO.getStatus());
//         pricingPlan.setDescription(pricingPlanDTO.getDescription());

//         /*
//          * 🔑 CRITICAL LOGIC
//          * We ONLY extract subjectId
//          * All other interconnected fields remain untouched
//          */
//         if (pricingPlanDTO.getSubjects() != null && !pricingPlanDTO.getSubjects().isEmpty()) {

//             List<String> subjectIds = pricingPlanDTO.getSubjects()
//                     .stream()
//                     .map(SubjectDTO::getSubjectId)
//                     .collect(Collectors.toList());

//             List<Subject> subjects = subjectRepository.findAllById(subjectIds);
//             pricingPlan.setSubjects(subjects);
//         }

//         PricingPlan savedPlan = pricingPlanRepository.save(pricingPlan);
//         return modelMapper.map(savedPlan, PricingPlanDTO.class);
//     }

//     // ================= READ =================
//     public List<PricingPlanDTO> getAllPricingPlans() {
//         return pricingPlanRepository.findAll()
//                 .stream()
//                 .map(plan -> modelMapper.map(plan, PricingPlanDTO.class))
//                 .collect(Collectors.toList());
//     }

//     public Optional<PricingPlanDTO> getPricingPlanById(String id) {
//         return pricingPlanRepository.findById(id)
//                 .map(plan -> modelMapper.map(plan, PricingPlanDTO.class));
//     }

//     // ================= UPDATE =================
//     public PricingPlanDTO updatePricingPlan(String id, PricingPlanDTO pricingPlanDTO) {

//         PricingPlan existingPlan = pricingPlanRepository.findById(id)
//                 .orElseThrow(() -> new RuntimeException("Pricing Plan not found"));

//         existingPlan.setName(pricingPlanDTO.getName());
//         existingPlan.setPrice(pricingPlanDTO.getPrice());
//         existingPlan.setDurationInMonths(pricingPlanDTO.getDurationInMonths());
//         existingPlan.setGrade(pricingPlanDTO.getGrade());
//         existingPlan.setStatus(pricingPlanDTO.getStatus());
//         existingPlan.setDescription(pricingPlanDTO.getDescription());

//         if (pricingPlanDTO.getSubjects() != null) {

//             List<String> subjectIds = pricingPlanDTO.getSubjects()
//                     .stream()
//                     .map(SubjectDTO::getSubjectId)
//                     .collect(Collectors.toList());

//             List<Subject> subjects = subjectRepository.findAllById(subjectIds);
//             existingPlan.setSubjects(subjects);
//         }

//         PricingPlan updatedPlan = pricingPlanRepository.save(existingPlan);
//         return modelMapper.map(updatedPlan, PricingPlanDTO.class);
//     }

//     // ================= DELETE =================
//     public void deletePricingPlan(String id) {
//         pricingPlanRepository.deleteById(id);
//     }
// }
package com.sensei.backend.service;

import com.sensei.backend.entity.PricingPlan;
import java.util.*;

public interface PricingPlanService {

    PricingPlan create(PricingPlan plan);

    PricingPlan getById(UUID id);

    List<PricingPlan> getAll();

    PricingPlan update(UUID id, PricingPlan updatedPlan);

    void delete(UUID id);
}
