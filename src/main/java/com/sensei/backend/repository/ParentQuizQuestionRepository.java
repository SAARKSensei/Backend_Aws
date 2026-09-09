package com.sensei.backend.repository;

import com.sensei.backend.entity.ParentQuizQuestion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface ParentQuizQuestionRepository extends JpaRepository<ParentQuizQuestion, UUID> {
    List<ParentQuizQuestion> findAllByIsActiveTrueOrderByOrderIndexAsc();
}
