package com.sensei.backend.repository;

import com.sensei.backend.entity.ChildQuestionAttempt;
import com.sensei.backend.entity.Question;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface ChildQuestionAttemptRepository
        extends JpaRepository<ChildQuestionAttempt, UUID> {

    long countByChildIdAndQuestion(UUID childId, Question question);

    long countByChildIdAndQuestionAndIsCorrect(UUID childId, Question question, Boolean isCorrect);
}
// This gives you:

// attempt number

// first-attempt accuracy

// confusion patterns