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
package com.sensei.backend.adapter.controller;

import com.sensei.backend.application.dto.pricingplan.*;
import com.sensei.backend.domain.service.PricingPlanService;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/pricing-plans")
@RequiredArgsConstructor
public class PricingPlanController {

    private final PricingPlanService service;

    @PostMapping
    public PricingPlanResponseDTO create(@RequestBody PricingPlanRequestDTO dto) {
        return service.create(dto);
    }

    @GetMapping
    public List<PricingPlanResponseDTO> getAll() {
        return service.getAll();
    }

    @GetMapping("/{id}")
    public PricingPlanResponseDTO getById(@PathVariable UUID id) {
        return service.getById(id);
    }

    @GetMapping("/status/{status}")
    public List<PricingPlanResponseDTO> getByStatus(@PathVariable String status) {
        return service.getByStatus(status);
    }

    @PutMapping("/{id}")
    public PricingPlanResponseDTO update(@PathVariable UUID id, @RequestBody PricingPlanRequestDTO dto) {
        return service.update(id, dto);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable UUID id) {
        service.delete(id);
    }
}
