// package com.sensei.backend.repository;

// import com.sensei.backend.entity.Processes;
// import org.springframework.data.jpa.repository.JpaRepository;
// import org.springframework.stereotype.Repository;

// @Repository
// public interface ProcessesRepository extends JpaRepository<Processes, String> {
// }
package com.sensei.backend.repository;

import com.sensei.backend.entity.InteractiveProcess;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface InteractiveProcessRepository extends JpaRepository<InteractiveProcess, UUID> {

    /**
     * Fetch all processes for an interactive activity
     * ordered by step_order (Sensei flow)
     */
    List<InteractiveProcess> findByInteractiveActivity_IdOrderByStepOrderAsc(
            UUID interactiveActivityId
    );

    /**
     * Used for access-control validation
     */
    boolean existsByIdAndInteractiveActivity_Id(
            UUID processId,
            UUID interactiveActivityId
    );
}
