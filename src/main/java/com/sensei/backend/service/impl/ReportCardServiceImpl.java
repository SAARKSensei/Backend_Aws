package com.sensei.backend.service.impl;

import com.sensei.backend.dto.report.MasterReportCardResponse;
import com.sensei.backend.entity.ChildLifeSkill;
import com.sensei.backend.entity.ChildQuestionAttempt;
import com.sensei.backend.entity.ChildSubModuleCompletion;
import com.sensei.backend.entity.ChildUser;
import com.sensei.backend.entity.ParentUser;
import com.sensei.backend.enums.LifeSkillType;
import com.sensei.backend.repository.ChildLifeSkillRepository;
import com.sensei.backend.repository.ChildQuestionAttemptRepository;
import com.sensei.backend.repository.ChildSubModuleCompletionRepository;
import com.sensei.backend.repository.ChildUserRepository;
import com.sensei.backend.service.ReportCardService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ReportCardServiceImpl implements ReportCardService {

    private static final List<LifeSkillType> TIE_BREAKER_PRIORITY = Arrays.asList(
            LifeSkillType.PROBLEM_SOLVING,
            LifeSkillType.DECISION_MAKING,
            LifeSkillType.CREATIVE_THINKING,
            LifeSkillType.CRITICAL_THINKING,
            LifeSkillType.SELF_AWARENESS,
            LifeSkillType.EMPATHY,
            LifeSkillType.INTERPERSONAL_RELATIONSHIP,
            LifeSkillType.EFFECTIVE_COMMUNICATION,
            LifeSkillType.COPING_WITH_STRESS,
            LifeSkillType.COPING_WITH_EMOTIONS
    );

    private final ChildUserRepository childUserRepository;
    private final ChildLifeSkillRepository childLifeSkillRepository;
    private final ChildSubModuleCompletionRepository completionRepository;
    private final ChildQuestionAttemptRepository questionAttemptRepository;

    @Override
    @Transactional(readOnly = true)
    public MasterReportCardResponse generateMasterReportCard(UUID childId, int page, int size) {
        ChildUser child = childUserRepository.findById(childId)
                .orElseThrow(() -> new RuntimeException("Child not found"));
        
        ParentUser parent = child.getParentUser();

        MasterReportCardResponse.ChildProfileDto childProfile = MasterReportCardResponse.ChildProfileDto.builder()
                .name(child.getChildName())
                .grade(child.getGrade())
                .planStatus(child.getPlanStatus() != null ? child.getPlanStatus().name() : null)
                .planStartDate(child.getPlanStartDate())
                .planExpiryDate(child.getPlanExpiryDate())
                .build();

        MasterReportCardResponse.ParentProfileDto parentProfile = MasterReportCardResponse.ParentProfileDto.builder()
                .name(parent.getName())
                .email(parent.getEmail())
                .phone(parent.getPhone())
                .location(parent.getLocation())
                .build();

        // Life Skills
        List<ChildLifeSkill> lifeSkills = childLifeSkillRepository.findByChildUser_ChildId(childId);
        Map<LifeSkillType, Integer> scores = new HashMap<>();
        for (LifeSkillType type : LifeSkillType.values()) {
            scores.put(type, 0); // Default to 0
        }
        for (ChildLifeSkill skill : lifeSkills) {
            scores.put(skill.getLifeSkill(), skill.getScore() != null ? skill.getScore() : 0);
        }

        List<LifeSkillType> sortedSkills = new ArrayList<>(Arrays.asList(LifeSkillType.values()));
        sortedSkills.sort((s1, s2) -> {
            int scoreCompare = scores.get(s2).compareTo(scores.get(s1)); // descending
            if (scoreCompare != 0) {
                return scoreCompare;
            }
            // Tie breaker
            return Integer.compare(TIE_BREAKER_PRIORITY.indexOf(s1), TIE_BREAKER_PRIORITY.indexOf(s2));
        });

        List<LifeSkillType> showingLifeSkills = new ArrayList<>(sortedSkills.subList(0, Math.min(5, sortedSkills.size())));
        
        List<LifeSkillType> developFurtherLifeSkills = new ArrayList<>(sortedSkills.subList(Math.min(5, sortedSkills.size()), sortedSkills.size()));
        developFurtherLifeSkills.sort((s1, s2) -> {
            int scoreCompare = scores.get(s1).compareTo(scores.get(s2)); // ascending
            if (scoreCompare != 0) {
                return scoreCompare;
            }
            // Tie breaker for consistent ordering
            return Integer.compare(TIE_BREAKER_PRIORITY.indexOf(s1), TIE_BREAKER_PRIORITY.indexOf(s2));
        });

        // Completed Tasks (Modules/SubModules)
        List<ChildSubModuleCompletion> completions = completionRepository.findByChildId(childId);
        List<String> completedTaskNames = completions.stream()
                .map(c -> c.getSubModule().getName())
                .collect(Collectors.toList());
                
        // Calculate overall score (Average of submodule scores, or 0 if none)
        double overallScore = 0.0;
        if (!completions.isEmpty()) {
            double total = completions.stream()
                    .filter(c -> c.getScore() != null)
                    .mapToDouble(c -> c.getScore().doubleValue())
                    .sum();
            overallScore = total / completions.size();
        }

        // Wrong Questions (Paginated)
        org.springframework.data.domain.Pageable pageable = org.springframework.data.domain.PageRequest.of(page, size, org.springframework.data.domain.Sort.by("attemptedAt").descending());
        org.springframework.data.domain.Page<ChildQuestionAttempt> wrongAttemptsPage = questionAttemptRepository.findByChildIdAndIsCorrectFalse(childId, pageable);
        List<String> wrongQuestionsHistory = wrongAttemptsPage.getContent().stream()
                .map(a -> a.getQuestion().getQuestionText())
                .collect(Collectors.toList());

        return MasterReportCardResponse.builder()
                .childProfile(childProfile)
                .parentProfile(parentProfile)
                .showingLifeSkills(showingLifeSkills)
                .developFurtherLifeSkills(developFurtherLifeSkills)
                .completedTasks(completedTaskNames)
                .wrongQuestionsHistory(wrongQuestionsHistory)
                .overallScore(overallScore)
                .build();
    }
}
