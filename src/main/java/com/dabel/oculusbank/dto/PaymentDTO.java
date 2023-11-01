package com.dabel.oculusbank.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class PaymentDTO extends BasicDTO {

    private int paymentId;
    private int debitAccountId;
    private int creditAccountId;
    @Positive
    private double amount;
    private String currency;
    @NotBlank
    private String reason;
    private String failureReason;
    private String debitAccountName;
    @NotBlank
    private String debitAccountNumber;
    private String creditAccountName;
    @NotBlank
    private String creditAccountNumber;
}
