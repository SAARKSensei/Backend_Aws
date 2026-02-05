// package com.sensei.backend.controller;

// import com.sensei.backend.dto.SubjectDTO;
// import com.sensei.backend.dto.ModuleDTO;
// import com.sensei.backend.dto.SubModuleDTO;
// import com.sensei.backend.dto.InteractiveActivityDTO;
// import com.sensei.backend.dto.ProcessesDTO;
// import com.sensei.backend.service.SubjectService;
// import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.http.HttpStatus;
// import org.springframework.http.ResponseEntity;
// import org.springframework.web.bind.annotation.*;

// import javax.validation.Valid;
// import java.util.List;
// import java.util.Optional;
// import java.util.Comparator;

// @RestController
// @RequestMapping("/api/subjects")
// public class SubjectController {

//     @Autowired
//     private SubjectService subjectService;

//     @PostMapping
//     public ResponseEntity<SubjectDTO> createSubject(@Valid @RequestBody SubjectDTO subjectDTO) {
//         SubjectDTO createdSubject = subjectService.createSubject(subjectDTO);
//         return new ResponseEntity<>(createdSubject, HttpStatus.CREATED);
//     }

//     @GetMapping
//     public ResponseEntity<List<SubjectDTO>> getAllSubjects() {
//         List<SubjectDTO> subjects = subjectService.getAllSubjects();
//         subjects.forEach(this::sortSubjectData);
//         return new ResponseEntity<>(subjects, HttpStatus.OK);
//     }

//     @GetMapping("/{id}")
//     public ResponseEntity<SubjectDTO> getSubjectById(@PathVariable String id) {
//         Optional<SubjectDTO> subject = subjectService.getSubjectById(id);
//         subject.ifPresent(this::sortSubjectData);
//         return subject.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
//     }

//     @PutMapping("/{id}")
//     public ResponseEntity<SubjectDTO> updateSubject(@PathVariable String id, @Valid @RequestBody SubjectDTO subjectDTO) {
//         SubjectDTO updatedSubject = subjectService.updateSubject(id, subjectDTO);
//         return new ResponseEntity<>(updatedSubject, HttpStatus.OK);
//     }

//     @DeleteMapping("/{id}")
//     public ResponseEntity<Void> deleteSubject(@PathVariable String id) {
//         subjectService.deleteSubject(id);
//         return new ResponseEntity<>(HttpStatus.NO_CONTENT);
//     }

//     private void sortSubjectData(SubjectDTO subject) {
//         subject.getModules().sort(Comparator.comparingInt(ModuleDTO::getModuleNumber));
//         subject.getModules().forEach(module -> {
//             module.getSubModules().sort(Comparator.comparingInt(SubModuleDTO::getSubModuleNumber));
//             module.getSubModules().forEach(subModule -> {
//                 subModule.getInteractiveActivities().sort(Comparator.comparingInt(InteractiveActivityDTO::getInteractiveActivityNumber));
//                 subModule.getInteractiveActivities().forEach(activity ->
//                         activity.getProcesses().sort(Comparator.comparingInt(ProcessesDTO::getProcessNumber))
//                 );
//             });
//         });
//     }
// }
// package com.sensei.backend.controller;

// import com.sensei.backend.dto.subject.SubjectRequestDTO;
// import com.sensei.backend.dto.subject.SubjectResponseDTO;

// import com.sensei.backend.service.SubjectService;
// import lombok.RequiredArgsConstructor;
// import org.springframework.http.ResponseEntity;
// import org.springframework.web.bind.annotation.*;

// import java.util.List;
// import java.util.UUID;

// @RestController
// @RequestMapping("/api/subjects")
// @RequiredArgsConstructor
// public class SubjectController {

//     private final SubjectService subjectService;

//     @PostMapping
//     public ResponseEntity<SubjectResponseDTO> createSubject(@RequestBody SubjectRequestDTO dto) {
//         return ResponseEntity.ok(subjectService.createSubject(dto));
//     }

//     @GetMapping
//     public ResponseEntity<List<SubjectResponseDTO>> getAllSubjects() {
//         return ResponseEntity.ok(subjectService.getAllSubjects());
//     }

//     @GetMapping("/{id}")
//     public ResponseEntity<SubjectResponseDTO> getSubject(@PathVariable UUID id) {
//         return ResponseEntity.ok(subjectService.getSubjectById(id));
//     }

//     @DeleteMapping("/{id}")
//     public ResponseEntity<Void> deleteSubject(@PathVariable UUID id) {
//         subjectService.deleteSubject(id);
//         return ResponseEntity.noContent().build();
//     }
// }
package com.sensei.backend.controller;

import com.sensei.backend.dto.subject.SubjectRequestDTO;
import com.sensei.backend.dto.subject.SubjectResponseDTO;
import com.sensei.backend.service.AccessControlService;
import com.sensei.backend.service.SubjectService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/subjects")
@RequiredArgsConstructor
public class SubjectController {

    private final SubjectService subjectService;
    private final AccessControlService accessControlService;

    @PostMapping
    public SubjectResponseDTO create(@RequestBody SubjectRequestDTO dto) {
        return subjectService.create(dto);
    }

    @GetMapping
    public List<SubjectResponseDTO> getAll() {
        return subjectService.getAll();
    }

    @GetMapping("/{id}")
    public SubjectResponseDTO getById(
            @PathVariable UUID id,
            @RequestParam UUID childId) {

        accessControlService.validateSubjectAccess(childId, id);
        return subjectService.getById(id);
    }

    @PutMapping("/{id}")
    public SubjectResponseDTO update(@PathVariable UUID id, @RequestBody SubjectRequestDTO dto) {
        return subjectService.update(id, dto);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable UUID id) {
        subjectService.delete(id);
    }
}


