// // package com.sensei.backend.controller;

// // import com.sensei.backend.dto.ModuleDTO;
// // import com.sensei.backend.service.ModuleService;
// // import org.springframework.beans.factory.annotation.Autowired;
// // import org.springframework.http.HttpStatus;
// // import org.springframework.http.ResponseEntity;
// // import org.springframework.web.bind.annotation.*;

// // import javax.validation.Valid;
// // import java.util.List;
// // import java.util.Optional;

// // @RestController
// // @RequestMapping("/api/modules")
// // public class ModuleController {

// //     @Autowired
// //     private ModuleService moduleService;

// //     @PostMapping
// // //    public ResponseEntity<ModuleDTO> createModule(@Valid @RequestBody ModuleDTO moduleDTO, @RequestParam String subjectId) {
// //     public ResponseEntity<ModuleDTO> createModule(@Valid @RequestBody ModuleDTO moduleDTO) {
// // //        ModuleDTO createdModule = moduleService.createModule(moduleDTO, subjectId);
// //         ModuleDTO createdModule = moduleService.createModule(moduleDTO);
// //         return new ResponseEntity<>(createdModule, HttpStatus.CREATED);
// //     }

// //     @GetMapping
// //     public ResponseEntity<List<ModuleDTO>> getAllModules() {
// //         List<ModuleDTO> modules = moduleService.getAllModules();
// //         return new ResponseEntity<>(modules, HttpStatus.OK);
// //     }

// //     @GetMapping("/{id}")
// //     public ResponseEntity<ModuleDTO> getModuleById(@PathVariable String id) {
// //         Optional<ModuleDTO> module = moduleService.getModuleById(id);
// //         return module.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
// //     }

// //     @PutMapping("/{id}")
// //     public ResponseEntity<ModuleDTO> updateModule(@PathVariable String id, @Valid @RequestBody ModuleDTO moduleDTO) {
// //         ModuleDTO updatedModule = moduleService.updateModule(id, moduleDTO);
// //         return new ResponseEntity<>(updatedModule, HttpStatus.OK);
// //     }

// //     @DeleteMapping("/{id}")
// //     public ResponseEntity<Void> deleteModule(@PathVariable String id) {
// //         moduleService.deleteModule(id);
// //         return new ResponseEntity<>(HttpStatus.NO_CONTENT);
// //     }
// // }
// package com.sensei.backend.controller;

// import com.sensei.backend.dto.module.ModuleRequestDTO;
// import com.sensei.backend.dto.module.ModuleResponseDTO;
// import com.sensei.backend.service.ModuleService;
// import lombok.RequiredArgsConstructor;
// import org.springframework.http.ResponseEntity;
// import org.springframework.web.bind.annotation.*;

// import java.util.List;
// import java.util.UUID;

// @RestController
// @RequestMapping("/api/modules")
// @RequiredArgsConstructor
// public class ModuleController {

//     private final ModuleService moduleService;

//     @PostMapping
//     public ResponseEntity<ModuleResponseDTO> create(@RequestBody ModuleRequestDTO dto) {
//         return ResponseEntity.ok(moduleService.createModule(dto));
//     }

//     @GetMapping("/by-subject/{subjectId}")
//     public ResponseEntity<List<ModuleResponseDTO>> getBySubject(@PathVariable UUID subjectId) {
//         return ResponseEntity.ok(moduleService.getModulesBySubject(subjectId));
//     }
// }
package com.sensei.backend.adapter.controller;

import com.sensei.backend.application.dto.module.*;
import com.sensei.backend.domain.service.ModuleService;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/modules")
@RequiredArgsConstructor
public class ModuleController {

    private final ModuleService moduleService;

    @PostMapping
    public ModuleResponseDTO create(@RequestBody ModuleRequestDTO dto) {
        return moduleService.createModule(dto);
    }

    @GetMapping("/by-subject/{subjectId}")
    public List<ModuleResponseDTO> getBySubject(@PathVariable UUID subjectId) {
        return moduleService.getModulesBySubject(subjectId);
    }

    @GetMapping("/{id}")
    public ModuleResponseDTO getById(@PathVariable UUID id) {
        return moduleService.getById(id);
    }

    @PutMapping("/{id}")
    public ModuleResponseDTO update(@PathVariable UUID id, @RequestBody ModuleRequestDTO dto) {
        return moduleService.update(id, dto);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable UUID id) {
        moduleService.delete(id);
    }

    @GetMapping
public List<ModuleResponseDTO> getAll() {
    return moduleService.getAllModules();
}

}
