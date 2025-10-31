package com.sensei.backend.repository;

import com.sensei.backend.entity.Question;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface QuestionRepository extends JpaRepository<Question, String> {
	@Query("SELECT DISTINCT q FROM Question q LEFT JOIN FETCH q.answers")
    List<Question> findAllWithAnswers();
}
