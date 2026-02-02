package com.sensei.backend.adapter.controller;
/*
package com.sensei.backend.controller;

import com.otpless.authsdk.OTPResponse;
import com.otpless.authsdk.OTPVerificationResponse;
import com.sensei.backend.dto.OTPRequestDTO;
import com.sensei.backend.dto.OTPVerificationDTO;
import com.sensei.backend.dto.ResendOTPRequest;
import com.sensei.backend.service.OtplessService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/otp")
public class OTPController {

    private final OtplessService otplessService;

    @Autowired
    public OTPController(OtplessService otplessService) {
        this.otplessService = otplessService;
    }

    @PostMapping("/send")
    public ResponseEntity<String> sendOTP(@RequestBody OTPRequestDTO otpRequest) {
        try {
            OTPResponse otpResponse = otplessService.sendOTP(
                    otpRequest.getDtCode(),
                    otpRequest.getPhoneNumber(),
                    otpRequest.getEmail(),
                    otpRequest.getOrderId(),
                    otpRequest.getOtpTTL(),
                    otpRequest.getOtpLength(),
                    otpRequest.getChannel()
            );

            if (otpResponse.isSuccess()) {
                return ResponseEntity.ok("OTP sent. Order ID: " + otpResponse.getOrderId());
            } else {
                return ResponseEntity.status(400).body("OTP send failed: " + otpResponse.getErrorMessage());
            }
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Internal Server Error: " + e.getMessage());
        }
    }

//    @PostMapping("/resend")
//    public ResponseEntity<String> resendOTP(@RequestBody String dtCode) {
//        try {
//            OTPResponse otpResponse = otplessService.resendOTP(dtCode);
//            if (otpResponse.isSuccess()) {
//                return ResponseEntity.ok("OTP resent. Order ID: " + otpResponse.getOrderId());
//            } else {
//                return ResponseEntity.status(400).body("OTP resend failed: " + otpResponse.getErrorMessage());
//            }
//        } catch (Exception e) {
//            return ResponseEntity.status(500).body("Internal Server Error: " + e.getMessage());
//        }
//    }

    @PostMapping("/resend")
    public ResponseEntity<String> resendOTP(@RequestBody ResendOTPRequest resendOTPRequest) {
        try {
            OTPResponse otpResponse = otplessService.resendOTP(resendOTPRequest.getDtCode());
            if (otpResponse.isSuccess()) {
                return ResponseEntity.ok("OTP resent. Order ID: " + otpResponse.getOrderId());
            } else {
                return ResponseEntity.status(400).body("OTP resend failed: " + otpResponse.getErrorMessage());
            }
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Internal Server Error: " + e.getMessage());
        }
    }

    @PostMapping("/verify")
    public ResponseEntity<String> verifyOTP(@RequestBody OTPVerificationDTO otpVerificationRequest) {
        try {
            OTPVerificationResponse otpVerificationResponse = otplessService.verifyOTP(
                    otpVerificationRequest.getDtCode(),
                    otpVerificationRequest.getOtpCode(),
                    otpVerificationRequest.getPhoneNumber(),
                    otpVerificationRequest.getEmail()
            );

            if (otpVerificationResponse.isSuccess() && otpVerificationResponse.getIsOTPVerified()) {
                return ResponseEntity.ok("OTP verified successfully.");
            } else {
                return ResponseEntity.status(400).body("OTP verification failed: " + otpVerificationResponse.getReason());
            }
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Internal Server Error: " + e.getMessage());
        }
    }
}
*/
