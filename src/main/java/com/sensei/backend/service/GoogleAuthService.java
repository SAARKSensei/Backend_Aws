package com.sensei.backend.service;

import java.util.Collections;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class GoogleAuthService {

    @Value("${GOOGLE_CLIENT_ID}")
    private String clientId;

    public GoogleIdToken.Payload verifyToken(String idTokenString) {

        try {
            // Log unverified token payload for debugging
            try {
                GoogleIdToken unverifiedToken = GoogleIdToken.parse(JacksonFactory.getDefaultInstance(), idTokenString);
                if (unverifiedToken != null && unverifiedToken.getPayload() != null) {
                    log.debug("Received Google ID Token with audience: {}", unverifiedToken.getPayload().getAudience());
                    log.debug("Expected Google Client ID: {}", clientId);
                }
            } catch (Exception e) {
                log.warn("Failed to parse unverified token: {}", e.getMessage());
            }

            GoogleIdTokenVerifier verifier = new GoogleIdTokenVerifier.Builder(
                    new NetHttpTransport(),
                    JacksonFactory.getDefaultInstance()
            )
                    .setAudience(Collections.singletonList(clientId))
                    .build();

            GoogleIdToken idToken = verifier.verify(idTokenString);

            if (idToken != null) {
                return idToken.getPayload();
            } else {
                log.error("Google Token Verification Failed. Token might have an invalid signature, wrong audience, or be expired.");
                throw new RuntimeException("Invalid Google token");
            }

        } catch (Exception e) {
            log.error("Google token verification error", e);
            throw new RuntimeException("Google token verification failed: " + e.getMessage(), e);
        }
    }
}