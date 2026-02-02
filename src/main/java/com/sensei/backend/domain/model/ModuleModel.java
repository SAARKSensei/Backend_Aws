package com.sensei.backend.domain.model;

import java.time.Instant;
import java.util.UUID;

public class ModuleModel {
    private UUID id;
    private UUID subjectId;
    private String subjectName;
    private String name;
    private String description;
    private Integer orderIndex;
    private Boolean isActive;

    public ModuleModel(
        UUID id,
        UUID subjectId,
        String subjectName,
        String name,
        String description,
        Integer orderIndex,
        Boolean isActive
    ){
        this.id = id;
        this.subjectId = subjectId;
        this.description = description;
        this.subjectName = subjectName;
        this.name = name;
        this.orderIndex = orderIndex;
        this.isActive = isActive;
    }

    // ===== Business behavior =====

    public void update(String name, String description, Integer orderIndex){
        this.name = name;
        this.description = description;
        this.orderIndex = orderIndex;
    }

    public void deactivate(){
        this.isActive = false;
    }

    // ===== Getters =====
    public UUID getId(){
        return id;
    }

    public UUID getSubId(){
        return subjectId;
    }

    public String getSubName(){
        return subjectName;
    }

    public String getName(){
        return name;
    }

    public String getDescription(){
        return description;
    }

    public Integer getOrderIndex(){
        return orderIndex;
    }

    public boolean isActive(){
        return isActive;
    }
}
