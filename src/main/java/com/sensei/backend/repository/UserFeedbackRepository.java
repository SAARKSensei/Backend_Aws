package com.sensei.backend.repository;

import com.sensei.backend.entity.UserFeedback;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserFeedbackRepository extends JpaRepository<UserFeedback, Long> {
}