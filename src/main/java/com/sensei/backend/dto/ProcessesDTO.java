package com.sensei.backend.dto;

import lombok.Data;
import javax.validation.constraints.NotBlank;

@Data
public class ProcessesDTO {
    private String processId;

    @NotBlank
    private String processName;

    private int processNumber;

    private String senseiMessage;

    private String parentMessage;

    private String image;

    private int interactiveActivityRef;

    // New Field
    private String interactiveActivityIdRef;

    // New Field
    private String nextProcessIdRef;

    // Getter for processNumber
    public int getProcessNumber() {
        return this.processNumber;
    }
}
