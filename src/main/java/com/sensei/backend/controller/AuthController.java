package com.sensei.backend.controller;

import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.sensei.backend.security.JwtUtil;
import com.sensei.backend.entity.ParentUser;
import com.sensei.backend.repository.ParentUserRepository;
import com.sensei.backend.service.GoogleAuthService;
import lombok.RequiredArgsConstructor;
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
    public ResponseEntity<?> googleLogin(@RequestParam String idToken) {
        log.info("Google login attempt");
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
        String token = jwtUtil.generateToken(email);

        return ResponseEntity.ok(token);
    }

    @Value("${TEST_ADMIN_EMAIL:admin.sensei.org.in@gmail.com}")
    private String testAdminEmail;

    // TEMP ADMIN LOGIN FOR POSTMAN (NEW)
    @PostMapping("/test-login")
    public ResponseEntity<?> testLogin(@RequestParam String email) {
        log.info("Test login attempt for email: {}", email);
        if (!email.equals(testAdminEmail)) {
            return ResponseEntity.status(403).body("Not allowed");
        }

        String token = jwtUtil.generateToken(email);
        return ResponseEntity.ok(token);
    }
}