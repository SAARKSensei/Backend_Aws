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
                .orElseThrow(() -> new RuntimeException("Parent not found"));
        ChildUser child = childUserRepository.findById(request.getChildId())
                .orElseThrow(() -> new RuntimeException("Child not found"));



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
                .build();
        attemptRepository.save(attempt);
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
}
