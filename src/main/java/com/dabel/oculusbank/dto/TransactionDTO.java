package com.dabel.oculusbank.dto;

import com.dabel.oculusbank.app.validation.Currency;
import com.dabel.oculusbank.app.validation.TransactionType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;

@Data
@SuperBuilder
@NoArgsConstructor
public class TransactionDTO {

    private int transactionId;
    @TransactionType
    private String transactionType;
    private int accountId;
    @Positive
    private double amount;
   @Currency
    private String currency;
    private String sourceType;
    private String sourceValue;
    @NotBlank
    private String reason;
    @NotBlank
    private String customerIdentity, customerFullName;
    private String failureReason;
    private String status;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private String initiatedBy;
    private String updatedBy;
    private String accountName;
    @NotBlank
    private String accountNumber;

}
