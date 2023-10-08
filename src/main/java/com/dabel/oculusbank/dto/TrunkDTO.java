package com.dabel.oculusbank.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
@NoArgsConstructor
public class TrunkDTO extends AccountDTO {

    private int trunkId;
    private int customerId;
    private String membership;
    private String customerIdentityNumber;
}
