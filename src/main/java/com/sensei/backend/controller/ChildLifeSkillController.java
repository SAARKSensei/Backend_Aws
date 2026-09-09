package com.sensei.backend.controller;

import com.sensei.backend.dto.lifeskills.ChildLifeSkillReportResponse;
import com.sensei.backend.service.ChildLifeSkillService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

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
