package com.sensei.backend.controller;

import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.sensei.backend.entity.ParentUser;
import com.sensei.backend.repository.ParentUserRepository;
import com.sensei.backend.security.JwtUtil;
import com.sensei.backend.service.GoogleAuthService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/**
 * @author vaishnav88sk
 */
@ExtendWith(MockitoExtension.class)
class AuthControllerTest {

    @Mock
    private GoogleAuthService googleAuthService;

    @Mock
    private ParentUserRepository parentUserRepository;

    @Mock
    private JwtUtil jwtUtil;

    @InjectMocks
    private AuthController authController;

    @BeforeEach
    void setUp() {
        ReflectionTestUtils.setField(authController, "testAdminEmail", "admin.sensei.org.in@gmail.com");
    }

    @Test
    void testGoogleLoginWithExistingUser() {
        GoogleIdToken.Payload payload = new GoogleIdToken.Payload();
        payload.setEmail("test@example.com");
        payload.set("name", "Test User");

        when(googleAuthService.verifyToken("valid-token")).thenReturn(payload);

        ParentUser existingUser = new ParentUser();
        existingUser.setEmail("test@example.com");

        when(parentUserRepository.findByEmail("test@example.com")).thenReturn(Optional.of(existingUser));
        when(jwtUtil.generateToken("test@example.com")).thenReturn("mock-jwt-token");

        ResponseEntity<?> response = authController.googleLogin("valid-token", "web");

        assertEquals(200, response.getStatusCode().value());
        assertEquals("mock-jwt-token", response.getBody());
        verify(parentUserRepository, never()).save(any(ParentUser.class));
    }

    @Test
    void testGoogleLoginWithNewUser() {
        GoogleIdToken.Payload payload = new GoogleIdToken.Payload();
        payload.setEmail("newuser@example.com");
        payload.set("name", "New User");

        when(googleAuthService.verifyToken("valid-token")).thenReturn(payload);
        when(parentUserRepository.findByEmail("newuser@example.com")).thenReturn(Optional.empty());
        when(parentUserRepository.save(any(ParentUser.class))).thenAnswer(i -> i.getArgument(0));
        when(jwtUtil.generateToken("newuser@example.com")).thenReturn("mock-jwt-token");

        ResponseEntity<?> response = authController.googleLogin("valid-token", "web");

        assertEquals(200, response.getStatusCode().value());
        assertEquals("mock-jwt-token", response.getBody());
        verify(parentUserRepository, times(1)).save(any(ParentUser.class));
    }

    @Test
    void testTestLoginWithValidEmail() {
        when(jwtUtil.generateToken("admin.sensei.org.in@gmail.com")).thenReturn("mock-admin-token");

        ResponseEntity<?> response = authController.testLogin("admin.sensei.org.in@gmail.com", "web");

        assertEquals(200, response.getStatusCode().value());
        assertEquals("mock-admin-token", response.getBody());
    }

    @Test
    void testTestLoginWithInvalidEmail() {
        ResponseEntity<?> response = authController.testLogin("hacker@example.com", "web");

        assertEquals(403, response.getStatusCode().value());
        assertEquals("Not allowed", response.getBody());
    }
}

