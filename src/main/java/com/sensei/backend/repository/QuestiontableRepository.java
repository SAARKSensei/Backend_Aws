package com.sensei.backend.repository;

import com.sensei.backend.entity.Questiontable;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface QuestiontableRepository extends JpaRepository<Questiontable, String> {
    @Query("SELECT DISTINCT q FROM Questiontable q LEFT JOIN FETCH q.answers")
    List<Questiontable> findAllWithAnswers();
}

