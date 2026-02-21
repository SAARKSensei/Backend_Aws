// package com.sensei.backend.controller;

// import com.sensei.backend.dto.ProcessesDTO;
// import com.sensei.backend.service.ProcessesService;
// import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.http.HttpStatus;
// import org.springframework.http.ResponseEntity;
// import org.springframework.web.bind.annotation.*;

// import javax.validation.Valid;
// import java.util.List;
// import java.util.Optional;

// @RestController
// @RequestMapping("/api/processes")
// public class ProcessesController {

//     @Autowired
//     private ProcessesService processesService;

//     @PostMapping
//     public ResponseEntity<ProcessesDTO> createProcesses(@Valid @RequestBody ProcessesDTO processesDTO) {
//         ProcessesDTO createdProcesses = processesService.createProcesses(processesDTO);
//         return new ResponseEntity<>(createdProcesses, HttpStatus.CREATED);
//     }

//     @GetMapping
//     public ResponseEntity<List<ProcessesDTO>> getAllProcesses() {
//         List<ProcessesDTO> processes = processesService.getAllProcesses();
//         return new ResponseEntity<>(processes, HttpStatus.OK);
//     }

//     @GetMapping("/{id}")
//     public ResponseEntity<ProcessesDTO> getProcessesById(@PathVariable String id) {
//         Optional<ProcessesDTO> processes = processesService.getProcessesById(id);
//         return processes.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
//     }

//     @PutMapping("/{id}")
//     public ResponseEntity<ProcessesDTO> updateProcesses(@PathVariable String id, @Valid @RequestBody ProcessesDTO processesDTO) {
//         ProcessesDTO updatedProcesses = processesService.updateProcesses(id, processesDTO);
//         return new ResponseEntity<>(updatedProcesses, HttpStatus.OK);
//     }

//     @DeleteMapping("/{id}")
//     public ResponseEntity<Void> deleteProcesses(@PathVariable String id) {
//         processesService.deleteProcesses(id);
//         return new ResponseEntity<>(HttpStatus.NO_CONTENT);
//     }
// }
package com.sensei.backend.controller;

import com.sensei.backend.dto.interactiveprocess.InteractiveProcessRequestDTO;
import com.sensei.backend.dto.interactiveprocess.InteractiveProcessResponseDTO;
import com.sensei.backend.service.AccessControlService;
import com.sensei.backend.service.InteractiveProcessService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/interactive-processes")
@RequiredArgsConstructor
public class InteractiveProcessController {

    private final InteractiveProcessService processService;
    private final AccessControlService accessControlService;

    @PostMapping
    public ResponseEntity<InteractiveProcessResponseDTO> create(
            @RequestBody InteractiveProcessRequestDTO dto,
            @RequestParam(required = false) UUID childId) {

        if (childId != null) {
            UUID subjectId = accessControlService.getSubjectIdByActivity(dto.getInteractiveActivityId());
            accessControlService.validateSubjectAccess(childId, subjectId);
        }

        return ResponseEntity.ok(processService.create(dto));
    }

    @GetMapping("/by-activity/{activityId}")
    public ResponseEntity<List<InteractiveProcessResponseDTO>> getByActivity(
            @PathVariable UUID activityId,
            @RequestParam(required = false) UUID childId) {

        if (childId != null) {
            UUID subjectId = accessControlService.getSubjectIdByActivity(activityId);
            accessControlService.validateSubjectAccess(childId, subjectId);
        }

        return ResponseEntity.ok(processService.getByActivity(activityId));
    }

    @GetMapping("/{processId}")
    public ResponseEntity<InteractiveProcessResponseDTO> getById(
            @PathVariable UUID processId,
            @RequestParam(required = false) UUID childId) {

        if (childId != null) {
            UUID subjectId = accessControlService.getSubjectIdByProcess(processId);
            accessControlService.validateSubjectAccess(childId, subjectId);
        }

        return ResponseEntity.ok(processService.getById(processId));
    }

    @PutMapping("/{processId}")
    public ResponseEntity<InteractiveProcessResponseDTO> update(
            @PathVariable UUID processId,
            @RequestBody InteractiveProcessRequestDTO dto,
            @RequestParam(required = false) UUID childId) {

        if (childId != null) {
            UUID subjectId = accessControlService.getSubjectIdByProcess(processId);
            accessControlService.validateSubjectAccess(childId, subjectId);
        }

        return ResponseEntity.ok(processService.update(processId, dto));
    }

    @DeleteMapping("/{processId}")
    public ResponseEntity<Void> delete(
            @PathVariable UUID processId,
            @RequestParam(required = false) UUID childId) {

        if (childId != null) {
            UUID subjectId = accessControlService.getSubjectIdByProcess(processId);
            accessControlService.validateSubjectAccess(childId, subjectId);
        }

        processService.delete(processId);
        return ResponseEntity.noContent().build();
    }
}


