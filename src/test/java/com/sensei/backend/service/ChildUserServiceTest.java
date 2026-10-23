package com.sensei.backend.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
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

import com.sensei.backend.dto.ChildUserDTO;
import com.sensei.backend.dto.ChildUserUpdateDTO;
import com.sensei.backend.entity.ChildUser;
import com.sensei.backend.entity.ParentUser;
import com.sensei.backend.exception.ResourceNotFoundException;
import com.sensei.backend.mapper.ChildUserMapper;
import com.sensei.backend.repository.ChildUserRepository;
import com.sensei.backend.repository.ParentUserRepository;
import com.sensei.backend.repository.PricingPlanRepository;

@ExtendWith(MockitoExtension.class)
class ChildUserServiceTest {

    @Mock
    private ChildUserRepository childUserRepository;

    @Mock
    private ParentUserRepository parentUserRepository;

    @Mock
    private PricingPlanRepository pricingPlanRepository;

    @Mock
    private ChildUserMapper childUserMapper;

    @InjectMocks
    private ChildUserService childUserService;

    private ChildUser childUser;
    private ChildUserDTO childUserDTO;
    private UUID testChildId;
    private UUID testParentId;

    @BeforeEach
    void setUp() {
        testChildId = UUID.randomUUID();
        testParentId = UUID.randomUUID();

        ParentUser parent = new ParentUser();
        parent.setParentId(testParentId);

        childUser = new ChildUser();
        childUser.setChildId(testChildId);
        childUser.setChildName("Original Name");
        childUser.setParentUser(parent);
        childUser.setGrade("Grade 1");

        childUserDTO = new ChildUserDTO();
        childUserDTO.setChildId(testChildId);
        childUserDTO.setChildName("Original Name");
        childUserDTO.setParentId(testParentId);
    }

    @Test
    void testUpdate_Partial() {
        ChildUserUpdateDTO updateDTO = new ChildUserUpdateDTO();
        updateDTO.setChildName("Updated Name");

        when(childUserRepository.findById(testChildId)).thenReturn(Optional.of(childUser));
        when(childUserRepository.save(childUser)).thenReturn(childUser);
        when(childUserMapper.toDto(childUser)).thenReturn(childUserDTO);

        ChildUserDTO result = childUserService.update(testChildId, updateDTO);

        assertNotNull(result);
        verify(childUserRepository, times(1)).findById(testChildId);
        verify(childUserMapper, times(1)).updateEntityFromUpdateDto(updateDTO, childUser);
        verify(childUserRepository, times(1)).save(childUser);
    }

    @Test
    void testUpdate_NotFound() {
        ChildUserUpdateDTO updateDTO = new ChildUserUpdateDTO();
        when(childUserRepository.findById(testChildId)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> {
            childUserService.update(testChildId, updateDTO);
        });
    }

    @Test
    void testUpdate_ReassignParent() {
        UUID newParentId = UUID.randomUUID();
        ParentUser newParent = new ParentUser();
        newParent.setParentId(newParentId);

        ChildUserUpdateDTO updateDTO = new ChildUserUpdateDTO();
        updateDTO.setParentId(newParentId);

        when(childUserRepository.findById(testChildId)).thenReturn(Optional.of(childUser));
        when(parentUserRepository.findById(newParentId)).thenReturn(Optional.of(newParent));
        when(childUserRepository.save(childUser)).thenReturn(childUser);
        when(childUserMapper.toDto(childUser)).thenReturn(childUserDTO);

        ChildUserDTO result = childUserService.update(testChildId, updateDTO);

        assertNotNull(result);
        assertEquals(newParent, childUser.getParentUser());
        verify(parentUserRepository, times(1)).findById(newParentId);
        verify(childUserRepository, times(1)).save(childUser);
    }
}
