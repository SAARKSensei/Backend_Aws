package com.sensei.backend.dto;

import lombok.Data;
import javax.validation.constraints.NotBlank;
import java.util.List;

@Data
public class SubModuleDTO {
    private String subModuleId;

    @NotBlank
    private String subModuleName;

    private int subModuleNumber;

    private List<InteractiveActivityDTO> interactiveActivities;

    private List<DigitalActivityDTO> digitalActivities;

    // New Field
    private String moduleIdRef;

    // New Field
    private String interactiveActivityIdRef;

    // New Field
    private String digitalActivityIdRef;

    // Getter for subModuleNumber
    public int getSubModuleNumber() {
        return this.subModuleNumber;
    }
}
