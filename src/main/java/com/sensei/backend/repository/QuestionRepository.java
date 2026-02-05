package com.sensei.backend.repository;

import com.sensei.backend.entity.Question;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface QuestionRepository extends JpaRepository<Question, UUID> {

    // ✅ API + Progress + Enforcement (single source of truth)
   // List<Question> findByDigitalActivity_IdAndIsActiveTrueOrderByOrderIndexAsc(UUID digitalActivityId);

    List<Question> findByDigitalActivity_IdOrderByOrderIndexAsc(UUID digitalActivityId);

    List<Question> findByDigitalActivity_IdAndIsActiveTrueOrderByOrderIndexAsc(UUID digitalActivityId);

}
