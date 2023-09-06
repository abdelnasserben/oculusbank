package com.dabel.oculusbank.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;

@Data
@SuperBuilder
@NoArgsConstructor
public class ExchangeDTO {

    private int exchangeId;
    private String customerName;
    private String customerIdentity;
    private String saleCurrency;
    private String buyCurrency;
    private double amount;
    private String reason;
    private String failureReason;
    private String status;
    private String initiatedBy;
    private String updatedBy;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
