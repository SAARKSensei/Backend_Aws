// package com.sensei.backend.controller;

// import com.sensei.backend.dto.ChildUserDTO;
// import com.sensei.backend.service.ChildUserService;
// import lombok.RequiredArgsConstructor;
// import lombok.extern.slf4j.Slf4j;
// import org.springframework.http.HttpStatus;
// import org.springframework.http.ResponseEntity;
// import org.springframework.web.bind.annotation.*;

// import javax.validation.Valid;
// import java.util.List;

// @RestController
// @RequestMapping("/api/child-users")
// @RequiredArgsConstructor
// @Slf4j
// public class ChildUserController {

//     private final ChildUserService childUserService;

//     @PostMapping
//     public ResponseEntity<ChildUserDTO> createChildUser(@Valid @RequestBody ChildUserDTO childUserDTO) {
//         ChildUserDTO createdChildUser = childUserService.createChildUser(childUserDTO);
//         return new ResponseEntity<>(createdChildUser, HttpStatus.CREATED);
//     }

//     @GetMapping("/{childId}")
//     public ResponseEntity<ChildUserDTO> getChildUserById(@PathVariable String childId) {
//         ChildUserDTO childUser = childUserService.getChildUserById(childId);
//         return new ResponseEntity<>(childUser, HttpStatus.OK);
//     }

//     @GetMapping
//     public ResponseEntity<List<ChildUserDTO>> getAllChildUsers() {
//         List<ChildUserDTO> childUsers = childUserService.getAllChildUsers();
//         log.info("Returning {} child users", childUsers.size());
//         return new ResponseEntity<>(childUsers, HttpStatus.OK);
//     }

//     @PutMapping("/{childId}")
//     public ResponseEntity<ChildUserDTO> updateChildUser(@PathVariable String childId, @Valid @RequestBody ChildUserDTO childUserDTO) {
//         ChildUserDTO updatedChildUser = childUserService.updateChildUser(childId, childUserDTO);
//         return new ResponseEntity<>(updatedChildUser, HttpStatus.OK);
//     }

//     @DeleteMapping("/{childId}")
//     public ResponseEntity<Void> deleteChildUser(@PathVariable String childId) {
//         childUserService.deleteChildUser(childId);
//         return new ResponseEntity<>(HttpStatus.NO_CONTENT);
//     }

//     @GetMapping("/filter")
//     public ResponseEntity<List<ChildUserDTO>> getChildUsersByPhoneNumber(@RequestParam long phoneNumber) {
//         log.info("Received request to filter ChildUsers by phoneNumber: {}", phoneNumber);
//         List<ChildUserDTO> childUsers = childUserService.findByPhoneNumber(phoneNumber);
//         return ResponseEntity.ok(childUsers);
//     }
// }
package com.sensei.backend.adapter.controller;

import com.sensei.backend.application.dto.ChildUserDTO;
import com.sensei.backend.domain.service.ChildUserService;

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
    public ResponseEntity<ChildUserDTO> create(@RequestBody ChildUserDTO dto) {
        return ResponseEntity.ok(childUserService.createChild(dto));
    }

    @GetMapping("/{childId}")
    public ResponseEntity<ChildUserDTO> getById(@PathVariable UUID childId) {
        return ResponseEntity.ok(childUserService.getByChildId(childId));
    }

    @PutMapping("/{childId}")
    public ResponseEntity<ChildUserDTO> update(@PathVariable UUID childId,
                                               @RequestBody ChildUserDTO dto) {
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
public ResponseEntity<List<ChildUserDTO>> getAllChildren() {
    return ResponseEntity.ok(childUserService.getAllChildren());
}

@GetMapping("/parent/{parentId}")
public ResponseEntity<List<ChildUserDTO>> getChildrenByParent(@PathVariable UUID parentId) {
    return ResponseEntity.ok(childUserService.getChildrenByParent(parentId));
}

}
