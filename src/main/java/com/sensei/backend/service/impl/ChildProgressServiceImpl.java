package com.sensei.backend.service.impl;

import com.sensei.backend.dto.ChildUserDTO;
import com.sensei.backend.dto.progress.*;
import com.sensei.backend.entity.*;
import com.sensei.backend.entity.Module;
import com.sensei.backend.exception.ResourceNotFoundException;
import com.sensei.backend.repository.*;
import com.sensei.backend.service.ChildProgressService;
import com.sensei.backend.service.ChildLifeSkillService;
import com.sensei.backend.repository.ChildSubModuleProgressRepository;
import com.sensei.backend.repository.ChildModuleProgressRepository;
import com.sensei.backend.repository.ChildSubjectProgressRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
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

    private final ChildSubModuleProgressRepository childSubModuleProgressRepo;
    private final ChildModuleProgressRepository childModuleProgressRepo;
    private final ChildSubjectProgressRepository childSubjectProgressRepo;
    private final ModuleRepository moduleRepository;

    // -----------------------------------------
    // START INTERACTIVE ACTIVITY
    // -----------------------------------------
    @Override
    @Transactional
    public void startInteractiveActivity(StartActivityDTO dto) {

        InteractiveActivity activity = interactiveActivityRepository.findById(dto.getInteractiveActivityId())
                .orElseThrow(() -> new ResourceNotFoundException("Interactive Activity not found"));

        if (activityProgressRepo.findByChildIdAndInteractiveActivity(dto.getChildId(), activity).isPresent()) {
            return; // Idempotent: already started
        }

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
    @Transactional
    public void completeInteractiveActivity(CompleteActivityDTO dto) {

        InteractiveActivity activity = interactiveActivityRepository.findById(dto.getInteractiveActivityId())
                .orElseThrow(() -> new ResourceNotFoundException("Interactive Activity not found"));

        ChildInteractiveActivityProgress progress =
                activityProgressRepo.findByChildIdAndInteractiveActivity(dto.getChildId(), activity)
                        .orElseThrow(() -> new ResourceNotFoundException("Activity not started"));

        if ("COMPLETED".equals(progress.getStatus())) {
            tryAutoCompleteSubModule(dto.getChildId(), activity.getSubModule());
            updateHierarchicalProgress(dto.getChildId(), activity.getSubModule());
            return;
        }

        progress.setStatus("COMPLETED");
        progress.setCompletedAt(LocalDateTime.now());
        if (dto.getTimeTakenSeconds() != null) {
            progress.setTimeTakenSeconds(dto.getTimeTakenSeconds());
        }
        if (dto.getFeedbackStars() != null) {
            progress.setFeedbackStars(dto.getFeedbackStars());
        }
        if (dto.getFeedbackMessage() != null) {
            progress.setFeedbackMessage(dto.getFeedbackMessage());
        }
        activityProgressRepo.save(progress);

        tryAutoCompleteSubModule(dto.getChildId(), activity.getSubModule());
        updateHierarchicalProgress(dto.getChildId(), activity.getSubModule());
    }

    // -----------------------------------------
    // QUESTION ATTEMPT
    // -----------------------------------------
    @Override
    @Transactional
    public void recordQuestionAttempt(QuestionAttemptDTO dto) {

        Question question = questionRepository.findById(dto.getQuestionId())
                .orElseThrow(() -> new ResourceNotFoundException("Question not found"));

        QuestionOption option = questionOptionRepository.findById(dto.getOptionId())
                .orElseThrow(() -> new ResourceNotFoundException("Option not found"));

        if (!option.getQuestion().getId().equals(question.getId())) {
            throw new IllegalArgumentException("Option does not belong to the given question");
        }

        long previousAttempts =
                questionAttemptRepo.countByChildIdAndQuestion(dto.getChildId(), question);

        boolean isCorrect = Boolean.TRUE.equals(option.getIsCorrect());

        ChildQuestionAttempt attempt = ChildQuestionAttempt.builder()
                .childId(dto.getChildId())
                .question(question)
                .option(option)
                .isCorrect(isCorrect)
                .attemptNumber((int) previousAttempts + 1)
                .attemptedAt(LocalDateTime.now())
                .timeTakenSeconds(dto.getTimeTakenSeconds())
                .build();

        questionAttemptRepo.save(attempt);

        if (isCorrect) {
            tryAutoCompleteSubModule(dto.getChildId(), question.getDigitalActivity().getSubModule());
            updateHierarchicalProgress(dto.getChildId(), question.getDigitalActivity().getSubModule());
        }
    }

    // -----------------------------------------
    // AUTO COMPLETE SUBMODULE
    // -----------------------------------------
    private void tryAutoCompleteSubModule(UUID childId, SubModule subModule) {

        if (subModuleCompletionRepo.existsByChildIdAndSubModule(childId, subModule)) {
            return;
        }

        long totalActivities = interactiveActivityRepository.countBySubModuleIdAndIsActiveTrue(subModule.getId());
        long completedActivities =
                activityProgressRepo
                        .countByChildIdAndInteractiveActivity_SubModule_IdAndInteractiveActivity_IsActiveTrueAndStatus(
                                childId,
                                subModule.getId(),
                                "COMPLETED"
                        );

        if (totalActivities > 0 && completedActivities < totalActivities) return;

        long totalDigitals = digitalActivityRepository.countBySubModuleIdAndIsActiveTrue(subModule.getId());
        long completedDigitals =
                digitalProgressRepo
                        .countByChildIdAndDigitalActivity_SubModule_IdAndDigitalActivity_IsActiveTrueAndStatus(
                                childId,
                                subModule.getId(),
                                "COMPLETED"
                        );

        if (totalDigitals > 0 && completedDigitals < totalDigitals) return;

        if (totalActivities == 0 && totalDigitals == 0) return;

        List<DigitalActivity> digitals =
                digitalActivityRepository.findBySubModule_IdAndIsActiveTrueOrderByOrderIndexAsc(
                        subModule.getId()
                );

        long totalQuestions = 0;
        long correctQuestions = 0;

        for (DigitalActivity digital : digitals) {
            List<Question> questions =
                    questionRepository.findByDigitalActivity_IdAndIsActiveTrueOrderByOrderIndexAsc(
                            digital.getId()
                    );
            totalQuestions += questions.size();
            for (Question q : questions) {
                if (questionAttemptRepo.countByChildIdAndQuestionAndIsCorrect(childId, q, true) > 0) {
                    correctQuestions++;
                } else {
                    return; // All questions must be passed
                }
            }
        }

        double score = 100.0;
        if (totalQuestions > 0) {
            score = (correctQuestions * 100.0) / totalQuestions;
        }

        ChildSubModuleCompletion completion = ChildSubModuleCompletion.builder()
                .childId(childId)
                .subModule(subModule)
                .completedAt(LocalDateTime.now())
                .score(BigDecimal.valueOf(score))
                .remarks(score >= 80 ? "Strong mastery" : "Needs improvement")
                .build();

        subModuleCompletionRepo.save(completion);
        
    }

    // -----------------------------------------
    // DIGITAL START
    // -----------------------------------------
    @Override
    @Transactional
    public void startDigitalActivity(StartDigitalActivityDTO dto) {

        DigitalActivity digital = digitalActivityRepository.findById(dto.getDigitalActivityId())
                .orElseThrow(() -> new ResourceNotFoundException("Digital Activity not found"));

        if (digitalProgressRepo.findByChildIdAndDigitalActivity(dto.getChildId(), digital).isPresent()) {
            return; // Idempotent: already started
        }

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
    @Transactional
    public void completeDigitalActivity(CompleteDigitalActivityDTO dto) {

        DigitalActivity digital = digitalActivityRepository.findById(dto.getDigitalActivityId())
                .orElseThrow(() -> new ResourceNotFoundException("Digital Activity not found"));

        ChildDigitalActivityProgress progress =
                digitalProgressRepo
                        .findByChildIdAndDigitalActivity(dto.getChildId(), digital)
                        .orElseThrow(() -> new ResourceNotFoundException("Digital activity not started"));

        if (dto.getAttempts() != null && !dto.getAttempts().isEmpty()) {
            for (QuestionAttemptDTO attempt : dto.getAttempts()) {
                if (attempt.getChildId() == null) {
                    attempt.setChildId(dto.getChildId());
                }
                Question q = questionRepository.findById(attempt.getQuestionId())
                        .orElseThrow(() -> new ResourceNotFoundException("Question not found"));
                if (q.getDigitalActivity() == null || !q.getDigitalActivity().getId().equals(digital.getId())) {
                    throw new IllegalArgumentException("Question does not belong to the submitted digital activity");
                }
                recordQuestionAttempt(attempt);
            }
        }

        if ("COMPLETED".equals(progress.getStatus())) {
            tryAutoCompleteSubModule(dto.getChildId(), digital.getSubModule());
            updateHierarchicalProgress(dto.getChildId(), digital.getSubModule());
            return;
        }

        progress.setStatus("COMPLETED");
        progress.setCompletedAt(LocalDateTime.now());
        if (dto.getTimeTakenSeconds() != null) {
            progress.setTimeTakenSeconds(dto.getTimeTakenSeconds());
        }
        if (dto.getFeedbackStars() != null) {
            progress.setFeedbackStars(dto.getFeedbackStars());
        }
        if (dto.getFeedbackMessage() != null) {
            progress.setFeedbackMessage(dto.getFeedbackMessage());
        }

        digitalProgressRepo.save(progress);

        tryAutoCompleteSubModule(dto.getChildId(), digital.getSubModule());
        updateHierarchicalProgress(dto.getChildId(), digital.getSubModule());
    }

    private void updateHierarchicalProgress(UUID childId, SubModule subModule) {
        // SubModule Level
        long totalActivities = interactiveActivityRepository.countBySubModuleIdAndIsActiveTrue(subModule.getId()) +
                               digitalActivityRepository.countBySubModuleIdAndIsActiveTrue(subModule.getId());
        long completedActivities = activityProgressRepo.countByChildIdAndInteractiveActivity_SubModule_IdAndInteractiveActivity_IsActiveTrueAndStatus(childId, subModule.getId(), "COMPLETED") +
                                   digitalProgressRepo.countByChildIdAndDigitalActivity_SubModule_IdAndDigitalActivity_IsActiveTrueAndStatus(childId, subModule.getId(), "COMPLETED");
        
        ChildSubModuleProgress smProgress = childSubModuleProgressRepo.findByChildIdAndSubModuleId(childId, subModule.getId())
                .orElse(ChildSubModuleProgress.builder().childId(childId).subModuleId(subModule.getId()).build());
        smProgress.setTotalActivities((int) totalActivities);
        smProgress.setCompletedActivities((int) completedActivities);
        
        boolean isMastered = subModuleCompletionRepo.existsByChildIdAndSubModule(childId, subModule);
        smProgress.setIsCompleted(isMastered);
        
        smProgress.setUpdatedAt(LocalDateTime.now());
        childSubModuleProgressRepo.save(smProgress);

        // Module Level
        if (subModule.getModule() != null) {
            Module module = subModule.getModule();
            List<SubModule> subModulesInModule = subModuleRepository.findByModule_IdAndIsActiveTrueOrderByOrderIndexAsc(module.getId());
            long totalSubModules = subModulesInModule.size();
            long completedSubModules = 0;
            if (!subModulesInModule.isEmpty()) {
                completedSubModules = childSubModuleProgressRepo.countByChildIdAndSubModuleIdInAndIsCompletedTrue(
                    childId, 
                    subModulesInModule.stream().map(SubModule::getId).toList()
                );
            }

            ChildModuleProgress mProgress = childModuleProgressRepo.findByChildIdAndModuleId(childId, module.getId())
                    .orElse(ChildModuleProgress.builder().childId(childId).moduleId(module.getId()).build());
            mProgress.setTotalSubmodules((int) totalSubModules);
            mProgress.setCompletedSubmodules((int) completedSubModules);
            mProgress.setIsCompleted(totalSubModules > 0 && completedSubModules >= totalSubModules);
            mProgress.setUpdatedAt(LocalDateTime.now());
            childModuleProgressRepo.save(mProgress);

            // Subject Level
            if (module.getSubject() != null) {
                Subject subject = module.getSubject();
                List<Module> modulesInSubject = moduleRepository.findBySubjectIdAndIsActiveTrueOrderByOrderIndexAsc(subject.getId());
                long totalModules = modulesInSubject.size();
                long completedModules = 0;
                if (!modulesInSubject.isEmpty()) {
                    completedModules = childModuleProgressRepo.countByChildIdAndModuleIdInAndIsCompletedTrue(
                        childId,
                        modulesInSubject.stream().map(Module::getId).toList()
                    );
                }

                ChildSubjectProgress sProgress = childSubjectProgressRepo.findByChildIdAndSubjectId(childId, subject.getId())
                        .orElse(ChildSubjectProgress.builder().childId(childId).subjectId(subject.getId()).build());
                sProgress.setTotalModules((int) totalModules);
                sProgress.setCompletedModules((int) completedModules);
                sProgress.setIsCompleted(totalModules > 0 && completedModules >= totalModules);
                sProgress.setUpdatedAt(LocalDateTime.now());
                childSubjectProgressRepo.save(sProgress);
            }
        }
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