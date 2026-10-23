package com.sensei.backend.service.impl;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.sensei.backend.dto.parentquiz.CreateQuizOptionDto;
import com.sensei.backend.dto.parentquiz.CreateQuizQuestionRequest;
import com.sensei.backend.dto.parentquiz.ParentQuizOptionDto;
import com.sensei.backend.dto.parentquiz.ParentQuizResponse;
import com.sensei.backend.dto.parentquiz.SubmitQuizRequest;
import com.sensei.backend.entity.ChildUser;
import com.sensei.backend.entity.ParentQuizAttempt;
import com.sensei.backend.entity.ParentQuizOption;
import com.sensei.backend.entity.ParentQuizQuestion;
import com.sensei.backend.entity.ParentUser;
import com.sensei.backend.repository.ChildUserRepository;
import com.sensei.backend.repository.ParentQuizAttemptRepository;
import com.sensei.backend.repository.ParentQuizOptionRepository;
import com.sensei.backend.repository.ParentQuizQuestionRepository;
import com.sensei.backend.repository.ParentUserRepository;
import com.sensei.backend.service.ChildLifeSkillService;
import com.sensei.backend.service.ParentQuizService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ParentQuizServiceImpl implements ParentQuizService {

    private final ParentQuizQuestionRepository questionRepository;
    private final ParentQuizOptionRepository optionRepository;
    private final ParentQuizAttemptRepository attemptRepository;
    private final ChildUserRepository childUserRepository;
    private final ParentUserRepository parentUserRepository;
    private final ChildLifeSkillService childLifeSkillService;

    @Override
    @Transactional(readOnly = true)
    public List<ParentQuizResponse> getQuizQuestions() {
        List<ParentQuizQuestion> questions = questionRepository.findAllByIsActiveTrueOrderByOrderIndexAsc();
        
        return questions.stream().map(q -> {
            List<ParentQuizOption> options = optionRepository.findByQuestion_Id(q.getId());
            List<ParentQuizOptionDto> optionDtos = options.stream()
                    .map(o -> new ParentQuizOptionDto(o.getId(), o.getOptionText(), o.getAssociatedLifeSkills()))
                    .collect(Collectors.toList());
                    
            return ParentQuizResponse.builder()
                    .questionId(q.getId())
                    .questionText(q.getQuestionText())
                    .orderIndex(q.getOrderIndex())
                    .options(optionDtos)
                    .build();
        }).collect(Collectors.toList());
    }

    @Override
    @Transactional
    public void submitQuiz(SubmitQuizRequest request) {
        // Ensure the parent and child exist
        ParentUser parent = parentUserRepository.findById(request.getParentId())
                .orElseThrow(() -> new com.sensei.backend.exception.ResourceNotFoundException("Parent not found"));
        ChildUser child = childUserRepository.findById(request.getChildId())
                .orElseThrow(() -> new com.sensei.backend.exception.ResourceNotFoundException("Child not found"));

        if (attemptRepository.existsByParentUser_ParentIdAndChildUser_ChildId(parent.getParentId(), child.getChildId())) {
            updateQuiz(request);
            return;
        }

        // Process options and unlock lifeskills
        List<ParentQuizOption> selectedOptions = optionRepository.findAllById(request.getSelectedOptionIds());
        for (ParentQuizOption option : selectedOptions) {
            if (option.getAssociatedLifeSkills() != null) {
                for (var skill : option.getAssociatedLifeSkills()) {
                    childLifeSkillService.addLifeSkillPoints(child.getChildId(), skill, 1);
                }
            }
        }

        // Record attempt
        ParentQuizAttempt attempt = ParentQuizAttempt.builder()
                .parentUser(parent)
                .childUser(child)
                .selectedOptions(selectedOptions)
                .build();
        attemptRepository.save(attempt);

        // Update parent user isQuizCompleted only if all active questions are answered
        long totalActive = questionRepository.countByIsActiveTrue();
        long answeredCount = selectedOptions.stream()
                .map(opt -> opt.getQuestion().getId())
                .distinct()
                .count();
        boolean isAllCompleted = totalActive > 0 && answeredCount >= totalActive;
        parent.setIsQuizCompleted(isAllCompleted);
        parentUserRepository.save(parent);
    }

    @Override
    @Transactional
    public void updateQuiz(SubmitQuizRequest request) {
        ParentUser parent = parentUserRepository.findById(request.getParentId())
                .orElseThrow(() -> new com.sensei.backend.exception.ResourceNotFoundException("Parent not found"));
        ChildUser child = childUserRepository.findById(request.getChildId())
                .orElseThrow(() -> new com.sensei.backend.exception.ResourceNotFoundException("Child not found"));

        ParentQuizAttempt attempt = attemptRepository.findFirstByParentUser_ParentIdAndChildUser_ChildIdOrderByCompletedAtDesc(request.getParentId(), request.getChildId())
                .orElseThrow(() -> new com.sensei.backend.exception.ResourceNotFoundException("Previous quiz attempt not found"));

        List<ParentQuizOption> currentSelectedOptions = new java.util.ArrayList<>(
                attempt.getSelectedOptions() != null ? attempt.getSelectedOptions() : java.util.Collections.emptyList()
        );
        List<ParentQuizOption> newOptions = optionRepository.findAllById(request.getSelectedOptionIds());

        for (ParentQuizOption newOption : newOptions) {
            java.util.UUID questionId = newOption.getQuestion().getId();

            // Find the old option for this specific question
            ParentQuizOption oldOptionForQuestion = currentSelectedOptions.stream()
                    .filter(opt -> opt.getQuestion().getId().equals(questionId))
                    .findFirst()
                    .orElse(null);

            // If the option hasn't changed for this question, skip to the next
            if (oldOptionForQuestion != null && oldOptionForQuestion.getId().equals(newOption.getId())) {
                continue;
            }

            // Revert old option's life skills if it exists
            if (oldOptionForQuestion != null) {
                if (oldOptionForQuestion.getAssociatedLifeSkills() != null) {
                    for (var skill : oldOptionForQuestion.getAssociatedLifeSkills()) {
                        childLifeSkillService.removeLifeSkillPoints(child.getChildId(), skill, 1);
                    }
                }
                currentSelectedOptions.remove(oldOptionForQuestion);
            }

            // Apply new option's life skills
            if (newOption.getAssociatedLifeSkills() != null) {
                for (var skill : newOption.getAssociatedLifeSkills()) {
                    childLifeSkillService.addLifeSkillPoints(child.getChildId(), skill, 1);
                }
            }
            
            // Add the newly selected option
            currentSelectedOptions.add(newOption);
        }

        // Update the attempt with the merged list
        attempt.setSelectedOptions(currentSelectedOptions);
        attemptRepository.save(attempt);

        // Update parent user isQuizCompleted if all active questions are now answered
        long totalActive = questionRepository.countByIsActiveTrue();
        long answeredCount = currentSelectedOptions.stream()
                .map(opt -> opt.getQuestion().getId())
                .distinct()
                .count();
        boolean isAllCompleted = totalActive > 0 && answeredCount >= totalActive;
        parent.setIsQuizCompleted(isAllCompleted);
        parentUserRepository.save(parent);
    }

    @Override
    @Transactional
    public void addQuizQuestions(List<CreateQuizQuestionRequest> requestList) {
        for (CreateQuizQuestionRequest request : requestList) {
            ParentQuizQuestion question = ParentQuizQuestion.builder()
                    .questionText(request.getQuestionText())
                    .orderIndex(request.getOrderIndex())
                    .isActive(true)
                    .build();
                    
            question = questionRepository.save(question);
            
            if (request.getOptions() != null) {
                for (CreateQuizOptionDto optionDto : request.getOptions()) {
                    ParentQuizOption option = ParentQuizOption.builder()
                            .question(question)
                            .optionText(optionDto.getOptionText())
                            .associatedLifeSkills(optionDto.getLifeSkills())
                            .build();
                    optionRepository.save(option);
                }
            }
        }
    }

    @Override
    @Transactional(readOnly = true)
    public com.sensei.backend.dto.parentquiz.ParentQuizAttemptResponse getQuizAttempt(java.util.UUID parentId, java.util.UUID childId) {
        ParentQuizAttempt attempt = attemptRepository.findFirstByParentUser_ParentIdAndChildUser_ChildIdOrderByCompletedAtDesc(parentId, childId)
                .orElseThrow(() -> new com.sensei.backend.exception.ResourceNotFoundException("Quiz attempt not found"));

        java.util.List<ParentQuizOption> selectedOptions = attempt.getSelectedOptions() != null
                ? attempt.getSelectedOptions()
                : java.util.Collections.emptyList();

        java.util.List<java.util.UUID> selectedOptionIds = selectedOptions.stream()
                .map(ParentQuizOption::getId)
                .collect(Collectors.toList());

        java.util.Set<java.util.UUID> answeredQuestionIds = selectedOptions.stream()
                .map(opt -> opt.getQuestion().getId())
                .collect(Collectors.toSet());

        List<ParentQuizQuestion> activeQuestions = questionRepository.findAllByIsActiveTrueOrderByOrderIndexAsc();
        List<java.util.UUID> unansweredQuestionIds = activeQuestions.stream()
                .map(ParentQuizQuestion::getId)
                .filter(qId -> !answeredQuestionIds.contains(qId))
                .collect(Collectors.toList());

        boolean isCompleted = !activeQuestions.isEmpty() && answeredQuestionIds.size() >= activeQuestions.size();

        return com.sensei.backend.dto.parentquiz.ParentQuizAttemptResponse.builder()
                .parentId(parentId)
                .childId(childId)
                .selectedOptionIds(selectedOptionIds)
                .answeredQuestionIds(new java.util.ArrayList<>(answeredQuestionIds))
                .unansweredQuestionIds(unansweredQuestionIds)
                .isQuizCompleted(isCompleted)
                .totalQuestions(activeQuestions.size())
                .answeredCount(answeredQuestionIds.size())
                .build();
    }

    @Override
    @Transactional(readOnly = true)
    public List<ParentQuizResponse> getUnfilledQuizQuestions(java.util.UUID parentId, java.util.UUID childId) {
        List<ParentQuizQuestion> activeQuestions = questionRepository.findAllByIsActiveTrueOrderByOrderIndexAsc();

        java.util.Optional<ParentQuizAttempt> attemptOpt = attemptRepository.findFirstByParentUser_ParentIdAndChildUser_ChildIdOrderByCompletedAtDesc(parentId, childId);

        java.util.Set<java.util.UUID> answeredQuestionIds = attemptOpt.map(attempt ->
                attempt.getSelectedOptions() != null
                        ? attempt.getSelectedOptions().stream().map(opt -> opt.getQuestion().getId()).collect(Collectors.toSet())
                        : java.util.Collections.<java.util.UUID>emptySet()
        ).orElse(java.util.Collections.emptySet());

        return activeQuestions.stream()
                .filter(q -> !answeredQuestionIds.contains(q.getId()))
                .map(q -> {
                    List<ParentQuizOption> options = optionRepository.findByQuestion_Id(q.getId());
                    List<ParentQuizOptionDto> optionDtos = options.stream()
                            .map(o -> new ParentQuizOptionDto(o.getId(), o.getOptionText(), o.getAssociatedLifeSkills()))
                            .collect(Collectors.toList());

                    return ParentQuizResponse.builder()
                            .questionId(q.getId())
                            .questionText(q.getQuestionText())
                            .orderIndex(q.getOrderIndex())
                            .options(optionDtos)
                            .build();
                })
                .collect(Collectors.toList());
    }
}
