package com.dabel.oculusbank.service.support.account;

import com.dabel.oculusbank.constant.Status;
import com.dabel.oculusbank.dto.VaultDTO;
import com.dabel.oculusbank.exception.AccountNotFoundException;
import com.dabel.oculusbank.mapper.VaultMapper;
import com.dabel.oculusbank.model.Vault;
import com.dabel.oculusbank.model.VaultView;
import com.dabel.oculusbank.repository.VaultRepository;
import com.dabel.oculusbank.repository.VaultViewRepository;
import com.dabel.oculusbank.service.core.account.VaultService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class VaultServiceImpl implements VaultService {

    @Autowired
    VaultRepository vaultRepository;
    @Autowired
    VaultViewRepository vaultViewRepository;

    @Override
    public VaultDTO save(int accountId, int branchId) {

        VaultDTO vaultDTO = VaultDTO.builder()
                .accountId(accountId)
                .branchId(branchId)
                .build();
        Vault savedVault = vaultRepository.save(VaultMapper.toEntity(vaultDTO));
        vaultDTO.setVaultId(savedVault.getVaultId());

        return vaultDTO;
    }

    @Override
    public VaultDTO findByBranchId(int branchId) {

        VaultView vaultView = vaultViewRepository.findByBranchId(branchId)
                .orElseThrow(AccountNotFoundException::new);

        return VaultMapper.toDTO(vaultView);
    }

    @Override
    public List<VaultDTO> findAllByBranchId(int branchId) {

        List<VaultView> vaults = vaultViewRepository.findAllByBranchId(branchId);

        return vaults.stream()
                .map(VaultMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public VaultDTO findByNumber(String accountNumber) {
        VaultView vaultView = vaultViewRepository.findByAccountNumber(accountNumber)
                .orElseThrow(AccountNotFoundException::new);

        return VaultMapper.toDTO(vaultView);
    }

    private static VaultDTO formatStatusAndMapToDto(VaultView vaultView) {

        vaultView.setStatus(Status.nameOf(vaultView.getStatus()));
        return VaultMapper.toDTO(vaultView);
    }
}
