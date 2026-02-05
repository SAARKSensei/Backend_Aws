package com.sensei.backend.repository;

import com.sensei.backend.entity.InteractiveProcessSubStep;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface InteractiveProcessSubStepRepository
        extends JpaRepository<InteractiveProcessSubStep, UUID> {

    /**
     * Fetch all substeps for a given process
     * ordered by step_order
     */
    List<InteractiveProcessSubStep> findByProcess_IdOrderByStepOrderAsc(
            UUID processId
    );

    /**
     * Count substeps (useful for validation & analytics later)
     */
    long countByProcess_Id(UUID processId);
}
