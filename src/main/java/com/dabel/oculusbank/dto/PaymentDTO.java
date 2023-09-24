package com.dabel.oculusbank.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;

@Data
@SuperBuilder
@NoArgsConstructor
public class PaymentDTO {

    private int paymentId;
    private int debitAccountId;
    private int creditAccountId;
    @Positive
    private double amount;
    private String currency;
    @NotBlank
    private String reason;
    private String failureReason;
    private String status;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private String initiatedBy;
    private String updatedBy;
    private String debitAccountName;
    @NotBlank
    private String debitAccountNumber;
    private String creditAccountName;
    @NotBlank
    private String creditAccountNumber;
}
