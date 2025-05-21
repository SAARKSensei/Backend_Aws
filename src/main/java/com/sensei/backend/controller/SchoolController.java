package com.sensei.backend.controller;

import com.sensei.backend.dto.SchoolDTO;
import com.sensei.backend.service.SchoolService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/schools")
@RequiredArgsConstructor
public class SchoolController {

    private final SchoolService schoolService;

    @PostMapping
    public ResponseEntity<SchoolDTO> createSchool(@RequestBody SchoolDTO schoolDTO) {
        return ResponseEntity.ok(schoolService.createSchool(schoolDTO));
    }

    @GetMapping("/{id}")
    public ResponseEntity<SchoolDTO> getSchoolById(@PathVariable String id) {
        return ResponseEntity.ok(schoolService.getSchoolById(id));
    }

    @GetMapping
    public ResponseEntity<List<SchoolDTO>> getAllSchools() {
        return ResponseEntity.ok(schoolService.getAllSchools());
    }

    @PutMapping("/{id}")
    public ResponseEntity<SchoolDTO> updateSchool(@PathVariable String id, @RequestBody SchoolDTO schoolDTO) {
        return ResponseEntity.ok(schoolService.updateSchool(id, schoolDTO));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSchool(@PathVariable String id) {
        schoolService.deleteSchool(id);
        return ResponseEntity.noContent().build();
    }
}
