package com.sensei.backend.controller;

import com.sensei.backend.dto.SubjectDTO;
import com.sensei.backend.dto.ModuleDTO;
import com.sensei.backend.dto.SubModuleDTO;
import com.sensei.backend.dto.InteractiveActivityDTO;
import com.sensei.backend.dto.ProcessesDTO;
import com.sensei.backend.service.SubjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;
import java.util.Comparator;

@RestController
@RequestMapping("/api/subjects")
public class SubjectController {

    @Autowired
    private SubjectService subjectService;

    @PostMapping
    public ResponseEntity<SubjectDTO> createSubject(@Valid @RequestBody SubjectDTO subjectDTO) {
        SubjectDTO createdSubject = subjectService.createSubject(subjectDTO);
        return new ResponseEntity<>(createdSubject, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<SubjectDTO>> getAllSubjects() {
        List<SubjectDTO> subjects = subjectService.getAllSubjects();
        subjects.forEach(this::sortSubjectData);
        return new ResponseEntity<>(subjects, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<SubjectDTO> getSubjectById(@PathVariable String id) {
        Optional<SubjectDTO> subject = subjectService.getSubjectById(id);
        subject.ifPresent(this::sortSubjectData);
        return subject.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<SubjectDTO> updateSubject(@PathVariable String id, @Valid @RequestBody SubjectDTO subjectDTO) {
        SubjectDTO updatedSubject = subjectService.updateSubject(id, subjectDTO);
        return new ResponseEntity<>(updatedSubject, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSubject(@PathVariable String id) {
        subjectService.deleteSubject(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    private void sortSubjectData(SubjectDTO subject) {
        subject.getModules().sort(Comparator.comparingInt(ModuleDTO::getModuleNumber));
        subject.getModules().forEach(module -> {
            module.getSubModules().sort(Comparator.comparingInt(SubModuleDTO::getSubModuleNumber));
            module.getSubModules().forEach(subModule -> {
                subModule.getInteractiveActivities().sort(Comparator.comparingInt(InteractiveActivityDTO::getInteractiveActivityNumber));
                subModule.getInteractiveActivities().forEach(activity ->
                        activity.getProcesses().sort(Comparator.comparingInt(ProcessesDTO::getProcessNumber))
                );
            });
        });
    }
}
