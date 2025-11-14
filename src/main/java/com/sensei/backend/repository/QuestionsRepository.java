package com.sensei.backend.repository;

import com.sensei.backend.entity.Questions;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface QuestionsRepository extends JpaRepository<Questions, String> {
	@Query("SELECT DISTINCT q FROM Questions q LEFT JOIN FETCH q.answers")
    List<Questions> findAllWithAnswers();
}