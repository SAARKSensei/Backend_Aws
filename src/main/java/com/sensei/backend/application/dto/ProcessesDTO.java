package com.sensei.backend.application.dto;

import lombok.Data;
import javax.validation.constraints.NotBlank;

@Data
public class ProcessesDTO {

    private String processId;

    @NotBlank
    private String processName;

    private int processNumber;

    private String senseiMessage;

    // Renamed
    private String childMessage;

    private String image;

    private int interactiveActivityRef;

    private String interactiveActivityIdRef;

    private String nextProcessIdRef;

    // NEW FIELD
    private String hint;

    public int getProcessNumber() {
        return this.processNumber;
    }
}
