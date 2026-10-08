package com.sensei.backend.config;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import jakarta.annotation.PostConstruct;

import java.io.IOException;

@Configuration
@Slf4j
public class FirebaseConfig {

    @PostConstruct
    public void initialize() {
        try {
            if (FirebaseApp.getApps().isEmpty()) {
                FirebaseOptions options;
                
                // 1. Production approach: Read JSON from a Base64 encoded environment variable
                String base64Credentials = System.getenv("FIREBASE_CREDENTIALS_BASE64");
                
                if (base64Credentials != null && !base64Credentials.isEmpty()) {
                    byte[] decodedBytes = java.util.Base64.getDecoder().decode(base64Credentials);
                    java.io.InputStream credentialsStream = new java.io.ByteArrayInputStream(decodedBytes);
                    
                    options = FirebaseOptions.builder()
                            .setCredentials(GoogleCredentials.fromStream(credentialsStream))
                            .build();
                } else {
                    // 2. Local approach: Read from GOOGLE_APPLICATION_CREDENTIALS file path
                    options = FirebaseOptions.builder()
                            .setCredentials(GoogleCredentials.getApplicationDefault())
                            .build();
                }
                
                FirebaseApp.initializeApp(options);
                log.info("Firebase Admin SDK initialized successfully.");
            }
        } catch (IOException e) {
            log.error("Failed to initialize Firebase Admin SDK. Please set FIREBASE_CREDENTIALS_BASE64 or GOOGLE_APPLICATION_CREDENTIALS.", e);
        }
    }
}
