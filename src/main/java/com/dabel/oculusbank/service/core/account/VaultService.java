package com.dabel.oculusbank.service.core.account;

import com.dabel.oculusbank.dto.VaultDTO;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface VaultService extends ReadAccountService<VaultDTO> {

    VaultDTO save(int accountId, int branchId);

    VaultDTO findByBranchId(int branchId);

    List<VaultDTO> findAllByBranchId(int branchId);
}
