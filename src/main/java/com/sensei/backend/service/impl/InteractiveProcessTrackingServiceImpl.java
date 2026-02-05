// package com.sensei.backend.service.impl;

// import com.sensei.backend.dto.interactiveprocesstracking.InteractiveProcessTrackingRequestDTO;
// import com.sensei.backend.dto.interactiveprocesstracking.InteractiveProcessTrackingResponseDTO;
// import com.sensei.backend.entity.ChildUser;
// import com.sensei.backend.entity.InteractiveProcess;
// import com.sensei.backend.entity.InteractiveProcessTracking;
// import com.sensei.backend.enums.ProcessTrackingStatus;
// import com.sensei.backend.repository.ChildUserRepository;
// import com.sensei.backend.repository.InteractiveProcessRepository;
// import com.sensei.backend.repository.InteractiveProcessTrackingRepository;
// import com.sensei.backend.service.InteractiveProcessTrackingService;
// import lombok.RequiredArgsConstructor;
// import org.springframework.stereotype.Service;

// import java.time.LocalDateTime;
// import java.util.List;
// import java.util.UUID;
// import java.util.stream.Collectors;

// @Service
// @RequiredArgsConstructor
// public class InteractiveProcessTrackingServiceImpl
//         implements InteractiveProcessTrackingService {

//     private final InteractiveProcessTrackingRepository trackingRepository;
//     private final InteractiveProcessRepository processRepository;
//     private final ChildUserRepository childUserRepository;

//     // ----------------------------------------
//     // START PROCESS
//     // ----------------------------------------
//     @Override
//     public InteractiveProcessTrackingResponseDTO start(
//             InteractiveProcessTrackingRequestDTO dto
//     ) {
//         ChildUser child = childUserRepository.findById(dto.getChildId())
//                 .orElseThrow(() -> new RuntimeException("Child not found"));

//         InteractiveProcess process = processRepository.findById(dto.getInteractiveProcessId())
//                 .orElseThrow(() -> new RuntimeException("Process not found"));

//         InteractiveProcessTracking tracking =
//                 trackingRepository
//                         .findByChild_ChildIdAndInteractiveProcess_Id(
//                                 dto.getChildId(),
//                                 dto.getInteractiveProcessId()
//                         )
//                         .orElse(
//                                 InteractiveProcessTracking.builder()
//                                         .child(child)
//                                         .interactiveProcess(process)
//                                         .status(ProcessTrackingStatus.IN_PROGRESS)
//                                         .startedAt(LocalDateTime.now())
//                                         .build()
//                         );

//         if (tracking.getStatus() == ProcessTrackingStatus.COMPLETED) {
//             throw new RuntimeException("Process already completed");
//         }

//        tracking.setStatus(ProcessTrackingStatus.IN_PROGRESS);
//         tracking.setStartedAt(LocalDateTime.now());

//         return map(trackingRepository.save(tracking));
//     }

//     // ----------------------------------------
//     // COMPLETE PROCESS
//     // ----------------------------------------
//     @Override
//     public InteractiveProcessTrackingResponseDTO complete(
//             UUID childId,
//             UUID processId
//     ) {
//         InteractiveProcessTracking tracking =
//                 trackingRepository
//                         .findByChild_ChildIdAndInteractiveProcess_Id(childId, processId)
//                         .orElseThrow(() -> new RuntimeException("Tracking not found"));

//         if (tracking.getStatus() == ProcessTrackingStatus.COMPLETED) {
//             throw new RuntimeException("Process already completed");
//         }

//         tracking.setStatus(ProcessTrackingStatus.COMPLETED);
//         tracking.setCompletedAt(LocalDateTime.now());

//         return map(trackingRepository.save(tracking));
//     }

//     // ----------------------------------------
//     // GET SINGLE
//     // ----------------------------------------
//     @Override
//     public InteractiveProcessTrackingResponseDTO get(
//             UUID childId,
//             UUID processId
//     ) {
//         return map(
//                 trackingRepository
//                         .findByChild_ChildIdAndInteractiveProcess_Id(childId, processId)
//                         .orElseThrow(() -> new RuntimeException("Tracking not found"))
//         );
//     }

//     // ----------------------------------------
//     // GET ALL FOR CHILD
//     // ----------------------------------------
//     @Override
//     public List<InteractiveProcessTrackingResponseDTO> getByChild(UUID childId) {
//         return trackingRepository
//                 .findByChild_ChildId(childId)
//                 .stream()
//                 .map(this::map)
//                 .collect(Collectors.toList());
//     }

//     // ----------------------------------------
//     // DELETE (ADMIN / DEBUG only)
//     // ----------------------------------------
//     @Override
//     public void delete(UUID trackingId) {
//         trackingRepository.deleteById(trackingId);
//     }

//     // ----------------------------------------
//     // MAPPER
//     // ----------------------------------------
//     private InteractiveProcessTrackingResponseDTO map(
//             InteractiveProcessTracking t
//     ) {
//         return InteractiveProcessTrackingResponseDTO.builder()
//                 .id(t.getId())
//                 .childId(t.getChild().getChildId())
//                 .interactiveProcessId(t.getInteractiveProcess().getId())
//                 .status(t.getStatus())
//                 .startedAt(t.getStartedAt())
//                 .completedAt(t.getCompletedAt())
//                 .createdAt(t.getCreatedAt())
//                 .updatedAt(t.getUpdatedAt())
//                 .build();
//     }
// }
package com.sensei.backend.service.impl;

import com.sensei.backend.dto.interactiveprocesstracking.InteractiveProcessTrackingRequestDTO;
import com.sensei.backend.dto.interactiveprocesstracking.InteractiveProcessTrackingResponseDTO;
import com.sensei.backend.entity.ChildUser;
import com.sensei.backend.entity.InteractiveProcess;
import com.sensei.backend.entity.InteractiveProcessTracking;
import com.sensei.backend.enums.ProcessTrackingStatus;
import com.sensei.backend.repository.ChildUserRepository;
import com.sensei.backend.repository.InteractiveProcessRepository;
import com.sensei.backend.repository.InteractiveProcessTrackingRepository;
import com.sensei.backend.service.InteractiveProcessTrackingService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class InteractiveProcessTrackingServiceImpl
        implements InteractiveProcessTrackingService {

    private final InteractiveProcessTrackingRepository trackingRepository;
    private final InteractiveProcessRepository processRepository;
    private final ChildUserRepository childUserRepository;

    // ----------------------------------------
    // START / RESUME PROCESS
    // ----------------------------------------
    @Override
    public InteractiveProcessTrackingResponseDTO start(
            InteractiveProcessTrackingRequestDTO dto
    ) {
        ChildUser child = childUserRepository.findById(dto.getChildId())
                .orElseThrow(() -> new RuntimeException("Child not found"));

        InteractiveProcess process = processRepository.findById(dto.getInteractiveProcessId())
                .orElseThrow(() -> new RuntimeException("Process not found"));

        InteractiveProcessTracking tracking =
                trackingRepository
                        .findByChild_ChildIdAndInteractiveProcess_Id(
                                dto.getChildId(),
                                dto.getInteractiveProcessId()
                        )
                        .orElse(null);

        // ❌ HARD STOP: cannot restart a completed process
        if (tracking != null && tracking.getStatus() == ProcessTrackingStatus.COMPLETED) {
            throw new RuntimeException("Process already completed");
        }

        // ✅ Create new tracking if not exists
        if (tracking == null) {
            tracking = InteractiveProcessTracking.builder()
                    .child(child)
                    .interactiveProcess(process)
                    .status(ProcessTrackingStatus.IN_PROGRESS)
                    .startedAt(LocalDateTime.now())
                    .createdAt(LocalDateTime.now())
                    .updatedAt(LocalDateTime.now())
                    .build();
        } else {
            // ✅ Resume existing IN_PROGRESS process
            tracking.setStatus(ProcessTrackingStatus.IN_PROGRESS);
            tracking.setUpdatedAt(LocalDateTime.now());
        }

        return map(trackingRepository.save(tracking));
    }

    // ----------------------------------------
    // COMPLETE PROCESS
    // ----------------------------------------
    @Override
    public InteractiveProcessTrackingResponseDTO complete(
            UUID childId,
            UUID processId
    ) {
        InteractiveProcessTracking tracking =
                trackingRepository
                        .findByChild_ChildIdAndInteractiveProcess_Id(childId, processId)
                        .orElseThrow(() -> new RuntimeException("Tracking not found"));

        // ❌ Prevent double completion
        if (tracking.getStatus() == ProcessTrackingStatus.COMPLETED) {
            throw new RuntimeException("Process already completed");
        }

        tracking.setStatus(ProcessTrackingStatus.COMPLETED);
        tracking.setCompletedAt(LocalDateTime.now());
        tracking.setUpdatedAt(LocalDateTime.now());

        return map(trackingRepository.save(tracking));
    }

    // ----------------------------------------
    // GET SINGLE
    // ----------------------------------------
    @Override
    public InteractiveProcessTrackingResponseDTO get(
            UUID childId,
            UUID processId
    ) {
        return map(
                trackingRepository
                        .findByChild_ChildIdAndInteractiveProcess_Id(childId, processId)
                        .orElseThrow(() -> new RuntimeException("Tracking not found"))
        );
    }

    // ----------------------------------------
    // GET ALL FOR CHILD
    // ----------------------------------------
    @Override
    public List<InteractiveProcessTrackingResponseDTO> getByChild(UUID childId) {
        return trackingRepository
                .findByChild_ChildId(childId)
                .stream()
                .map(this::map)
                .collect(Collectors.toList());
    }

    // ----------------------------------------
    // DELETE (ADMIN / DEBUG ONLY)
    // ----------------------------------------
    @Override
    public void delete(UUID trackingId) {
        trackingRepository.deleteById(trackingId);
    }

    // ----------------------------------------
    // MAPPER
    // ----------------------------------------
    private InteractiveProcessTrackingResponseDTO map(
            InteractiveProcessTracking t
    ) {
        return InteractiveProcessTrackingResponseDTO.builder()
                .id(t.getId())
                .childId(t.getChild().getChildId())
                .interactiveProcessId(t.getInteractiveProcess().getId())
                .status(t.getStatus())
                .startedAt(t.getStartedAt())
                .completedAt(t.getCompletedAt())
                .createdAt(t.getCreatedAt())
                .updatedAt(t.getUpdatedAt())
                .build();
    }
}
