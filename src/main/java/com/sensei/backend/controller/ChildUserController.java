package com.sensei.backend.controller;
import jakarta.validation.Valid;

import com.sensei.backend.dto.ChildUserDTO;
import com.sensei.backend.dto.ChildUserUpdateDTO;
import com.sensei.backend.service.ChildUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;


@RestController
@RequestMapping("/api/children")
@RequiredArgsConstructor
public class ChildUserController {

    private final ChildUserService childUserService;

    @PostMapping
    public ResponseEntity<ChildUserDTO> create(@Valid @RequestBody ChildUserDTO dto) {
        return ResponseEntity.ok(childUserService.createChildUser(dto));
    }

    @GetMapping("/{childId}")
    public ResponseEntity<ChildUserDTO> getById(@PathVariable UUID childId) {
        return ResponseEntity.ok(childUserService.getByChildId(childId));
    }

    @PutMapping("/{childId}")
    public ResponseEntity<ChildUserDTO> update(@PathVariable UUID childId,
                                               @Valid @RequestBody ChildUserUpdateDTO dto) {
        return ResponseEntity.ok(childUserService.update(childId, dto));
    }

    @PatchMapping("/{childId}")
    public ResponseEntity<ChildUserDTO> patch(@PathVariable UUID childId,
                                               @Valid @RequestBody ChildUserUpdateDTO dto) {
        return ResponseEntity.ok(childUserService.update(childId, dto));
    }

    @DeleteMapping("/{childId}")
    public ResponseEntity<Void> delete(@PathVariable UUID childId) {
        childUserService.delete(childId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/phone/{phone}")
    public ResponseEntity<List<ChildUserDTO>> getByPhone(@PathVariable String phone) {
        return ResponseEntity.ok(childUserService.findByPhone(phone));
    }

    @GetMapping
public ResponseEntity<org.springframework.data.domain.Page<ChildUserDTO>> getAllChildren(org.springframework.data.domain.Pageable pageable) {
    return ResponseEntity.ok(childUserService.getAllChildren(pageable));
}

@GetMapping("/parent/{parentId}")
public ResponseEntity<List<ChildUserDTO>> getChildrenByParent(@PathVariable UUID parentId) {
    return ResponseEntity.ok(childUserService.getChildrenByParent(parentId));
}

}
