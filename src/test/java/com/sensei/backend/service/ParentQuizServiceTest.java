package com.sensei.backend.service;

import com.sensei.backend.dto.parentquiz.ParentQuizAttemptResponse;
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
import com.sensei.backend.service.impl.ParentQuizServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ParentQuizServiceTest {

    @Mock
    private ParentQuizQuestionRepository questionRepository;

    @Mock
    private ParentQuizOptionRepository optionRepository;

    @Mock
    private ParentQuizAttemptRepository attemptRepository;

    @Mock
    private ChildUserRepository childUserRepository;

    @Mock
    private ParentUserRepository parentUserRepository;

    @Mock
    private ChildLifeSkillService childLifeSkillService;

    @InjectMocks
    private ParentQuizServiceImpl parentQuizService;

    private UUID parentId;
    private UUID childId;
    private ParentUser parent;
    private ChildUser child;
    private List<ParentQuizQuestion> activeQuestions;

    @BeforeEach
    void setUp() {
        parentId = UUID.randomUUID();
        childId = UUID.randomUUID();

        parent = new ParentUser();
        parent.setParentId(parentId);
        parent.setIsQuizCompleted(false);

        child = new ChildUser();
        child.setChildId(childId);

        activeQuestions = new ArrayList<>();
        for (int i = 1; i <= 7; i++) {
            ParentQuizQuestion q = ParentQuizQuestion.builder()
                    .id(UUID.randomUUID())
                    .questionText("Question " + i)
                    .orderIndex(i)
                    .isActive(true)
                    .build();
            activeQuestions.add(q);
        }
    }

    @Test
    void testPartialSubmitDoesNotSetIsQuizCompleted() {
        when(parentUserRepository.findById(parentId)).thenReturn(Optional.of(parent));
        when(childUserRepository.findById(childId)).thenReturn(Optional.of(child));
        when(attemptRepository.existsByParentUser_ParentIdAndChildUser_ChildId(parentId, childId)).thenReturn(false);

        // 3 out of 7 questions answered
        List<UUID> selectedOptionIds = List.of(UUID.randomUUID(), UUID.randomUUID(), UUID.randomUUID());
        List<ParentQuizOption> selectedOptions = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            ParentQuizOption opt = ParentQuizOption.builder()
                    .id(selectedOptionIds.get(i))
                    .question(activeQuestions.get(i))
                    .optionText("Option " + (i + 1))
                    .build();
            selectedOptions.add(opt);
        }

        when(optionRepository.findAllById(selectedOptionIds)).thenReturn(selectedOptions);
        when(questionRepository.countByIsActiveTrue()).thenReturn(7L);

        SubmitQuizRequest request = new SubmitQuizRequest();
        request.setParentId(parentId);
        request.setChildId(childId);
        request.setSelectedOptionIds(selectedOptionIds);

        parentQuizService.submitQuiz(request);

        verify(attemptRepository, times(1)).save(any(ParentQuizAttempt.class));
        verify(parentUserRepository, times(1)).save(parent);
        assertFalse(parent.getIsQuizCompleted(), "Parent isQuizCompleted must be false when only 3 of 7 questions are answered");
    }

    @Test
    void testFullSubmitSetsIsQuizCompletedTrue() {
        when(parentUserRepository.findById(parentId)).thenReturn(Optional.of(parent));
        when(childUserRepository.findById(childId)).thenReturn(Optional.of(child));
        when(attemptRepository.existsByParentUser_ParentIdAndChildUser_ChildId(parentId, childId)).thenReturn(false);

        // All 7 questions answered
        List<UUID> selectedOptionIds = new ArrayList<>();
        List<ParentQuizOption> selectedOptions = new ArrayList<>();
        for (int i = 0; i < 7; i++) {
            UUID optId = UUID.randomUUID();
            selectedOptionIds.add(optId);
            ParentQuizOption opt = ParentQuizOption.builder()
                    .id(optId)
                    .question(activeQuestions.get(i))
                    .optionText("Option " + (i + 1))
                    .build();
            selectedOptions.add(opt);
        }

        when(optionRepository.findAllById(selectedOptionIds)).thenReturn(selectedOptions);
        when(questionRepository.countByIsActiveTrue()).thenReturn(7L);

        SubmitQuizRequest request = new SubmitQuizRequest();
        request.setParentId(parentId);
        request.setChildId(childId);
        request.setSelectedOptionIds(selectedOptionIds);

        parentQuizService.submitQuiz(request);

        verify(attemptRepository, times(1)).save(any(ParentQuizAttempt.class));
        verify(parentUserRepository, times(1)).save(parent);
        assertTrue(parent.getIsQuizCompleted(), "Parent isQuizCompleted must be true when all 7 questions are answered");
    }

    @Test
    void testUpdateRemainingQuestionsCompletesQuiz() {
        when(parentUserRepository.findById(parentId)).thenReturn(Optional.of(parent));
        when(childUserRepository.findById(childId)).thenReturn(Optional.of(child));

        // Existing attempt has first 3 questions
        List<ParentQuizOption> initialOptions = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            ParentQuizOption opt = ParentQuizOption.builder()
                    .id(UUID.randomUUID())
                    .question(activeQuestions.get(i))
                    .optionText("Initial Option " + (i + 1))
                    .build();
            initialOptions.add(opt);
        }

        ParentQuizAttempt attempt = ParentQuizAttempt.builder()
                .id(UUID.randomUUID())
                .parentUser(parent)
                .childUser(child)
                .selectedOptions(initialOptions)
                .build();

        when(attemptRepository.findFirstByParentUser_ParentIdAndChildUser_ChildIdOrderByCompletedAtDesc(parentId, childId))
                .thenReturn(Optional.of(attempt));

        // Submit remaining 4 questions
        List<UUID> remainingOptionIds = new ArrayList<>();
        List<ParentQuizOption> remainingOptions = new ArrayList<>();
        for (int i = 3; i < 7; i++) {
            UUID optId = UUID.randomUUID();
            remainingOptionIds.add(optId);
            ParentQuizOption opt = ParentQuizOption.builder()
                    .id(optId)
                    .question(activeQuestions.get(i))
                    .optionText("Remaining Option " + (i + 1))
                    .build();
            remainingOptions.add(opt);
        }

        when(optionRepository.findAllById(remainingOptionIds)).thenReturn(remainingOptions);
        when(questionRepository.countByIsActiveTrue()).thenReturn(7L);

        SubmitQuizRequest request = new SubmitQuizRequest();
        request.setParentId(parentId);
        request.setChildId(childId);
        request.setSelectedOptionIds(remainingOptionIds);

        parentQuizService.updateQuiz(request);

        assertEquals(7, attempt.getSelectedOptions().size(), "Attempt should now have all 7 options");
        verify(attemptRepository, times(1)).save(attempt);
        verify(parentUserRepository, times(1)).save(parent);
        assertTrue(parent.getIsQuizCompleted(), "Parent isQuizCompleted must be true after updating remaining questions");
    }

    @Test
    void testGetUnfilledQuizQuestionsReturnsOnlyUnanswered() {
        // First 3 answered
        List<ParentQuizOption> answeredOptions = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            ParentQuizOption opt = ParentQuizOption.builder()
                    .id(UUID.randomUUID())
                    .question(activeQuestions.get(i))
                    .optionText("Option " + (i + 1))
                    .build();
            answeredOptions.add(opt);
        }

        ParentQuizAttempt attempt = ParentQuizAttempt.builder()
                .id(UUID.randomUUID())
                .parentUser(parent)
                .childUser(child)
                .selectedOptions(answeredOptions)
                .build();

        when(questionRepository.findAllByIsActiveTrueOrderByOrderIndexAsc()).thenReturn(activeQuestions);
        when(attemptRepository.findFirstByParentUser_ParentIdAndChildUser_ChildIdOrderByCompletedAtDesc(parentId, childId))
                .thenReturn(Optional.of(attempt));

        List<ParentQuizResponse> unfilled = parentQuizService.getUnfilledQuizQuestions(parentId, childId);

        assertEquals(4, unfilled.size(), "Should return exactly the 4 unanswered questions");
        assertEquals("Question 4", unfilled.get(0).getQuestionText());
        assertEquals("Question 5", unfilled.get(1).getQuestionText());
        assertEquals("Question 6", unfilled.get(2).getQuestionText());
        assertEquals("Question 7", unfilled.get(3).getQuestionText());
    }

    @Test
    void testGetQuizAttemptWithTrackingDetails() {
        List<ParentQuizOption> answeredOptions = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            ParentQuizOption opt = ParentQuizOption.builder()
                    .id(UUID.randomUUID())
                    .question(activeQuestions.get(i))
                    .optionText("Option " + (i + 1))
                    .build();
            answeredOptions.add(opt);
        }

        ParentQuizAttempt attempt = ParentQuizAttempt.builder()
                .id(UUID.randomUUID())
                .parentUser(parent)
                .childUser(child)
                .selectedOptions(answeredOptions)
                .build();

        when(attemptRepository.findFirstByParentUser_ParentIdAndChildUser_ChildIdOrderByCompletedAtDesc(parentId, childId))
                .thenReturn(Optional.of(attempt));
        when(questionRepository.findAllByIsActiveTrueOrderByOrderIndexAsc()).thenReturn(activeQuestions);

        ParentQuizAttemptResponse response = parentQuizService.getQuizAttempt(parentId, childId);

        assertNotNull(response);
        assertEquals(3, response.getAnsweredCount());
        assertEquals(7, response.getTotalQuestions());
        assertEquals(3, response.getAnsweredQuestionIds().size());
        assertEquals(4, response.getUnansweredQuestionIds().size());
        assertFalse(response.getIsQuizCompleted());
    }
}
