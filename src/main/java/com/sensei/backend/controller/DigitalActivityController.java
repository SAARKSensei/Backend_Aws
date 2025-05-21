package com.sensei.backend.controller;

import com.sensei.backend.dto.DigitalActivityDTO;
import com.sensei.backend.service.DigitalActivityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/digital-activities")
public class DigitalActivityController {

    @Autowired
    private DigitalActivityService digitalActivityService;

    @PostMapping
    public ResponseEntity<DigitalActivityDTO> createDigitalActivity(@Valid @RequestBody DigitalActivityDTO digitalActivityDTO) {
        DigitalActivityDTO createdDigitalActivity = digitalActivityService.createDigitalActivity(digitalActivityDTO);
        return new ResponseEntity<>(createdDigitalActivity, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<DigitalActivityDTO>> getAllDigitalActivities() {
        List<DigitalActivityDTO> digitalActivities = digitalActivityService.getAllDigitalActivities();
        return new ResponseEntity<>(digitalActivities, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<DigitalActivityDTO> getDigitalActivityById(@PathVariable String id) {
        Optional<DigitalActivityDTO> digitalActivity = digitalActivityService.getDigitalActivityById(id);
        return digitalActivity.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<DigitalActivityDTO> updateDigitalActivity(@PathVariable String id, @Valid @RequestBody DigitalActivityDTO digitalActivityDTO) {
        DigitalActivityDTO updatedDigitalActivity = digitalActivityService.updateDigitalActivity(id, digitalActivityDTO);
        return new ResponseEntity<>(updatedDigitalActivity, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteDigitalActivity(@PathVariable String id) {
        digitalActivityService.deleteDigitalActivity(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
