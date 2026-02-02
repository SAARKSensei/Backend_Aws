package com.sensei.backend.domain.model;

import java.time.Instant;
import java.util.UUID;

public class SubjectModel {

    private UUID id;
    private String name;
    private String description;
    private String iconUrl;
    private boolean active;
    private Instant createdAt;

    public SubjectModel(
            UUID id,
            String name,
            String description,
            String iconUrl,
            boolean active,
            Instant createdAt
    ) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.iconUrl = iconUrl;
        this.active = active;
        this.createdAt = createdAt;
    }

    // ===== Business behavior =====

    public void update(String name, String description, String iconUrl) {
        this.name = name;
        this.description = description;
        this.iconUrl = iconUrl;
    }

    public void deactivate() {
        this.active = false;
    }

    // ===== Getters =====

    public UUID getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public String getIconUrl() {
        return iconUrl;
    }

    public boolean isActive() {
        return active;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }
}
