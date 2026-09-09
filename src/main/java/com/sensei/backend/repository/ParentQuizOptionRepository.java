package com.sensei.backend.repository;

import com.sensei.backend.entity.ParentQuizOption;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface ParentQuizOptionRepository extends JpaRepository<ParentQuizOption, UUID> {
    List<ParentQuizOption> findByQuestion_Id(UUID questionId);
}
