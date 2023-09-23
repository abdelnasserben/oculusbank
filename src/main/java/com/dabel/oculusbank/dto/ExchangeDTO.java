package com.dabel.oculusbank.dto;

import com.dabel.oculusbank.app.validation.Currency;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;

@Data
@SuperBuilder
@NoArgsConstructor
public class ExchangeDTO {

    private int exchangeId;
    @NotBlank
    private String customerName;
    @NotBlank
    private String customerIdentity;
    @Currency
    private String purchaseCurrency;
    @Positive
    private double purchaseAmount;
    @Currency
    private String saleCurrency;
    private double saleAmount;
    @NotBlank
    private String reason;
    private String failureReason;
    private String status;
    private String initiatedBy;
    private String updatedBy;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
