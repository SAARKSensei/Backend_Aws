package com.sensei.backend.adapter.persistence.jpa.repository;

import com.sensei.backend.adapter.persistence.jpa.entity.*;
import com.sensei.backend.application.dto.progress.*;
import com.sensei.backend.domain.port.repository.*;
import com.sensei.backend.domain.service.ChildProgressService;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
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

    // -----------------------------------------
    // START INTERACTIVE ACTIVITY
    // -----------------------------------------
    @Override
    public void startInteractiveActivity(StartActivityDTO dto) {

        InteractiveActivity activity = interactiveActivityRepository.findById(dto.getInteractiveActivityId())
                .orElseThrow(() -> new RuntimeException("Interactive Activity not found"));

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
                .orElseThrow(() -> new RuntimeException("Interactive Activity not found"));

        ChildInteractiveActivityProgress progress =
                activityProgressRepo.findByChildIdAndInteractiveActivity(dto.getChildId(), activity)
                        .orElseThrow(() -> new RuntimeException("Activity not started"));

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
                .orElseThrow(() -> new RuntimeException("Question not found"));

        QuestionOption option = questionOptionRepository.findById(dto.getOptionId())
                .orElseThrow(() -> new RuntimeException("Option not found"));

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

        // All interactive activities must be completed
        long totalActivities = interactiveActivityRepository.countBySubModuleId(subModule.getId());
        long completedActivities =
                activityProgressRepo.countByChildIdAndStatus(childId, "COMPLETED");

        if (completedActivities < totalActivities) return;

        // Get digital activity
        List<DigitalActivity> digitals =
        digitalActivityRepository.findBySubModule_IdAndIsActiveTrueOrderByOrderIndexAsc(
                subModule.getId()
        );
        if (digitals.isEmpty()) return;

        DigitalActivity digital = digitals.get(0);

        // Load all questions
       List<Question> questions =
        questionRepository.findByDigitalActivityIdOrderByOrderIndexAsc(digital.getId());


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
    }
}
