package com.sensei.backend.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import com.sensei.backend.dto.referral.ReferralCodeResponseDTO;
import com.sensei.backend.dto.referral.ReferralUsageResponseDTO;
import com.sensei.backend.entity.ReferralCode;
import com.sensei.backend.entity.ReferralUsage;
import com.sensei.backend.exception.ValidationException;
import com.sensei.backend.repository.ReferralCodeRepository;
import com.sensei.backend.repository.ReferralUsageRepository;
import com.sensei.backend.service.impl.ReferralServiceImpl;


@ExtendWith(MockitoExtension.class)
class ReferralServiceTest {

    @Mock
    private ReferralCodeRepository referralCodeRepository;

    @Mock
    private ReferralUsageRepository referralUsageRepository;

    @Mock
    private WalletService walletService;

    @InjectMocks
    private ReferralServiceImpl referralService;

    private UUID parentId;
    private ReferralCode existingCode;

    @BeforeEach
    void setUp() {
        parentId = UUID.randomUUID();
        ReflectionTestUtils.setField(referralService, "maxReferralUses", 10);
        ReflectionTestUtils.setField(referralService, "referralRewardAmount", 100);

        existingCode = ReferralCode.builder()
                .id(UUID.randomUUID())
                .parentId(parentId)
                .code("SENSEI-TEST-1234")
                .maxUsage(10)
                .usedCount(1)
                .active(true)
                .createdAt(LocalDateTime.now())
                .build();
    }

    @Test
    void testGetReferralCodeForParent_WhenCodeExists() {
        when(referralCodeRepository.findByParentId(parentId)).thenReturn(Optional.of(existingCode));

        ReferralCodeResponseDTO response = referralService.getReferralCodeForParent(parentId);

        assertNotNull(response);
        assertEquals(existingCode.getCode(), response.getCode());
        assertEquals(parentId, response.getParentId());
        verify(referralCodeRepository, never()).save(any());
    }

    @Test
    void testGetReferralCodeForParent_WhenCodeDoesNotExist_ShouldAutoGenerate() {
        when(referralCodeRepository.findByParentId(parentId)).thenReturn(Optional.empty());
        when(referralCodeRepository.existsByCode(anyString())).thenReturn(false);
        when(referralCodeRepository.save(any(ReferralCode.class))).thenAnswer(invocation -> invocation.getArgument(0));

        ReferralCodeResponseDTO response = referralService.getReferralCodeForParent(parentId);

        assertNotNull(response);
        assertNotNull(response.getCode());
        assertTrue(response.getCode().startsWith("SENSEI-"));
        assertEquals(parentId, response.getParentId());
        assertEquals(0, response.getUsedCount());
        assertTrue(response.getActive());
        verify(referralCodeRepository, times(1)).save(any(ReferralCode.class));
    }

    @Test
    void testGetReferralUsage_WhenCodeExists() {
        when(referralCodeRepository.findByParentId(parentId)).thenReturn(Optional.of(existingCode));

        ReferralUsage usage = ReferralUsage.builder()
                .referralCodeId(existingCode.getId())
                .referrerParentId(parentId)
                .referredParentId(UUID.randomUUID())
                .rewardAmount(100)
                .usedAt(LocalDateTime.now())
                .build();

        when(referralUsageRepository.findByReferralCodeId(existingCode.getId())).thenReturn(List.of(usage));

        List<ReferralUsageResponseDTO> result = referralService.getReferralUsage(parentId);

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(100, result.get(0).getRewardAmount());
    }

    @Test
    void testGetReferralUsage_WhenCodeDoesNotExist_ShouldReturnEmptyListWithoutThrowing() {
        when(referralCodeRepository.findByParentId(parentId)).thenReturn(Optional.empty());

        List<ReferralUsageResponseDTO> result = referralService.getReferralUsage(parentId);

        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(referralUsageRepository, never()).findByReferralCodeId(any());
    }

    @Test
    void testApplyReferralCode_Success_WithTrimmingAndCaseInsensitivity() {
        UUID referredParentId = UUID.randomUUID();
        when(referralCodeRepository.findByCodeAndActiveTrue("SENSEI-TEST-1234")).thenReturn(Optional.of(existingCode));
        when(referralUsageRepository.existsByReferredParentId(referredParentId)).thenReturn(false);

        referralService.applyReferralCode(referredParentId, "  sensei-test-1234  ");

        assertEquals(2, existingCode.getUsedCount());
        verify(referralUsageRepository, times(1)).save(any(ReferralUsage.class));
        verify(referralCodeRepository, times(1)).save(existingCode);
        verify(walletService, times(2)).credit(any());
    }

    @Test
    void testApplyReferralCode_CannotUseOwnCode() {
        when(referralCodeRepository.findByCodeAndActiveTrue("SENSEI-TEST-1234")).thenReturn(Optional.of(existingCode));

        assertThrows(ValidationException.class, () ->
                referralService.applyReferralCode(parentId, "SENSEI-TEST-1234")
        );
    }

    @Test
    void testApplyReferralCode_InvalidCode() {
        when(referralCodeRepository.findByCodeAndActiveTrue("INVALID")).thenReturn(Optional.empty());

        assertThrows(ValidationException.class, () ->
                referralService.applyReferralCode(UUID.randomUUID(), "INVALID")
        );
    }
}
