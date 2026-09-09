package com.sensei.backend.controller;

import java.util.UUID;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.sensei.backend.dto.lifeskills.ChildLifeSkillReportResponse;
import com.sensei.backend.service.ChildLifeSkillService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/child")
@RequiredArgsConstructor
public class ChildLifeSkillController {

    private final ChildLifeSkillService childLifeSkillService;

    @GetMapping("/{childId}/lifeskills")
    public ResponseEntity<ChildLifeSkillReportResponse> getChildLifeSkills(@PathVariable UUID childId) {
        return ResponseEntity.ok(childLifeSkillService.getChildLifeSkills(childId));
    }
}
