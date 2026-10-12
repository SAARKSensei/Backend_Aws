package com.sensei.backend.service;

import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.UserRecord;
import com.google.firebase.auth.UserRecord.CreateRequest;
import org.junit.jupiter.api.Assumptions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;

import java.util.ArrayList;
import java.util.UUID;

import com.sensei.backend.entity.ChildUser;
import com.sensei.backend.entity.ParentUser;
import com.sensei.backend.repository.ParentUserRepository;
import org.springframework.beans.factory.annotation.Autowired;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
@TestPropertySource(properties = {
    "razorpay.key.id=dummy_test_key_id",
    "razorpay.key.secret=dummy_test_key_secret",
    "JWT_SECRET=dummy_test_jwt_secret_must_be_256bits_long_minimum",
    "GOOGLE_CLIENT_ID=dummy_google_client_id"
})
class FirebaseAuthIntegrationTest {

    @Autowired
    private ParentUserService parentUserService;

    @Autowired
    private ParentUserRepository parentUserRepository;

    private void assumeFirebaseConfigured() {
        Assumptions.assumeFalse(
                FirebaseApp.getApps().isEmpty(),
                "Skipping test: Firebase Admin credentials not configured or failed to initialize"
        );
    }

    @Test
    void testFirebaseInitializationAndCredentials() {
        assumeFirebaseConfigured();
        FirebaseApp app = FirebaseApp.getInstance();
        assertEquals("sensei-97eef", app.getOptions().getProjectId(), "Firebase project ID should match sensei-97eef");
    }

    @Test
    void testFirebaseUserCreationAndDeletion() throws FirebaseAuthException {
        assumeFirebaseConfigured();

        FirebaseAuth auth = FirebaseAuth.getInstance();
        String testEmail = "test-agent-" + UUID.randomUUID().toString().substring(0, 8) + "@sensei.org.in";

        // 1. Create a temporary user in Firebase Auth
        CreateRequest request = new CreateRequest()
                .setEmail(testEmail)
                .setEmailVerified(false)
                .setPassword("SenseiTestPassword123!")
                .setDisplayName("Automated Test User");

        UserRecord createdUser = auth.createUser(request);
        assertNotNull(createdUser);
        assertNotNull(createdUser.getUid());
        assertEquals(testEmail, createdUser.getEmail());

        // 2. Fetch the user by email (exactly as deleteParentUser does)
        UserRecord fetchedUser = auth.getUserByEmail(testEmail);
        assertEquals(createdUser.getUid(), fetchedUser.getUid());

        // 3. Delete the user from Firebase Auth (exactly as deleteParentUser does)
        auth.deleteUser(fetchedUser.getUid());

        // 4. Verify user no longer exists in Firebase Auth
        FirebaseAuthException exception = assertThrows(FirebaseAuthException.class, () -> {
            auth.getUserByEmail(testEmail);
        });
        assertEquals("USER_NOT_FOUND", exception.getAuthErrorCode().name());
    }

    @Test
    void testDeleteParentUser_WithFirebaseUser() throws FirebaseAuthException {
        assumeFirebaseConfigured();

        FirebaseAuth auth = FirebaseAuth.getInstance();
        String uniqueSuffix = UUID.randomUUID().toString().substring(0, 8);
        String testEmail = "test-parent-" + uniqueSuffix + "@sensei.org.in";
        String testUsername = "parent_" + uniqueSuffix;

        // Step 1: Create user in Firebase Auth
        CreateRequest request = new CreateRequest()
                .setEmail(testEmail)
                .setPassword("SenseiTest12345!")
                .setDisplayName("Test Parent");
        UserRecord firebaseUser = auth.createUser(request);
        assertNotNull(firebaseUser);

        // Step 2: Create ParentUser in database
        ParentUser parent = ParentUser.builder()
                .name("Test Parent")
                .userName(testUsername)
                .email(testEmail)
                .childUsers(new ArrayList<>())
                .build();

        ChildUser child = ChildUser.builder()
                .childName("Test Child")
                .parentUser(parent)
                .build();
        parent.getChildUsers().add(child);

        ParentUser savedParent = parentUserRepository.save(parent);
        UUID parentId = savedParent.getParentId();
        assertNotNull(parentId);

        // Step 3: Execute ParentUserService.deleteParentUser
        parentUserService.deleteParentUser(parentId);

        // Step 4: Verify user is DELETED from Firebase Auth
        FirebaseAuthException authException = assertThrows(FirebaseAuthException.class, () -> {
            auth.getUserByEmail(testEmail);
        });
        assertEquals("USER_NOT_FOUND", authException.getAuthErrorCode().name(), "User must be deleted from Firebase");

        // Step 5: Verify user is soft-deleted from active database queries
        assertTrue(parentUserRepository.findById(parentId).isEmpty(), "Parent must not be returned by standard findById (SQLRestriction)");
    }

    @Test
    void testDeleteParentUser_GracefulWhenNotInFirebase() {
        String uniqueSuffix = UUID.randomUUID().toString().substring(0, 8);
        String testEmail = "nonexistent-in-firebase-" + uniqueSuffix + "@sensei.org.in";

        ParentUser parent = ParentUser.builder()
                .name("Non Firebase Parent")
                .userName("non_fb_" + uniqueSuffix)
                .email(testEmail)
                .childUsers(new ArrayList<>())
                .build();

        ParentUser savedParent = parentUserRepository.save(parent);
        UUID parentId = savedParent.getParentId();

        // Should not throw any exception even if user doesn't exist in Firebase Auth
        assertDoesNotThrow(() -> parentUserService.deleteParentUser(parentId));

        assertTrue(parentUserRepository.findById(parentId).isEmpty(), "Parent must be soft-deleted in database");
    }

    @Test
    void listFirebaseUsers() throws FirebaseAuthException {
        assumeFirebaseConfigured();

        com.google.firebase.auth.ListUsersPage page = FirebaseAuth.getInstance().listUsers(null);
        System.out.println("=================================================");
        System.out.println(">>> FIREBASE USERS CURRENTLY IN PROJECT sensei-97eef:");
        int count = 0;
        for (com.google.firebase.auth.ExportedUserRecord u : page.getValues()) {
            System.out.println(" - Email: " + u.getEmail() + " | UID: " + u.getUid() + " | Name: " + u.getDisplayName());
            count++;
            if (count >= 10) break;
        }
        System.out.println(">>> Total displayed: " + count);
        System.out.println("=================================================");
    }
}
