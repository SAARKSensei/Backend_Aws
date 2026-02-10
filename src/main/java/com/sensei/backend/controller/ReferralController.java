package com.sensei.backend.controller;

import com.sensei.backend.dto.referral.ReferralCodeResponseDTO;
import com.sensei.backend.dto.referral.ReferralUsageResponseDTO;
import com.sensei.backend.service.ReferralService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/referrals")
@RequiredArgsConstructor
public class ReferralController {

    private final ReferralService referralService;

    /**
     * Generate referral code for parent
     * (Called once after signup / first login)
     */
    @PostMapping("/generate")
    public ResponseEntity<String> generateReferralCode(
            @RequestParam UUID parentId
    ) {
        String code = referralService.generateReferralCode(parentId);
        return ResponseEntity.ok(code);
    }

    /**
     * Apply referral code
     * (Called by referred user)
     */
    @PostMapping("/apply")
    public ResponseEntity<String> applyReferralCode(
            @RequestParam UUID parentId,
            @RequestParam String referralCode
    ) {
        referralService.applyReferralCode(parentId, referralCode);
        return ResponseEntity.ok("Referral code applied successfully");
    }


    /**
 * Get referral code for logged-in parent
 */
@GetMapping("/my-code")
public ResponseEntity<ReferralCodeResponseDTO> getMyReferralCode(
        @RequestParam UUID parentId
) {
    return ResponseEntity.ok(
            referralService.getReferralCodeForParent(parentId)
    );
}


/**
 * Get referral usage history (analytics)
 */
@GetMapping("/usage")
public ResponseEntity<List<ReferralUsageResponseDTO>> getReferralUsage(
        @RequestParam UUID parentId
) {
    return ResponseEntity.ok(
            referralService.getReferralUsage(parentId)
    );
}

}
