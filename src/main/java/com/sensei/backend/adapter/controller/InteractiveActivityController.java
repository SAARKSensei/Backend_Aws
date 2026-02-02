// package com.sensei.backend.controller;

// import com.sensei.backend.dto.InteractiveActivityDTO;
// import com.sensei.backend.dto.ProcessesDTO;
// import com.sensei.backend.service.InteractiveActivityService;
// import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.http.HttpStatus;
// import org.springframework.http.ResponseEntity;
// import org.springframework.web.bind.annotation.*;

// import javax.validation.Valid;
// import java.util.List;
// import java.util.Optional;
// import java.util.Comparator;
// import java.util.stream.Collectors;


// @RestController
// @RequestMapping("/api/interactive-activities")
// public class InteractiveActivityController {

//     @Autowired
//     private InteractiveActivityService interactiveActivityService;

//     @PostMapping
//     public ResponseEntity<InteractiveActivityDTO> createInteractiveActivity(@Valid @RequestBody InteractiveActivityDTO interactiveActivityDTO) {
//         InteractiveActivityDTO createdInteractiveActivity = interactiveActivityService.createInteractiveActivity(interactiveActivityDTO);
//         return new ResponseEntity<>(createdInteractiveActivity, HttpStatus.CREATED);
//     }

//     @GetMapping
//     public ResponseEntity<List<InteractiveActivityDTO>> getAllInteractiveActivities() {
//         List<InteractiveActivityDTO> interactiveActivities = interactiveActivityService.getAllInteractiveActivities()
//                 .stream()
//                 .sorted(Comparator.comparingInt(InteractiveActivityDTO::getInteractiveActivityNumber)) // Sorting by interactiveActivityNumber
//                 .collect(Collectors.toList());
//         // Sort processes within each InteractiveActivityDTO
//         interactiveActivities.forEach(activity -> {
//             List<ProcessesDTO> sortedProcesses = activity.getProcesses()
//                     .stream()
//                     .sorted(Comparator.comparingInt(ProcessesDTO::getProcessNumber)) // Ensure sorting by processNumber
//                     .collect(Collectors.toList());
//             activity.setProcesses(sortedProcesses);
//         });
//         return new ResponseEntity<>(interactiveActivities, HttpStatus.OK);
//     }

//     @GetMapping("/{id}")
//     public ResponseEntity<InteractiveActivityDTO> getInteractiveActivityById(@PathVariable String id) {
//         Optional<InteractiveActivityDTO> interactiveActivity = interactiveActivityService.getInteractiveActivityById(id);
//         return interactiveActivity.map(activity -> {
//             List<ProcessesDTO> sortedProcesses = activity.getProcesses().stream()
//                     .sorted(Comparator.comparingInt(ProcessesDTO::getProcessNumber)) // Ensure sorting by processNumber
//                     .collect(Collectors.toList());

//             activity.setProcesses(sortedProcesses);
//             return ResponseEntity.ok(activity);
//         })
//                 .orElseGet(() -> ResponseEntity.notFound().build());
//     }

//     @PutMapping("/{id}")
//     public ResponseEntity<InteractiveActivityDTO> updateInteractiveActivity(@PathVariable String id, @Valid @RequestBody InteractiveActivityDTO interactiveActivityDTO) {
//         InteractiveActivityDTO updatedInteractiveActivity = interactiveActivityService.updateInteractiveActivity(id, interactiveActivityDTO);
//         return new ResponseEntity<>(updatedInteractiveActivity, HttpStatus.OK);
//     }

//     @DeleteMapping("/{id}")
//     public ResponseEntity<Void> deleteInteractiveActivity(@PathVariable String id) {
//         interactiveActivityService.deleteInteractiveActivity(id);
//         return new ResponseEntity<>(HttpStatus.NO_CONTENT);
//     }
// }
package com.sensei.backend.adapter.controller;

import com.sensei.backend.application.dto.interactiveactivity.InteractiveActivityRequestDTO;
import com.sensei.backend.application.dto.interactiveactivity.InteractiveActivityResponseDTO;
import com.sensei.backend.domain.service.InteractiveActivityService;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/interactive-activities")
@RequiredArgsConstructor
public class InteractiveActivityController {

    private final InteractiveActivityService service;

    @PostMapping
    public ResponseEntity<InteractiveActivityResponseDTO> create(@RequestBody InteractiveActivityRequestDTO dto) {
        return ResponseEntity.ok(service.create(dto));
    }

    @GetMapping("/by-submodule/{subModuleId}")
    public ResponseEntity<List<InteractiveActivityResponseDTO>> getBySubModule(@PathVariable UUID subModuleId) {
        return ResponseEntity.ok(service.getBySubModule(subModuleId));
    }

    @GetMapping("/{id}")
    public ResponseEntity<InteractiveActivityResponseDTO> getById(@PathVariable UUID id) {
        return ResponseEntity.ok(service.getById(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<InteractiveActivityResponseDTO> update(
            @PathVariable UUID id,
            @RequestBody InteractiveActivityRequestDTO dto) {
        return ResponseEntity.ok(service.update(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}
