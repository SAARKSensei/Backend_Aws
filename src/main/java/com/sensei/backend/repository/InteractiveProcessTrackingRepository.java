// package com.sensei.backend.repository;

// import com.sensei.backend.entity.InteractiveProcessTracking;
// import org.springframework.data.jpa.repository.JpaRepository;

// import java.util.List;
// import java.util.Optional;
// import java.util.UUID;

// public interface InteractiveProcessTrackingRepository
//         extends JpaRepository<InteractiveProcessTracking, UUID> {

//     Optional<InteractiveProcessTracking>
//     findByInteractiveProcess_IdAndChild_ChildId(
//             UUID processId,
//             UUID childId
//     );

//     Optional<InteractiveProcessTracking>
//     findByChild_ChildIdAndInteractiveProcess_Id(
//             UUID childId,
//             UUID interactiveProcessId
//     );

//     List<InteractiveProcessTracking>
//     findByChild_ChildId(UUID childId);

//     boolean existsByInteractiveProcess_IdAndChild_ChildIdAndStatus(
//             UUID processId,
//             UUID childId,
//             String status
//     );
// }
package com.sensei.backend.repository;

import com.sensei.backend.entity.InteractiveProcessTracking;
import com.sensei.backend.enums.ProcessTrackingStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface InteractiveProcessTrackingRepository
        extends JpaRepository<InteractiveProcessTracking, UUID> {

    Optional<InteractiveProcessTracking>
    findByChild_ChildIdAndInteractiveProcess_Id(
            UUID childId,
            UUID interactiveProcessId
    );

    List<InteractiveProcessTracking>
    findByChild_ChildId(UUID childId);

    boolean existsByChild_ChildIdAndInteractiveProcess_IdAndStatus(
            UUID childId,
            UUID interactiveProcessId,
            ProcessTrackingStatus status
    );
}
