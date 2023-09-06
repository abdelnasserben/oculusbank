package com.dabel.oculusbank.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class VaultDTO extends AccountDTO {
    private int vaultId;
    private int branchId;
}
