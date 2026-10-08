package com.sensei.backend.dto;

import java.time.LocalDateTime;
import java.util.UUID;

public interface DeletedUserProjection {
    UUID getParentId();
    String getName();
    String getEmail();
    String getUserName();
    LocalDateTime getDeletedAt();
}
