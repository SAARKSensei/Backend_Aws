package com.sensei.backend.service.impl;

import com.sensei.backend.dto.lifeskills.ChildLifeSkillReportResponse;
import com.sensei.backend.entity.ChildLifeSkill;
import com.sensei.backend.entity.ChildUser;
import com.sensei.backend.enums.LifeSkillType;
import com.sensei.backend.repository.ChildLifeSkillRepository;
import com.sensei.backend.repository.ChildUserRepository;
import com.sensei.backend.service.ChildLifeSkillService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ChildLifeSkillServiceImpl implements ChildLifeSkillService {

    private final ChildLifeSkillRepository childLifeSkillRepository;
    private final ChildUserRepository childUserRepository;

    @Override
    public ChildLifeSkillReportResponse getChildLifeSkills(UUID childId) {
        List<ChildLifeSkill> existingSkills = childLifeSkillRepository.findByChildUser_ChildId(childId);
        
        List<LifeSkillType> showing = Arrays.stream(LifeSkillType.values())
                .filter(type -> existingSkills.stream()
                        .anyMatch(skill -> skill.getLifeSkill() == type && skill.getScore() != null && skill.getScore() > 0))
                .collect(Collectors.toList());
                
        List<LifeSkillType> toDevelopFurther = Arrays.stream(LifeSkillType.values())
                .filter(type -> !showing.contains(type))
                .collect(Collectors.toList());
        
        return ChildLifeSkillReportResponse.builder()
                .showing(showing)
                .toDevelopFurther(toDevelopFurther)
                .build();
    }

    @Override
    public void addLifeSkillPoints(UUID childId, LifeSkillType lifeSkill, int points) {
        if (lifeSkill == null || points <= 0) return;
        
        Optional<ChildLifeSkill> existingSkillOpt = childLifeSkillRepository.findByChildUser_ChildIdAndLifeSkill(childId, lifeSkill);
        
        if (existingSkillOpt.isPresent()) {
            ChildLifeSkill existingSkill = existingSkillOpt.get();
            existingSkill.setScore((existingSkill.getScore() == null ? 0 : existingSkill.getScore()) + points);
            childLifeSkillRepository.save(existingSkill);
        } else {
            ChildUser childUser = childUserRepository.findById(childId)
                    .orElseThrow(() -> new RuntimeException("Child not found"));
            
            ChildLifeSkill newSkill = ChildLifeSkill.builder()
                    .childUser(childUser)
                    .lifeSkill(lifeSkill)
                    .score(points)
                    .build();
            childLifeSkillRepository.save(newSkill);
        }
    }
}
