package com.sensei.backend.controller;

import com.sensei.backend.dto.report.MasterReportCardResponse;
import com.sensei.backend.service.ReportCardService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/report-card")
@RequiredArgsConstructor
public class ReportCardController {

    private final ReportCardService reportCardService;

    @GetMapping("/child/{childId}")
    public ResponseEntity<MasterReportCardResponse> getMasterReportCard(
            @PathVariable UUID childId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        return ResponseEntity.ok(reportCardService.generateMasterReportCard(childId, page, size));
    }
}
