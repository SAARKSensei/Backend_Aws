// package com.sensei.backend.controller;

// import com.sensei.backend.dto.DigitalActivityResultCalculatorDTO;
// import com.sensei.backend.dto.DigitalActivityResultResponseDTO;
// import com.sensei.backend.entity.DigitalActivityResultCalculator;
// import com.sensei.backend.service.DigitalActivityResultCalculatorService;
// import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.http.HttpStatus;
// import org.springframework.http.ResponseEntity;
// import org.springframework.web.bind.annotation.*;

// @RestController
// @RequestMapping("/api/digital-activity-result")
// public class DigitalActivityResultCalculatorController {

//     @Autowired
//     private DigitalActivityResultCalculatorService service;

//     @PostMapping("/calculate")
//     public ResponseEntity<DigitalActivityResultResponseDTO> calculateResult(
//             @RequestBody DigitalActivityResultCalculatorDTO dto) {

//         DigitalActivityResultResponseDTO response = service.calculateResult(dto.getChildId(), dto.getDigitalActivityId());
//         return new ResponseEntity<>(response, HttpStatus.OK);
//     }

// }
