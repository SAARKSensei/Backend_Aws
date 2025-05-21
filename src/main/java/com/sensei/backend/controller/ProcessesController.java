package com.sensei.backend.controller;

import com.sensei.backend.dto.ProcessesDTO;
import com.sensei.backend.service.ProcessesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/processes")
public class ProcessesController {

    @Autowired
    private ProcessesService processesService;

    @PostMapping
    public ResponseEntity<ProcessesDTO> createProcesses(@Valid @RequestBody ProcessesDTO processesDTO) {
        ProcessesDTO createdProcesses = processesService.createProcesses(processesDTO);
        return new ResponseEntity<>(createdProcesses, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<ProcessesDTO>> getAllProcesses() {
        List<ProcessesDTO> processes = processesService.getAllProcesses();
        return new ResponseEntity<>(processes, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProcessesDTO> getProcessesById(@PathVariable String id) {
        Optional<ProcessesDTO> processes = processesService.getProcessesById(id);
        return processes.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProcessesDTO> updateProcesses(@PathVariable String id, @Valid @RequestBody ProcessesDTO processesDTO) {
        ProcessesDTO updatedProcesses = processesService.updateProcesses(id, processesDTO);
        return new ResponseEntity<>(updatedProcesses, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProcesses(@PathVariable String id) {
        processesService.deleteProcesses(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
