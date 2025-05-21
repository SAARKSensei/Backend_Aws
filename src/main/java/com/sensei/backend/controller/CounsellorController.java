package com.sensei.backend.controller;

import com.sensei.backend.dto.CounsellorDTO;
import com.sensei.backend.service.CounsellorService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/counsellors")
@RequiredArgsConstructor
public class CounsellorController {

    private final CounsellorService counsellorService;

    @PostMapping
    public ResponseEntity<CounsellorDTO> createCounsellor(@RequestBody CounsellorDTO counsellorDTO) {
        return ResponseEntity.ok(counsellorService.createCounsellor(counsellorDTO));
    }

    @GetMapping("/{id}")
    public ResponseEntity<CounsellorDTO> getCounsellorById(@PathVariable String id) {
        return ResponseEntity.ok(counsellorService.getCounsellorById(id));
    }

    @GetMapping
    public ResponseEntity<List<CounsellorDTO>> getAllCounsellors() {
        return ResponseEntity.ok(counsellorService.getAllCounsellors());
    }

    @PutMapping("/{id}")
    public ResponseEntity<CounsellorDTO> updateCounsellor(@PathVariable String id, @RequestBody CounsellorDTO counsellorDTO) {
        return ResponseEntity.ok(counsellorService.updateCounsellor(id, counsellorDTO));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCounsellor(@PathVariable String id) {
        counsellorService.deleteCounsellor(id);
        return ResponseEntity.noContent().build();
    }
}
