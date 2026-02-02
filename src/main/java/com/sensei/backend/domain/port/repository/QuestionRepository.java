package com.sensei.backend.domain.port.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.sensei.backend.adapter.persistence.jpa.entity.Question;

import java.util.List;
import java.util.UUID;

public interface QuestionRepository extends JpaRepository<Question, UUID> {

    // For Digital Activity → Questions
    List<Question> findByDigitalActivity_IdOrderByOrderIndexAsc(UUID digitalActivityId);

    // Used by ChildProgressService
    List<Question> findByDigitalActivityIdOrderByOrderIndexAsc(UUID digitalActivityId);
}