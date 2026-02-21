// package com.sensei.backend.controller;

// import com.sensei.backend.dto.DigitalActivityDTO;
// import com.sensei.backend.service.DigitalActivityService;
// import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.http.HttpStatus;
// import org.springframework.http.ResponseEntity;
// import org.springframework.web.bind.annotation.*;

// import javax.validation.Valid;
// import java.util.List;
// import java.util.Optional;

// @RestController
// @RequestMapping("/api/digital-activities")
// public class DigitalActivityController {

//     @Autowired
//     private DigitalActivityService digitalActivityService;

//     @PostMapping
//     public ResponseEntity<DigitalActivityDTO> createDigitalActivity(@Valid @RequestBody DigitalActivityDTO digitalActivityDTO) {
//         DigitalActivityDTO createdDigitalActivity = digitalActivityService.createDigitalActivity(digitalActivityDTO);
//         return new ResponseEntity<>(createdDigitalActivity, HttpStatus.CREATED);
//     }

//     @GetMapping
//     public ResponseEntity<List<DigitalActivityDTO>> getAllDigitalActivities() {
//         List<DigitalActivityDTO> digitalActivities = digitalActivityService.getAllDigitalActivities();
//         return new ResponseEntity<>(digitalActivities, HttpStatus.OK);
//     }

//     @GetMapping("/{id}")
//     public ResponseEntity<?> getDigitalActivityById(@PathVariable String id) {
//         try {
//             Optional<DigitalActivityDTO> digitalActivity = digitalActivityService.getDigitalActivityById(id);
//             if (digitalActivity.isPresent()) {
//                 return ResponseEntity.ok(digitalActivity.get());
//             } else {
//                 return ResponseEntity.status(HttpStatus.NOT_FOUND)
//                         .body("Digital Activity not found with id: " + id);
//             }
//         } catch (Exception e) {
//             return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
//                     .body("Error fetching Digital Activity: " + e.getMessage());
//         }
//     }

//     @PutMapping("/{id}")
//     public ResponseEntity<?> updateDigitalActivity(@PathVariable String id, @Valid @RequestBody DigitalActivityDTO digitalActivityDTO) {
//         try {
//             DigitalActivityDTO updated = digitalActivityService.updateDigitalActivity(id, digitalActivityDTO);
//             return ResponseEntity.ok(updated);
//         } catch (RuntimeException e) {
//             return ResponseEntity.status(HttpStatus.NOT_FOUND)
//                     .body("Update failed: " + e.getMessage());
//         } catch (Exception e) {
//             return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
//                     .body("Error updating Digital Activity: " + e.getMessage());
//         }
//     }

//     @DeleteMapping("/{id}")
//     public ResponseEntity<?> deleteDigitalActivity(@PathVariable String id) {
//         try {
//             digitalActivityService.deleteDigitalActivity(id);
//             return ResponseEntity.noContent().build();
//         } catch (RuntimeException e) {
//             return ResponseEntity.status(HttpStatus.NOT_FOUND)
//                     .body("Delete failed: " + e.getMessage());
//         } catch (Exception e) {
//             return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
//                     .body("Error deleting Digital Activity: " + e.getMessage());
//         }
//     }

// }
package com.sensei.backend.controller;

import com.sensei.backend.dto.digitalactivity.*;
import com.sensei.backend.service.AccessControlService;
import com.sensei.backend.service.DigitalActivityService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/digital-activities")
@RequiredArgsConstructor
public class DigitalActivityController {

    private final DigitalActivityService service;
    private final AccessControlService accessControlService;

    @PostMapping
    public DigitalActivityResponseDTO create(@RequestBody DigitalActivityRequestDTO dto) {
        return service.create(dto);
    }

    @PutMapping("/{id}")
    public DigitalActivityResponseDTO update(
            @PathVariable UUID id,
            @RequestBody DigitalActivityRequestDTO dto) {
        return service.update(id, dto);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable UUID id) {
        service.delete(id);
    }

    @GetMapping("/{id}")
    public DigitalActivityResponseDTO getById(@PathVariable UUID id) {
        return service.getById(id);
    }

    @GetMapping("/submodule/{subModuleId}")
    public List<DigitalActivityResponseDTO> getBySubModule(
            @PathVariable UUID subModuleId,
            @RequestParam(required = false) UUID childId) {

        if (childId != null) {
            UUID subjectId = service.getSubjectIdBySubModule(subModuleId);
            accessControlService.validateSubjectAccess(childId, subjectId);
        }

        return service.getBySubModule(subModuleId);
    }
}

