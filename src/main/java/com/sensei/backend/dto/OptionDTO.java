package com.sensei.backend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OptionDTO {
    private String text;
    private String status;
    private String counsellorNote;
}
