package com.sensei.backend.config;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import jakarta.annotation.PostConstruct;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Base64;

@Configuration
@Slf4j
public class FirebaseConfig {

    @Value("${FIREBASE_CREDENTIALS_BASE64:}")
    private String base64Credentials;

    @Value("${GOOGLE_APPLICATION_CREDENTIALS:}")
    private String credentialsPath;

    @PostConstruct
    public void initialize() {
        try {
            if (FirebaseApp.getApps().isEmpty()) {
                FirebaseOptions options = null;

                // 1. Try Base64-encoded credentials from .env or OS environment
                if (base64Credentials != null && !base64Credentials.trim().isEmpty()) {
                    try {
                        byte[] decodedBytes = Base64.getDecoder().decode(base64Credentials.trim());
                        InputStream credentialsStream = new ByteArrayInputStream(decodedBytes);
                        GoogleCredentials credentials = GoogleCredentials.fromStream(credentialsStream);
                        FirebaseOptions.Builder builder = FirebaseOptions.builder()
                                .setCredentials(credentials)
                                .setHttpTransport(new com.google.api.client.http.javanet.NetHttpTransport());
                        if (credentials instanceof com.google.auth.oauth2.ServiceAccountCredentials sac) {
                            if (sac.getProjectId() != null) {
                                builder.setProjectId(sac.getProjectId());
                            }
                        }
                        options = builder.build();
                        log.info("Initialized Firebase Admin SDK from Base64 credentials for project: {}", options.getProjectId());
                    } catch (Exception ex) {
                        log.warn("Failed to decode FIREBASE_CREDENTIALS_BASE64: {}", ex.getMessage());
                    }
                }

                // 2. Try file path credentials from .env or OS environment
                if (options == null && credentialsPath != null && !credentialsPath.trim().isEmpty()) {
                    File credFile = new File(credentialsPath.trim());
                    if (credFile.exists()) {
                        try (InputStream fileStream = new FileInputStream(credFile)) {
                            GoogleCredentials credentials = GoogleCredentials.fromStream(fileStream);
                            FirebaseOptions.Builder builder = FirebaseOptions.builder()
                                    .setCredentials(credentials)
                                    .setHttpTransport(new com.google.api.client.http.javanet.NetHttpTransport());
                            if (credentials instanceof com.google.auth.oauth2.ServiceAccountCredentials sac) {
                                if (sac.getProjectId() != null) {
                                    builder.setProjectId(sac.getProjectId());
                                }
                            }
                            options = builder.build();
                            log.info("Initialized Firebase Admin SDK from credentials file for project: {}", options.getProjectId());
                        } catch (Exception ex) {
                            log.warn("Failed to read credentials from {}: {}", credFile.getAbsolutePath(), ex.getMessage());
                        }
                    } else {
                        log.warn("GOOGLE_APPLICATION_CREDENTIALS path specified but file does not exist: {}", credentialsPath);
                    }
                }

                // 3. Fallback to Google Application Default Credentials
                if (options == null) {
                    try {
                        options = FirebaseOptions.builder()
                                .setCredentials(GoogleCredentials.getApplicationDefault())
                                .setHttpTransport(new com.google.api.client.http.javanet.NetHttpTransport())
                                .build();
                        log.info("Initialized Firebase Admin SDK from Application Default Credentials.");
                    } catch (Exception ex) {
                        log.warn("Application Default Credentials not available: {}", ex.getMessage());
                    }
                }

                if (options != null) {
                    FirebaseApp.initializeApp(options);
                    log.info("Firebase Admin SDK successfully initialized for project: {}", options.getProjectId());
                } else {
                    log.warn("Firebase Admin SDK could not be initialized with any credentials. Firebase user deletion will be bypassed.");
                }
            }
        } catch (Exception e) {
            log.error("Failed to initialize Firebase Admin SDK: {}", e.getMessage(), e);
        }
    }
}
