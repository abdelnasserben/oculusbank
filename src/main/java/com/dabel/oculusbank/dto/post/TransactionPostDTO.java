package com.dabel.oculusbank.dto.post;

import com.dabel.oculusbank.constant.Currency;
import com.dabel.oculusbank.constant.SourceType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import lombok.Data;

@Data
public class TransactionPostDTO {

    @NotBlank
    private String accountNumber;
    private Currency currency;
    @Positive
    private double amount;
    private SourceType sourceType;
    @NotBlank
    private String sourceValue;
    private String reason;
}
