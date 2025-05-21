package com.sensei.backend.repository;

import com.sensei.backend.entity.InteractiveActivity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface InteractiveActivityRepository extends JpaRepository<InteractiveActivity, String> {
}
