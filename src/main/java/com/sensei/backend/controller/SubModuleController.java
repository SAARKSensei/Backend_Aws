package com.sensei.backend.controller;

import com.sensei.backend.dto.SubModuleDTO;
import com.sensei.backend.service.SubModuleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/submodules")
public class SubModuleController {

    @Autowired
    private SubModuleService subModuleService;

    @PostMapping
    public ResponseEntity<SubModuleDTO> createSubModule(@Valid @RequestBody SubModuleDTO subModuleDTO) {
        SubModuleDTO createdSubModule = subModuleService.createSubModule(subModuleDTO);
        return new ResponseEntity<>(createdSubModule, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<SubModuleDTO>> getAllSubModules() {
        List<SubModuleDTO> subModules = subModuleService.getAllSubModules();
        return new ResponseEntity<>(subModules, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<SubModuleDTO> getSubModuleById(@PathVariable String id) {
        Optional<SubModuleDTO> subModule = subModuleService.getSubModuleById(id);
        return subModule.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<SubModuleDTO> updateSubModule(@PathVariable String id, @Valid @RequestBody SubModuleDTO subModuleDTO) {
        SubModuleDTO updatedSubModule = subModuleService.updateSubModule(id, subModuleDTO);
        return new ResponseEntity<>(updatedSubModule, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSubModule(@PathVariable String id) {
        subModuleService.deleteSubModule(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
