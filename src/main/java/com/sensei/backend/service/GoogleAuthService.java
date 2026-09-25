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

    @Value("${GOOGLE_ANDROID_CLIENT_ID:}")
    private String androidClientId;

    @Value("${GOOGLE_IOS_CLIENT_ID:}")
    private String iosClientId;

    public GoogleIdToken.Payload verifyToken(String idTokenString) {

        try {
            // Log unverified token payload for debugging
            try {
                GoogleIdToken unverifiedToken = GoogleIdToken.parse(JacksonFactory.getDefaultInstance(), idTokenString);
                if (unverifiedToken != null && unverifiedToken.getPayload() != null) {
                    log.debug("Received Google ID Token with audience: {}, azp: {}", 
                            unverifiedToken.getPayload().getAudience(), 
                            unverifiedToken.getPayload().getAuthorizedParty());
                    log.debug("Expected Google Client ID: {}", clientId);
                }
            } catch (Exception e) {
                log.warn("Failed to parse unverified token: {}", e.getMessage());
            }

            java.util.List<String> allowedClientIds = new java.util.ArrayList<>();
            allowedClientIds.add(clientId);
            
            if (androidClientId != null && !androidClientId.trim().isEmpty()) {
                for (String id : androidClientId.split(",")) {
                    if (!id.trim().isEmpty()) {
                        allowedClientIds.add(id.trim());
                    }
                }
            }
            if (iosClientId != null && !iosClientId.trim().isEmpty()) {
                for (String id : iosClientId.split(",")) {
                    if (!id.trim().isEmpty()) {
                        allowedClientIds.add(id.trim());
                    }
                }
            }

            GoogleIdTokenVerifier verifier = new GoogleIdTokenVerifier.Builder(
                    new NetHttpTransport(),
                    JacksonFactory.getDefaultInstance()
            )
                    .setAudience(allowedClientIds)
                    .build();

            GoogleIdToken idToken = verifier.verify(idTokenString);

            if (idToken != null) {
                return idToken.getPayload();
            } else {
                log.error("Google Token Verification Failed. Token might have an invalid signature, wrong audience/azp, or be expired. Allowed Client IDs: {}", allowedClientIds);
                throw new RuntimeException("Invalid Google token");
            }

        } catch (Exception e) {
            log.error("Google token verification error", e);
            throw new RuntimeException("Google token verification failed: " + e.getMessage(), e);
        }
    }
}