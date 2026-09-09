package com.sensei.backend.controller;

import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.sensei.backend.security.JwtUtil;
import com.sensei.backend.entity.ParentUser;
import com.sensei.backend.repository.ParentUserRepository;
import com.sensei.backend.service.GoogleAuthService;
import lombok.RequiredArgsConstructor;
import com.sensei.backend.dto.auth.AuthResponse;
import com.sensei.backend.dto.auth.TokenRefreshRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;
import lombok.extern.slf4j.Slf4j;

/**
 * @author vaishnav88sk
 */
@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@Slf4j
public class AuthController {

    private final GoogleAuthService googleAuthService;
    private final ParentUserRepository parentUserRepository;
    private final JwtUtil jwtUtil;

    // GOOGLE LOGIN (UNCHANGED)
    @PostMapping("/google")
    public ResponseEntity<?> googleLogin(@RequestParam String idToken, 
                                         @RequestParam(required = false, defaultValue = "web") String client) {
        log.info("Google login attempt for client: {}", client);
        GoogleIdToken.Payload payload = googleAuthService.verifyToken(idToken);

        String email = payload.getEmail();
        String name = (String) payload.get("name");

        // Check if user exists
        Optional<ParentUser> existingUser = parentUserRepository.findByEmail(email);

        ParentUser user;

        if (existingUser.isPresent()) {
            user = existingUser.get();
        } else {
            // Create new user
            user = ParentUser.builder()
                    .email(email)
                    .name(name)
                    .userName(email)
                    .build();

            user = parentUserRepository.save(user);
            log.info("New parent user created from Google login: {}", email);
        }

        // Generate JWT
        String accessToken = jwtUtil.generateToken(email);
        
        if ("app".equalsIgnoreCase(client)) {
            String refreshToken = jwtUtil.generateRefreshToken(email);
            AuthResponse authResponse = AuthResponse.builder()
                    .accessToken(accessToken)
                    .refreshToken(refreshToken)
                    .build();
            return ResponseEntity.ok(authResponse);
        }

        return ResponseEntity.ok(accessToken);
    }

    @Value("${TEST_ADMIN_EMAIL:admin.sensei.org.in@gmail.com}")
    private String testAdminEmail;

    // TEMP ADMIN LOGIN FOR POSTMAN (NEW)
    @PostMapping("/test-login")
    public ResponseEntity<?> testLogin(@RequestParam String email, 
                                       @RequestParam(required = false, defaultValue = "web") String client) {
        log.info("Test login attempt for email: {} from client: {}", email, client);
        if (!email.equals(testAdminEmail)) {
            return ResponseEntity.status(403).body("Not allowed");
        }

        String accessToken = jwtUtil.generateToken(email);
        
        if ("app".equalsIgnoreCase(client)) {
            String refreshToken = jwtUtil.generateRefreshToken(email);
            AuthResponse authResponse = AuthResponse.builder()
                    .accessToken(accessToken)
                    .refreshToken(refreshToken)
                    .build();
            return ResponseEntity.ok(authResponse);
        }
                
        return ResponseEntity.ok(accessToken);
    }

    @PostMapping("/refresh")
    public ResponseEntity<?> refreshAccessToken(@RequestBody TokenRefreshRequest request) {
        String refreshToken = request.getRefreshToken();
        
        if (refreshToken != null && jwtUtil.validateToken(refreshToken)) {
            String email = jwtUtil.extractEmail(refreshToken);
            
            String newAccessToken = jwtUtil.generateToken(email);
            String newRefreshToken = jwtUtil.generateRefreshToken(email); // Issue a new refresh token (rotating)
            
            AuthResponse authResponse = AuthResponse.builder()
                    .accessToken(newAccessToken)
                    .refreshToken(newRefreshToken)
                    .build();
                    
            return ResponseEntity.ok(authResponse);
        } else {
            return ResponseEntity.status(403).body("Invalid or expired refresh token");
        }
    }
}