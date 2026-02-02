package com.sensei.backend.domain.port.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.sensei.backend.adapter.persistence.jpa.entity.ChildQuestionAttempt;
import com.sensei.backend.adapter.persistence.jpa.entity.Question;

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