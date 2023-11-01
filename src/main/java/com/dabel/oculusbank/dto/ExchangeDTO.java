package com.dabel.oculusbank.dto;

import com.dabel.oculusbank.app.custom.validation.Currency;
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
public class ExchangeDTO extends BasicDTO {

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
}
