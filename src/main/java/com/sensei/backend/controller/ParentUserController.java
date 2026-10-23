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

// // import java.util.List;
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
import java.util.Collections;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.sensei.backend.dto.ParentUserDTO;
import com.sensei.backend.dto.ParentUserUpdateDTO;
import com.sensei.backend.entity.ParentUser;
import com.sensei.backend.entity.MasterTransaction;
import java.util.List;
import java.util.HashMap;
import com.google.firebase.auth.UserRecord;
import com.sensei.backend.service.FirebaseAuthService;
import com.sensei.backend.service.ParentUserService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/api/parent-users")
@RequiredArgsConstructor
@Slf4j
public class ParentUserController {

    private final ParentUserService parentUserService;
    private final FirebaseAuthService firebaseAuthService;

    @PostMapping
    public ResponseEntity<ParentUserDTO> create(@Valid @RequestBody ParentUserDTO dto) {
        log.info("Request to create ParentUser: {}", dto.getEmail());
        return new ResponseEntity<>(parentUserService.createParentUser(dto), HttpStatus.CREATED);
    }

    @GetMapping("/{parentId}")
    public ResponseEntity<ParentUserDTO> getById(@PathVariable UUID parentId) {
        return ResponseEntity.ok(parentUserService.getParentUserById(parentId));
    }

    @GetMapping("/{parentId}/transactions")
    public ResponseEntity<List<MasterTransaction>> getTransactions(@PathVariable UUID parentId) {
        return ResponseEntity.ok(parentUserService.getTransactionsByParentId(parentId));
    }

    @GetMapping
    public ResponseEntity<org.springframework.data.domain.Page<ParentUserDTO>> getAll(org.springframework.data.domain.Pageable pageable) {
        return ResponseEntity.ok(parentUserService.getAllParentUsers(pageable));
    }

    @PutMapping("/{parentId}")
    public ResponseEntity<ParentUserDTO> update(@PathVariable UUID parentId, @Valid @RequestBody ParentUserUpdateDTO dto) {
        return ResponseEntity.ok(parentUserService.updateParentUser(parentId, dto));
    }

    @PatchMapping("/{parentId}")
    public ResponseEntity<ParentUserDTO> patch(@PathVariable UUID parentId, @Valid @RequestBody ParentUserUpdateDTO dto) {
        return ResponseEntity.ok(parentUserService.updateParentUser(parentId, dto));
    }

    @DeleteMapping("/{parentId}")
    public ResponseEntity<Void> delete(@PathVariable UUID parentId) {
        log.info("Request to delete ParentUser: {}", parentId);
        parentUserService.deleteParentUser(parentId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/filter")
    public ResponseEntity<ParentUserDTO> byUserName(@RequestParam String userName) {
        return ResponseEntity.ok(parentUserService.getParentUserByUserName(userName));
    }

    @GetMapping("/phone")
    public ResponseEntity<ParentUserDTO> byPhone(@RequestParam String phone) {
        log.info("Request to fetch ParentUser by phone: {}", phone);
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
    @GetMapping("/admin/deleted")
    public ResponseEntity<List<com.sensei.backend.dto.DeletedUserProjection>> getAllDeletedUsers() {
        log.info("Request to fetch all deleted ParentUsers (Admin API)");
        return ResponseEntity.ok(parentUserService.getAllDeletedUsers());
    }

    @GetMapping("/admin/firebase-user")
    public ResponseEntity<?> getFirebaseUserByEmail(@RequestParam String email) {
        log.info("Admin request to check Firebase user by email: {}", email);
        if (!firebaseAuthService.isAvailable()) {
            return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE)
                    .body(Map.of("error", "FirebaseApp not initialized"));
        }
        Optional<UserRecord> userOpt = firebaseAuthService.getUserByEmail(email);
        if (userOpt.isPresent()) {
            UserRecord user = userOpt.get();
            Map<String, Object> response = new HashMap<>();
            response.put("exists", true);
            response.put("uid", user.getUid());
            response.put("email", user.getEmail());
            response.put("displayName", user.getDisplayName());
            response.put("disabled", user.isDisabled());
            return ResponseEntity.ok(response);
        } else {
            Map<String, Object> notFound = new HashMap<>();
            notFound.put("exists", false);
            notFound.put("message", "User not found in Firebase Auth");
            return ResponseEntity.ok(notFound);
        }
    }

    @GetMapping("/admin/firebase-users")
    public ResponseEntity<?> listFirebaseUsers() {
        log.info("Admin request to list Firebase users");
        if (!firebaseAuthService.isAvailable()) {
            return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE)
                    .body(Map.of("error", "FirebaseApp not initialized"));
        }
        return ResponseEntity.ok(firebaseAuthService.listUsers(100));
    }

    @PostMapping("/admin/firebase-sync")
    public ResponseEntity<?> syncAllUsersToFirebase() {
        log.info("Admin request to sync all active DB users to Firebase Auth");
        return ResponseEntity.ok(parentUserService.syncAllUsersToFirebase());
    }
}
