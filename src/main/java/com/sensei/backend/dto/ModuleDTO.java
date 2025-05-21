package com.sensei.backend.dto;

import lombok.Data;
import javax.validation.constraints.NotBlank;
import java.util.List;

@Data
public class ModuleDTO {
    private String moduleId;

    @NotBlank
    private String moduleName;

    private int moduleNumber;

    private List<SubModuleDTO> subModules;

    // New Field
    private String subjectIdRef;

    // New Field
//    private String submoduleIdRef;
    private List<String> submoduleIdRef;

    // Getter for moduleNumber
    public int getModuleNumber() {
        return this.moduleNumber;
    }
}
