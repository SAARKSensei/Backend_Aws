package com.sensei.backend.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author vaishnav88sk
 */
class GoogleAuthServiceTest {

    private GoogleAuthService googleAuthService;

    @BeforeEach
    void setUp() {
        googleAuthService = new GoogleAuthService();
        ReflectionTestUtils.setField(googleAuthService, "clientId", "dummy-client-id");
    }

    @Test
    void testVerifyTokenWithInvalidTokenThrowsException() {
        // Because the token is malformed and not a real Google JWT, the verifier will fail to parse it
        // and throw an exception, which the service catches and rethrows as RuntimeException.
        
        Exception exception = assertThrows(RuntimeException.class, () -> {
            googleAuthService.verifyToken("invalid-google-token");
        });

        assertTrue(exception.getMessage().startsWith("Google token verification failed"));
    }
    
    @Test
    void testVerifyTokenWithNullTokenThrowsException() {
        Exception exception = assertThrows(RuntimeException.class, () -> {
            googleAuthService.verifyToken(null);
        });

        assertTrue(exception.getMessage().startsWith("Google token verification failed"));
    }
}
