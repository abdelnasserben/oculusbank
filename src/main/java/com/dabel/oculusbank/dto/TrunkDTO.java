package com.dabel.oculusbank.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class TrunkDTO extends AccountDTO {

    private int trunkId;
    private int customerId;
    private String membership;
    private String customerIdentityNumber;
}
