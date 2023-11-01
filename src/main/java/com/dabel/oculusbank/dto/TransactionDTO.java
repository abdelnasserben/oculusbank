package com.dabel.oculusbank.dto;

import com.dabel.oculusbank.app.validation.Currency;
import com.dabel.oculusbank.app.validation.TransactionType;
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
public class TransactionDTO extends BasicDTO {

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
    private String accountName;
    @NotBlank
    private String accountNumber;

}
