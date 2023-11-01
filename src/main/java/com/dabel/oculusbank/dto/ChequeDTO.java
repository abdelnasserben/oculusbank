package com.dabel.oculusbank.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class ChequeDTO extends BasicDTO {

    private int chequeId;
    private int accountId;
    private String chequeName;
    private String chequeNumber;
    private String accountName;
    private String accountNumber;
}
