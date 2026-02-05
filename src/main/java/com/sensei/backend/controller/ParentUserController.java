// package com.sensei.backend.controller;

// import com.sensei.backend.dto.ParentUserDTO;
// import com.sensei.backend.service.ParentUserService;
// import com.sensei.backend.entity.ParentUser;
// import com.sensei.backend.entity.PricingPlan;
// import lombok.RequiredArgsConstructor;
// import lombok.extern.slf4j.Slf4j;
// import org.springframework.http.HttpStatus;
// import org.springframework.http.ResponseEntity;
// import org.springframework.web.bind.annotation.*;

// import javax.validation.Valid;
// import java.util.List;
// import java.util.Optional;
// import java.util.Map;
// import java.util.Collections;

// @RestController
// @RequestMapping("/api/parent-users")
// @RequiredArgsConstructor
// @Slf4j
// public class ParentUserController {

//     private final ParentUserService parentUserService;

//     @PostMapping
//     public ResponseEntity<ParentUserDTO> createParentUser(@Valid @RequestBody ParentUserDTO parentUserDTO) {
//         ParentUserDTO createdParentUser = parentUserService.createParentUser(parentUserDTO);
//         return new ResponseEntity<>(createdParentUser, HttpStatus.CREATED);
//     }

//     @GetMapping("/{parentId}")
//     public ResponseEntity<ParentUserDTO> getParentUserById(@PathVariable String parentId) {
//         ParentUserDTO parentUser = parentUserService.getParentUserById(parentId);
//         return new ResponseEntity<>(parentUser, HttpStatus.OK);
//     }

//     @GetMapping
//     public ResponseEntity<List<ParentUserDTO>> getAllParentUsers() {
//         List<ParentUserDTO> parentUsers = parentUserService.getAllParentUsers();
//         return new ResponseEntity<>(parentUsers, HttpStatus.OK);
//     }

//     @PutMapping("/{parentId}")
//     public ResponseEntity<ParentUserDTO> updateParentUser(@PathVariable String parentId, @Valid @RequestBody ParentUserDTO parentUserDTO) {
//         ParentUserDTO updatedParentUser = parentUserService.updateParentUser(parentId, parentUserDTO);   // Added ById by Vaishnav Kale
//         return new ResponseEntity<>(updatedParentUser, HttpStatus.OK);
//     }

//     @DeleteMapping("/{parentId}")
//     public ResponseEntity<Void> deleteParentUser(@PathVariable String parentId) {
//         parentUserService.deleteParentUser(parentId);
//         return new ResponseEntity<>(HttpStatus.NO_CONTENT);
//     }

//     @GetMapping("/filter")
//     public ResponseEntity<ParentUserDTO> getParentUserByUserName(@RequestParam String userName) {
//         ParentUserDTO parentUser = parentUserService.getParentUserByUserName(userName);
//         return new ResponseEntity<>(parentUser, HttpStatus.OK);
//     }

//     // Get Parent user from phone Number API
//     @GetMapping("/phone")
//     public ResponseEntity<ParentUserDTO> getParentUserByPhoneNumber(@RequestParam String phone) {
//         log.info("Fetching parent details by phone number: {}", phone);
//         ParentUserDTO parentUser = parentUserService.getParentUserByPhoneNumber(phone);     // Added getParentUserWithChildren() by Vaishnav Kale
//         return ResponseEntity.ok(parentUser);
//     }

//     //Get Parent User By Email ----
//     @GetMapping("/email")
//     public ResponseEntity<?> searchUserByEmail(@RequestParam String email) {
//         Optional<ParentUser> optionalParentUser = parentUserService.findByEmail(email);

//         if (optionalParentUser.isPresent()) {
//             ParentUser parentUser = optionalParentUser.get();
//             return ResponseEntity.ok(Collections.singletonMap("parentId", parentUser.getParentId()));
//         } else {
//             return ResponseEntity.ok(Collections.singletonMap("message", "User not found"));
//         }
//     }

//     //Get Pricing plan of child user associated to Parent User By Email ---- Vaishnav Kale
//     @GetMapping("/getPricingPlan")
//     public ResponseEntity<?> getPricingPlan(@RequestParam String email) {
//         Map<String, Object> pricingPlan = parentUserService.getPricingPlanForParent(email);

//         if (pricingPlan == null) {
//             return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No active plan found for this user.");
//         }

//         return ResponseEntity.ok(pricingPlan);
//     }

// }
package com.sensei.backend.controller;

import com.sensei.backend.dto.ParentUserDTO;
import com.sensei.backend.service.ParentUserService;
import com.sensei.backend.entity.ParentUser;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.*;

@RestController
@RequestMapping("/api/parent-users")
@RequiredArgsConstructor
@Slf4j
public class ParentUserController {

    private final ParentUserService parentUserService;

    @PostMapping
    public ResponseEntity<ParentUserDTO> create(@Valid @RequestBody ParentUserDTO dto) {
        return new ResponseEntity<>(parentUserService.createParentUser(dto), HttpStatus.CREATED);
    }

    @GetMapping("/{parentId}")
    public ResponseEntity<ParentUserDTO> getById(@PathVariable UUID parentId) {
        return ResponseEntity.ok(parentUserService.getParentUserById(parentId));
    }

    @GetMapping
    public ResponseEntity<List<ParentUserDTO>> getAll() {
        return ResponseEntity.ok(parentUserService.getAllParentUsers());
    }

    @PutMapping("/{parentId}")
    public ResponseEntity<ParentUserDTO> update(@PathVariable UUID parentId, @RequestBody ParentUserDTO dto) {
        return ResponseEntity.ok(parentUserService.updateParentUser(parentId, dto));
    }

    @DeleteMapping("/{parentId}")
    public ResponseEntity<Void> delete(@PathVariable UUID parentId) {
        parentUserService.deleteParentUser(parentId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/filter")
    public ResponseEntity<ParentUserDTO> byUserName(@RequestParam String userName) {
        return ResponseEntity.ok(parentUserService.getParentUserByUserName(userName));
    }

    @GetMapping("/phone")
    public ResponseEntity<ParentUserDTO> byPhone(@RequestParam String phone) {
        return ResponseEntity.ok(parentUserService.getParentUserByPhoneNumber(phone));
    }

    @GetMapping("/email")
    public ResponseEntity<?> byEmail(@RequestParam String email) {
        Optional<ParentUser> parent = parentUserService.findByEmail(email);
        if (parent.isPresent()) {
            return ResponseEntity.ok(Collections.singletonMap("parentId", parent.get().getParentId()));
        }
        return ResponseEntity.ok(Collections.singletonMap("message", "User not found"));
    }

    @GetMapping("/getPricingPlan")
    public ResponseEntity<?> getPricingPlan(@RequestParam String email) {
        Map<String, Object> pricingPlan = parentUserService.getPricingPlanForParent(email);
        if (pricingPlan == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No active plan found for this user.");
        }
        return ResponseEntity.ok(pricingPlan);
    }
}
