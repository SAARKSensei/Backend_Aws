package com.sensei.backend.service.impl;

import com.sensei.backend.dto.ChildUserDTO;
import com.sensei.backend.dto.progress.*;
import com.sensei.backend.entity.*;
import com.sensei.backend.exception.ResourceNotFoundException;
import com.sensei.backend.repository.*;
import com.sensei.backend.service.ChildProgressService;
import com.sensei.backend.service.ChildLifeSkillService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ChildProgressServiceImpl implements ChildProgressService {

    private final ChildInteractiveActivityProgressRepository activityProgressRepo;
    private final ChildQuestionAttemptRepository questionAttemptRepo;
    private final ChildSubModuleCompletionRepository subModuleCompletionRepo;

    private final InteractiveActivityRepository interactiveActivityRepository;
    private final QuestionRepository questionRepository;
    private final QuestionOptionRepository questionOptionRepository;
    private final SubModuleRepository subModuleRepository;
    private final DigitalActivityRepository digitalActivityRepository;
    private final ChildDigitalActivityProgressRepository digitalProgressRepo;
    private final ParentUserRepository parentUserRepository;
    private final ChildUserRepository childUserRepository;
    private final ChildLifeSkillService childLifeSkillService;

    // -----------------------------------------
    // START INTERACTIVE ACTIVITY
    // -----------------------------------------
    @Override
    public void startInteractiveActivity(StartActivityDTO dto) {

        InteractiveActivity activity = interactiveActivityRepository.findById(dto.getInteractiveActivityId())
                .orElseThrow(() -> new ResourceNotFoundException("Interactive Activity not found"));

        activityProgressRepo.findByChildIdAndInteractiveActivity(dto.getChildId(), activity)
                .ifPresent(p -> {
                    throw new RuntimeException("Activity already started");
                });

        ChildInteractiveActivityProgress progress = ChildInteractiveActivityProgress.builder()
                .childId(dto.getChildId())
                .interactiveActivity(activity)
                .status("STARTED")
                .startedAt(LocalDateTime.now())
                .build();

        activityProgressRepo.save(progress);
    }

    // -----------------------------------------
    // COMPLETE INTERACTIVE ACTIVITY
    // -----------------------------------------
    @Override
    public void completeInteractiveActivity(CompleteActivityDTO dto) {

        InteractiveActivity activity = interactiveActivityRepository.findById(dto.getInteractiveActivityId())
                .orElseThrow(() -> new ResourceNotFoundException("Interactive Activity not found"));

        ChildInteractiveActivityProgress progress =
                activityProgressRepo.findByChildIdAndInteractiveActivity(dto.getChildId(), activity)
                        .orElseThrow(() -> new ResourceNotFoundException("Activity not started"));

        progress.setStatus("COMPLETED");
        progress.setCompletedAt(LocalDateTime.now());
        activityProgressRepo.save(progress);

        tryAutoCompleteSubModule(dto.getChildId(), activity.getSubModule());
    }

    // -----------------------------------------
    // QUESTION ATTEMPT
    // -----------------------------------------
    @Override
    public void recordQuestionAttempt(QuestionAttemptDTO dto) {

        Question question = questionRepository.findById(dto.getQuestionId())
                .orElseThrow(() -> new ResourceNotFoundException("Question not found"));

        QuestionOption option = questionOptionRepository.findById(dto.getOptionId())
                .orElseThrow(() -> new ResourceNotFoundException("Option not found"));

        long previousAttempts =
                questionAttemptRepo.countByChildIdAndQuestion(dto.getChildId(), question);

        boolean isCorrect = "CORRECT".equalsIgnoreCase(option.getStatus());

        ChildQuestionAttempt attempt = ChildQuestionAttempt.builder()
                .childId(dto.getChildId())
                .question(question)
                .option(option)
                .isCorrect(isCorrect)
                .attemptNumber((int) previousAttempts + 1)
                .attemptedAt(LocalDateTime.now())
                .build();

        questionAttemptRepo.save(attempt);

        if (isCorrect) {
            tryAutoCompleteSubModule(dto.getChildId(), question.getDigitalActivity().getSubModule());
        }
    }

    // -----------------------------------------
    // AUTO COMPLETE SUBMODULE
    // -----------------------------------------
    private void tryAutoCompleteSubModule(UUID childId, SubModule subModule) {

        if (subModuleCompletionRepo.existsByChildIdAndSubModule(childId, subModule)) {
            return;
        }

        long totalActivities = interactiveActivityRepository.countBySubModuleId(subModule.getId());

        long completedActivities =
                activityProgressRepo
                        .countByChildIdAndInteractiveActivity_SubModule_IdAndStatus(
                                childId,
                                subModule.getId(),
                                "COMPLETED"
                        );

        if (completedActivities < totalActivities) return;

        long totalDigitals = digitalActivityRepository.countBySubModule_Id(subModule.getId());

        long completedDigitals =
                digitalProgressRepo
                        .countByChildIdAndDigitalActivity_SubModule_IdAndStatus(
                                childId,
                                subModule.getId(),
                                "COMPLETED"
                        );

        if (completedDigitals < totalDigitals) return;

        List<DigitalActivity> digitals =
                digitalActivityRepository.findBySubModule_IdAndIsActiveTrueOrderByOrderIndexAsc(
                        subModule.getId()
                );

        if (digitals.isEmpty()) return;

        DigitalActivity digital = digitals.get(0);

        List<Question> questions =
                questionRepository.findByDigitalActivity_IdAndIsActiveTrueOrderByOrderIndexAsc(
                        digital.getId()
                );

        boolean allPassed = questions.stream().allMatch(q ->
                questionAttemptRepo.countByChildIdAndQuestionAndIsCorrect(childId, q, true) > 0
        );

        if (!allPassed) return;

        long totalQuestions = questions.size();

        long correctQuestions = questions.stream()
                .filter(q -> questionAttemptRepo.countByChildIdAndQuestionAndIsCorrect(childId, q, true) > 0)
                .count();

        double score = (correctQuestions * 100.0) / totalQuestions;

        ChildSubModuleCompletion completion = ChildSubModuleCompletion.builder()
                .childId(childId)
                .subModule(subModule)
                .completedAt(LocalDateTime.now())
                .score(BigDecimal.valueOf(score))
                .remarks(score >= 80 ? "Strong mastery" : "Needs improvement")
                .build();

        subModuleCompletionRepo.save(completion);
        
        if (subModule.getModule() != null && subModule.getModule().getAssociatedLifeSkill() != null) {
            childLifeSkillService.addLifeSkillPoints(childId, subModule.getModule().getAssociatedLifeSkill(), 1);
        }
    }

    // -----------------------------------------
    // DIGITAL START
    // -----------------------------------------
    @Override
    public void startDigitalActivity(StartDigitalActivityDTO dto) {

        DigitalActivity digital = digitalActivityRepository.findById(dto.getDigitalActivityId())
                .orElseThrow(() -> new ResourceNotFoundException("Digital Activity not found"));

        digitalProgressRepo
                .findByChildIdAndDigitalActivity(dto.getChildId(), digital)
                .ifPresent(p -> {
                    throw new RuntimeException("Digital activity already started");
                });

        ChildDigitalActivityProgress progress = ChildDigitalActivityProgress.builder()
                .childId(dto.getChildId())
                .digitalActivity(digital)
                .status("STARTED")
                .startedAt(LocalDateTime.now())
                .build();

        digitalProgressRepo.save(progress);
    }

    // -----------------------------------------
    // DIGITAL COMPLETE
    // -----------------------------------------
    @Override
    public void completeDigitalActivity(CompleteDigitalActivityDTO dto) {

        DigitalActivity digital = digitalActivityRepository.findById(dto.getDigitalActivityId())
                .orElseThrow(() -> new ResourceNotFoundException("Digital Activity not found"));

        ChildDigitalActivityProgress progress =
                digitalProgressRepo
                        .findByChildIdAndDigitalActivity(dto.getChildId(), digital)
                        .orElseThrow(() -> new ResourceNotFoundException("Digital activity not started"));

        progress.setStatus("COMPLETED");
        progress.setCompletedAt(LocalDateTime.now());

        digitalProgressRepo.save(progress);

        tryAutoCompleteSubModule(dto.getChildId(), digital.getSubModule());
    }

    // -----------------------------------------
    // SUMMARY
    // -----------------------------------------
    @Override
    public ChildProgressSummaryDTO getChildProgressSummary(UUID childId) {

        long totalSubModules = subModuleRepository.count();

        long completedSubModules =
                subModuleCompletionRepo.countByChildId(childId);

        double progressPercentage =
                totalSubModules == 0 ? 0 :
                        (completedSubModules * 100.0) / totalSubModules;

        long totalActivities = interactiveActivityRepository.count();

        long completedActivities =
                activityProgressRepo.countByChildIdAndStatus(childId, "COMPLETED");

        long totalDigitals = digitalActivityRepository.count();

        long completedDigitals =
                digitalProgressRepo.countByChildIdAndStatus(childId, "COMPLETED");

        return ChildProgressSummaryDTO.builder()
                .totalSubModules(totalSubModules)
                .completedSubModules(completedSubModules)
                .progressPercentage(progressPercentage)
                .totalActivities(totalActivities)
                .completedActivities(completedActivities)
                .totalDigitals(totalDigitals)
                .completedDigitals(completedDigitals)
                .build();
    }

    // -----------------------------------------
    // LOCATION PROGRESS
    // -----------------------------------------
    @Override
    public SchoolProgressDTO getLocationProgress(String location) {

        List<ParentUser> parents =
                parentUserRepository.findByLocation(location);

        List<ChildUser> children = parents.stream()
                .flatMap(p -> p.getChildUsers().stream())
                .toList();

        long totalChildren = children.size();

        long completedSubModules = 0;
        double totalProgress = 0;

        for (ChildUser child : children) {

            long childCompleted =
                    subModuleCompletionRepo.countByChildId(child.getChildId());

            completedSubModules += childCompleted;

            long totalSubModules = subModuleRepository.count();

            double progress =
                    totalSubModules == 0 ? 0 :
                            (childCompleted * 100.0) / totalSubModules;

            totalProgress += progress;
        }

        double avgProgress =
                totalChildren == 0 ? 0 :
                        totalProgress / totalChildren;

        return SchoolProgressDTO.builder()
                .schoolName(location)
                .totalChildren(totalChildren)
                .activeChildren(totalChildren)
                .averageProgress(avgProgress)
                .completedSubModules(completedSubModules)
                .build();
    }

    // -----------------------------------------
    // FILTER CHILDREN
    // -----------------------------------------
    @Override
    public List<ChildUserDTO> getChildrenByFilter(String school, String location) {

        List<ChildUser> children = childUserRepository.findAll();

        return children.stream()
                .filter(c -> school == null || school.equals(c.getSchoolName()))
                .filter(c -> location == null ||
                        location.equals(c.getParentUser().getLocation()))
                .map(c -> {
                    ChildUserDTO dto = new ChildUserDTO();
                    dto.setChildId(c.getChildId());
                    dto.setChildName(c.getChildName());
                    dto.setSchoolName(c.getSchoolName());
                    return dto;
                })
                .toList();
    }

    // -----------------------------------------
    // SUBMODULE PROGRESS
    // -----------------------------------------
    @Override
    public List<SubModuleProgressDTO> getSubModuleProgress(UUID childId) {

        List<SubModule> subModules = subModuleRepository.findAll();

        return subModules.stream().map(sm -> {

            ChildSubModuleCompletion completion =
                    subModuleCompletionRepo
                            .findByChildIdAndSubModule(childId, sm)
                            .orElse(null);

            return SubModuleProgressDTO.builder()
                    .subModuleId(sm.getId())
                    .completed(completion != null)
                    .score(completion != null ? completion.getScore().doubleValue() : null)
                    .build();
        }).toList();
    }

    // -----------------------------------------
    // ACTIVITY PROGRESS
    // -----------------------------------------
    @Override
    public List<ActivityProgressDTO> getActivityProgress(UUID childId) {

        List<ChildInteractiveActivityProgress> progressList =
                activityProgressRepo.findByChildId(childId);

        return progressList.stream()
                .map(p -> ActivityProgressDTO.builder()
                        .activityId(p.getInteractiveActivity().getId())
                        .status(p.getStatus())
                        .build())
                .toList();
    }

    // -----------------------------------------
    // DIGITAL PROGRESS
    // -----------------------------------------
    @Override
    public List<DigitalProgressDTO> getDigitalProgress(UUID childId) {

        List<ChildDigitalActivityProgress> progressList =
                digitalProgressRepo.findByChildId(childId);

        return progressList.stream()
                .map(p -> DigitalProgressDTO.builder()
                        .digitalActivityId(p.getDigitalActivity().getId())
                        .status(p.getStatus())
                        .build())
                .toList();
    }

    // -----------------------------------------
    // SCHOOL PROGRESS
    // -----------------------------------------
    @Override
    public SchoolProgressDTO getSchoolProgress(String schoolName) {

        List<ChildUser> children =
                childUserRepository.findBySchoolName(schoolName);

        long totalChildren = children.size();

        long completedSubModules = 0;
        double totalProgress = 0;

        for (ChildUser child : children) {

            long childCompleted =
                    subModuleCompletionRepo.countByChildId(child.getChildId());

            completedSubModules += childCompleted;

            long totalSubModules = subModuleRepository.count();

            double progress =
                    totalSubModules == 0 ? 0 :
                            (childCompleted * 100.0) / totalSubModules;

            totalProgress += progress;
        }

        double avgProgress =
                totalChildren == 0 ? 0 :
                        totalProgress / totalChildren;

        return SchoolProgressDTO.builder()
                .schoolName(schoolName)
                .totalChildren(totalChildren)
                .activeChildren(totalChildren)
                .averageProgress(avgProgress)
                .completedSubModules(completedSubModules)
                .build();
    }
}