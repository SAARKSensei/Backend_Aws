package com.sensei.backend.controller;

import com.sensei.backend.dto.ChildUserDTO;
import com.sensei.backend.service.ChildUserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/child-users")
@RequiredArgsConstructor
@Slf4j
public class ChildUserController {

    private final ChildUserService childUserService;

    @PostMapping
    public ResponseEntity<ChildUserDTO> createChildUser(@Valid @RequestBody ChildUserDTO childUserDTO) {
        ChildUserDTO createdChildUser = childUserService.createChildUser(childUserDTO);
        return new ResponseEntity<>(createdChildUser, HttpStatus.CREATED);
    }

    @GetMapping("/{childId}")
    public ResponseEntity<ChildUserDTO> getChildUserById(@PathVariable String childId) {
        ChildUserDTO childUser = childUserService.getChildUserById(childId);
        return new ResponseEntity<>(childUser, HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<List<ChildUserDTO>> getAllChildUsers() {
        List<ChildUserDTO> childUsers = childUserService.getAllChildUsers();
        log.info("Returning {} child users", childUsers.size());
        return new ResponseEntity<>(childUsers, HttpStatus.OK);
    }

    @PutMapping("/{childId}")
    public ResponseEntity<ChildUserDTO> updateChildUser(@PathVariable String childId, @Valid @RequestBody ChildUserDTO childUserDTO) {
        ChildUserDTO updatedChildUser = childUserService.updateChildUser(childId, childUserDTO);
        return new ResponseEntity<>(updatedChildUser, HttpStatus.OK);
    }

    @DeleteMapping("/{childId}")
    public ResponseEntity<Void> deleteChildUser(@PathVariable String childId) {
        childUserService.deleteChildUser(childId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @GetMapping("/filter")
    public ResponseEntity<List<ChildUserDTO>> getChildUsersByPhoneNumber(@RequestParam long phoneNumber) {
        log.info("Received request to filter ChildUsers by phoneNumber: {}", phoneNumber);
        List<ChildUserDTO> childUsers = childUserService.findByPhoneNumber(phoneNumber);
        return ResponseEntity.ok(childUsers);
    }
}
