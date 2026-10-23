package com.sensei.backend.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.sensei.backend.dto.ParentUserDTO;
import com.sensei.backend.dto.ParentUserUpdateDTO;
import com.sensei.backend.entity.ParentUser;
import com.sensei.backend.exception.ResourceNotFoundException;
import com.sensei.backend.mapper.ParentUserMapper;
import com.sensei.backend.repository.ParentUserRepository;
import com.sensei.backend.repository.PricingPlanRepository;
import com.sensei.backend.repository.ChildUserRepository;
import com.sensei.backend.repository.MasterTransactionRepository;

/**
 * @author vaishnav88sk
 */
@ExtendWith(MockitoExtension.class)
class ParentUserServiceTest {

    @Mock
    private ParentUserRepository parentUserRepository;

    @Mock
    private PricingPlanRepository pricingPlanRepository;

    @Mock
    private ParentUserMapper parentUserMapper;

    @Mock
    private ChildUserRepository childUserRepository;

    @Mock
    private MasterTransactionRepository masterTransactionRepository;

    @Mock
    private FirebaseAuthService firebaseAuthService;

    @InjectMocks
    private ParentUserService parentUserService;

    private ParentUser parentUser;
    private ParentUserDTO parentUserDTO;
    private UUID testId;

    @BeforeEach
    void setUp() {
        testId = UUID.randomUUID();

        parentUser = new ParentUser();
        parentUser.setParentId(testId);
        parentUser.setEmail("test@example.com");
        parentUser.setUserName("testuser");
        parentUser.setChildUsers(new java.util.ArrayList<>());

        parentUserDTO = new ParentUserDTO();
        parentUserDTO.setParentId(testId);
        parentUserDTO.setEmail("test@example.com");
        parentUserDTO.setName("Test User");
    }

    @Test
    void testCreateParentUser() {
        when(parentUserMapper.toEntity(parentUserDTO)).thenReturn(parentUser);
        when(parentUserRepository.save(any(ParentUser.class))).thenReturn(parentUser);
        when(parentUserMapper.toDto(parentUser)).thenReturn(parentUserDTO);

        ParentUserDTO result = parentUserService.createParentUser(parentUserDTO);

        assertNotNull(result);
        assertEquals("test@example.com", result.getEmail());
        verify(parentUserRepository, times(1)).save(parentUser);
        verify(childUserRepository, times(1)).save(any());
        verify(firebaseAuthService, times(1)).ensureUser("test@example.com", "Test User");
    }

    @Test
    void testGetParentUserByIdSuccess() {
        when(parentUserRepository.findById(testId)).thenReturn(Optional.of(parentUser));
        when(parentUserMapper.toDto(parentUser)).thenReturn(parentUserDTO);

        ParentUserDTO result = parentUserService.getParentUserById(testId);

        assertNotNull(result);
        assertEquals(testId, result.getParentId());
    }

    @Test
    void testGetParentUserByIdThrowsNotFound() {
        when(parentUserRepository.findById(testId)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> {
            parentUserService.getParentUserById(testId);
        });
    }

    @Test
    void testFindByEmail() {
        when(parentUserRepository.findByEmail("test@example.com")).thenReturn(Optional.of(parentUser));

        Optional<ParentUser> result = parentUserService.findByEmail("test@example.com");

        assertTrue(result.isPresent());
        assertEquals("test@example.com", result.get().getEmail());
    }

    @Test
    void testDeleteParentUser_Success() {
        when(parentUserRepository.findById(testId)).thenReturn(Optional.of(parentUser));

        parentUserService.deleteParentUser(testId);

        assertTrue(parentUser.getIsDeleted());
        assertNotNull(parentUser.getDeletedAt());
        assertTrue(parentUser.getEmail().startsWith("del-test@example.com-"));
        assertTrue(parentUser.getUserName().startsWith("del-testuser-"));

        verify(firebaseAuthService, times(1)).deleteUserByEmail("test@example.com");
        verify(parentUserRepository, times(1)).save(parentUser);
    }

    @Test
    void testDeleteParentUser_NotFound() {
        when(parentUserRepository.findById(testId)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> {
            parentUserService.deleteParentUser(testId);
        });
    }

    @Test
    void testSyncAllUsersToFirebase() {
        java.util.List<ParentUser> users = java.util.List.of(parentUser);
        when(parentUserRepository.findAll()).thenReturn(users);
        when(firebaseAuthService.syncUsers(users)).thenReturn(java.util.Map.of("createdInFirebase", 1));

        java.util.Map<String, Object> result = parentUserService.syncAllUsersToFirebase();

        assertNotNull(result);
        assertEquals(1, result.get("createdInFirebase"));
        verify(firebaseAuthService, times(1)).syncUsers(users);
    }

    @Test
    void testUpdateParentUser_Partial() {
        ParentUserUpdateDTO updateDTO = new ParentUserUpdateDTO();
        updateDTO.setName("Updated Name");
        updateDTO.setCity("Mumbai");
        updateDTO.setPincode("400001");

        when(parentUserRepository.findById(testId)).thenReturn(Optional.of(parentUser));
        when(parentUserRepository.save(parentUser)).thenReturn(parentUser);
        when(parentUserMapper.toDto(parentUser)).thenReturn(parentUserDTO);

        ParentUserDTO result = parentUserService.updateParentUser(testId, updateDTO);

        assertNotNull(result);
        verify(parentUserRepository, times(1)).findById(testId);
        verify(parentUserMapper, times(1)).updateEntityFromUpdateDto(updateDTO, parentUser);
        verify(parentUserRepository, times(1)).save(parentUser);
    }
}
