package com.sensei.backend.repository;

import com.sensei.backend.entity.QuestionOption;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface QuestionOptionRepository extends JpaRepository<QuestionOption, UUID> {

    //List<QuestionOption> findByQuestionIdOrderByOrderIndexAsc(UUID questionId);

    // List<QuestionOption> findByQuestion_IdAndIsActiveTrueOrderByOrderIndexAsc(UUID questionId);


    // List<QuestionOption> findByQuestionIdAndIsActiveTrueOrderByOrderIndexAsc(UUID questionId);

    List<QuestionOption> findByQuestion_IdAndIsActiveTrueOrderByOrderIndexAsc(UUID questionId);



}
