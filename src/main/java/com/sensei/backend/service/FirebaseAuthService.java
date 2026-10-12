package com.sensei.backend.service;

import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.ExportedUserRecord;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.ListUsersPage;
import com.google.firebase.auth.UserRecord;
import com.sensei.backend.entity.ParentUser;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * Service encapsulating all Firebase Authentication Admin SDK operations.
 * Handles user provisioning, lookup, deletion, and bulk synchronization.
 *
 * @author vaishnav88sk
 */
@Service
@Slf4j
public class FirebaseAuthService {

    /**
     * Checks if Firebase Admin SDK is initialized and available.
     *
     * @return true if initialized, false otherwise
     */
    public boolean isAvailable() {
        return !FirebaseApp.getApps().isEmpty();
    }

    /**
     * Retrieves a Firebase user record by email address.
     *
     * @param email user email
     * @return Optional containing UserRecord if found, empty otherwise
     */
    public Optional<UserRecord> getUserByEmail(String email) {
        if (!isAvailable() || email == null || email.trim().isEmpty()) {
            return Optional.empty();
        }
        try {
            return Optional.of(FirebaseAuth.getInstance().getUserByEmail(email.trim()));
        } catch (FirebaseAuthException e) {
            log.debug("User {} not found in Firebase Auth: {}", email, e.getMessage());
            return Optional.empty();
        } catch (Exception e) {
            log.warn("Unexpected error looking up user {} in Firebase Auth: {}", email, e.getMessage());
            return Optional.empty();
        }
    }

    /**
     * Ensures that a user with the given email exists in Firebase Auth.
     * If not present, creates the user with emailVerified = true.
     *
     * @param email user email
     * @param displayName user display name
     * @return true if user exists or was created successfully, false on error
     */
    public boolean ensureUser(String email, String displayName) {
        if (!isAvailable() || email == null || email.trim().isEmpty() || email.startsWith("del-")) {
            return false;
        }
        String cleanEmail = email.trim();
        try {
            Optional<UserRecord> existing = getUserByEmail(cleanEmail);
            if (existing.isPresent()) {
                return true;
            }
            UserRecord.CreateRequest request = new UserRecord.CreateRequest()
                    .setEmail(cleanEmail)
                    .setEmailVerified(true)
                    .setDisplayName(displayName != null && !displayName.trim().isEmpty() ? displayName.trim() : cleanEmail);
            FirebaseAuth.getInstance().createUser(request);
            log.info("Registered user in Firebase Auth: {}", cleanEmail);
            return true;
        } catch (FirebaseAuthException e) {
            if ("EMAIL_ALREADY_EXISTS".equals(e.getAuthErrorCode().name()) || "EMAIL_EXISTS".equals(e.getAuthErrorCode().name())) {
                log.info("User already exists in Firebase Auth: {}", cleanEmail);
                return true;
            }
            log.warn("Firebase Auth error registering {}: {}", cleanEmail, e.getMessage());
            return false;
        } catch (Exception e) {
            log.warn("Unexpected error syncing user {} to Firebase Auth: {}", cleanEmail, e.getMessage());
            return false;
        }
    }

    /**
     * Deletes a user from Firebase Auth by email address.
     *
     * @param email user email to delete
     * @return true if deleted or not present, false on unexpected error
     */
    public boolean deleteUserByEmail(String email) {
        if (!isAvailable() || email == null || email.trim().isEmpty() || email.startsWith("del-")) {
            return false;
        }
        String cleanEmail = email.trim();
        try {
            UserRecord record = FirebaseAuth.getInstance().getUserByEmail(cleanEmail);
            FirebaseAuth.getInstance().deleteUser(record.getUid());
            log.info("Successfully deleted user from Firebase Auth: {}", cleanEmail);
            return true;
        } catch (FirebaseAuthException e) {
            if ("USER_NOT_FOUND".equals(e.getAuthErrorCode().name())) {
                log.info("User {} already does not exist in Firebase Auth", cleanEmail);
                return true;
            }
            log.warn("User {} failed to delete from Firebase Auth: {}", cleanEmail, e.getMessage());
            return false;
        } catch (Exception e) {
            log.warn("Unexpected error deleting user {} from Firebase Auth: {}", cleanEmail, e.getMessage());
            return false;
        }
    }

    /**
     * Lists users from Firebase Auth up to maxResults.
     *
     * @param maxResults maximum number of users to fetch (<=0 for default 100)
     * @return list of simplified user maps
     */
    public List<Map<String, Object>> listUsers(int maxResults) {
        List<Map<String, Object>> users = new ArrayList<>();
        if (!isAvailable()) {
            return users;
        }
        int limit = maxResults > 0 ? maxResults : 100;
        try {
            ListUsersPage page = FirebaseAuth.getInstance().listUsers(null);
            int count = 0;
            for (ExportedUserRecord u : page.getValues()) {
                Map<String, Object> item = new HashMap<>();
                item.put("uid", u.getUid());
                item.put("email", u.getEmail());
                item.put("displayName", u.getDisplayName());
                item.put("disabled", u.isDisabled());
                users.add(item);
                count++;
                if (count >= limit) {
                    break;
                }
            }
        } catch (Exception e) {
            log.warn("Failed to list users from Firebase Auth: {}", e.getMessage());
        }
        return users;
    }

    /**
     * Bulk synchronizes active PostgreSQL ParentUsers to Firebase Auth.
     *
     * @param parents list of parent user entities
     * @return sync summary map
     */
    public Map<String, Object> syncUsers(List<ParentUser> parents) {
        Map<String, Object> result = new HashMap<>();
        if (!isAvailable()) {
            result.put("error", "FirebaseApp not initialized");
            return result;
        }
        int created = 0;
        int alreadyExists = 0;
        int failed = 0;

        for (ParentUser parent : parents) {
            String email = parent.getEmail();
            if (email == null || email.trim().isEmpty() || email.startsWith("del-")) {
                continue;
            }
            try {
                Optional<UserRecord> userRecord = getUserByEmail(email);
                if (userRecord.isPresent()) {
                    alreadyExists++;
                } else {
                    boolean success = ensureUser(email, parent.getName() != null ? parent.getName() : parent.getUserName());
                    if (success) {
                        created++;
                    } else {
                        failed++;
                    }
                }
            } catch (Exception ex) {
                failed++;
            }
        }

        result.put("totalActiveParentsInDb", parents.size());
        result.put("createdInFirebase", created);
        result.put("alreadyExistedInFirebase", alreadyExists);
        result.put("failed", failed);
        return result;
    }
}
