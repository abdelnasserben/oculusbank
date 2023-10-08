package com.dabel.oculusbank.service.core.account;

import com.dabel.oculusbank.dto.VaultDTO;
import com.dabel.oculusbank.service.support.account.AccountReadService;

import java.util.List;

public interface VaultCruService extends AccountReadService<VaultDTO> {

    VaultDTO save(int accountId, int branchId);

    VaultDTO findByBranchId(int branchId);

    List<VaultDTO> findAllByBranchId(int branchId);
}
