package com.sensei.backend.domain.port.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.sensei.backend.adapter.persistence.jpa.entity.ChildInteractiveActivityProgress;
import com.sensei.backend.adapter.persistence.jpa.entity.InteractiveActivity;

import java.util.Optional;
import java.util.UUID;

public interface ChildInteractiveActivityProgressRepository
        extends JpaRepository<ChildInteractiveActivityProgress, UUID> {

    Optional<ChildInteractiveActivityProgress> 
        findByChildIdAndInteractiveActivity(UUID childId, InteractiveActivity activity);

    long countByChildIdAndStatus(UUID childId, String status);
}
// This allows:

// Detecting if an activity is already started

// Detecting if it is completed

// Measuring engagement