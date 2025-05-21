package com.sensei.backend.dto;

import lombok.Data;

@Data
public class PaymentRequestDTO {
    private double amount;
    private String currency;
    private String receipt;
}
