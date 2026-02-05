// package com.sensei.backend.controller;

// import com.sensei.backend.dto.PricingPlanDTO;
// import com.sensei.backend.service.PricingPlanService;
// import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.http.HttpStatus;
// import org.springframework.http.ResponseEntity;
// import org.springframework.web.bind.annotation.*;

// import javax.validation.Valid;
// import java.util.List;
// import java.util.Optional;

// @RestController
// @RequestMapping("/api/pricing-plans")
// @CrossOrigin(origins = "*") // ✅ SAFETY NET: Ensures this specific controller allows connections
// public class PricingPlanController {

//     @Autowired
//     private PricingPlanService pricingPlanService;

//     @PostMapping
//     public ResponseEntity<PricingPlanDTO> createPricingPlan(@Valid @RequestBody PricingPlanDTO pricingPlanDTO) {
//         PricingPlanDTO createdPricingPlan = pricingPlanService.createPricingPlan(pricingPlanDTO);
//         return new ResponseEntity<>(createdPricingPlan, HttpStatus.CREATED);
//     }

//     @GetMapping
//     public ResponseEntity<List<PricingPlanDTO>> getAllPricingPlans() {
//         List<PricingPlanDTO> pricingPlans = pricingPlanService.getAllPricingPlans();
//         return new ResponseEntity<>(pricingPlans, HttpStatus.OK);
//     }

//     @GetMapping("/{id}")
//     public ResponseEntity<PricingPlanDTO> getPricingPlanById(@PathVariable String id) {
//         Optional<PricingPlanDTO> pricingPlan = pricingPlanService.getPricingPlanById(id);
//         return pricingPlan.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
//     }

//     @PutMapping("/{id}")
//     public ResponseEntity<PricingPlanDTO> updatePricingPlan(@PathVariable String id, @Valid @RequestBody PricingPlanDTO pricingPlanDTO) {
//         PricingPlanDTO updatedPricingPlan = pricingPlanService.updatePricingPlan(id, pricingPlanDTO);
//         return new ResponseEntity<>(updatedPricingPlan, HttpStatus.OK);
//     }

//     @DeleteMapping("/{id}")
//     public ResponseEntity<Void> deletePricingPlan(@PathVariable String id) {
//         pricingPlanService.deletePricingPlan(id);
//         return new ResponseEntity<>(HttpStatus.NO_CONTENT);
//     }
// }
package com.sensei.backend.controller;

import com.sensei.backend.entity.PricingPlan;
import com.sensei.backend.service.PricingPlanService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/api/pricing-plans")
@RequiredArgsConstructor
public class PricingPlanController {

    private final PricingPlanService pricingPlanService;

    // Create Plan
    @PostMapping
    public ResponseEntity<PricingPlan> create(@RequestBody PricingPlan plan) {
        return ResponseEntity.ok(pricingPlanService.create(plan));
    }

    // Get all plans
    @GetMapping
    public ResponseEntity<List<PricingPlan>> getAll() {
        return ResponseEntity.ok(pricingPlanService.getAll());
    }

    // Get plan by ID
    @GetMapping("/{id}")
    public ResponseEntity<PricingPlan> getById(@PathVariable UUID id) {
        return ResponseEntity.ok(pricingPlanService.getById(id));
    }

    // Update plan
    @PutMapping("/{id}")
    public ResponseEntity<PricingPlan> update(
            @PathVariable UUID id,
            @RequestBody PricingPlan updatedPlan
    ) {
        return ResponseEntity.ok(pricingPlanService.update(id, updatedPlan));
    }

    // Delete plan
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        pricingPlanService.delete(id);
        return ResponseEntity.noContent().build();
    }
}

