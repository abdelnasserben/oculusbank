package com.dabel.oculusbank.dto;

import com.dabel.oculusbank.app.custom.validation.CardType;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class CardApplicationDTO extends BasicDTO {

    private int requestId;
    private int accountId;
    private int customerId;
    @CardType
    private String cardType;
    private String failureReason;
    private String accountName;
    @NotBlank
    private String accountNumber;
    private String customerFirstName;
    private String customerLastName;
    @NotBlank
    private String customerIdentityNumber;
}
