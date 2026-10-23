package com.sensei.backend.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ChildLockVerifyRequest {

    @NotBlank(message = "Child lock PIN is required")
    @Pattern(regexp = "^[0-9]{4}$", message = "Child lock PIN must be exactly 4 digits")
    private String pin;
}
