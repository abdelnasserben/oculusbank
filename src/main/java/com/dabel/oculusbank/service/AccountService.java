package com.dabel.oculusbank.service;

import com.dabel.oculusbank.constant.Status;
import com.dabel.oculusbank.dto.AccountDTO;
import com.dabel.oculusbank.dto.TrunkDTO;
import com.dabel.oculusbank.dto.VaultDTO;
import com.dabel.oculusbank.exception.AccountNotFoundException;
import com.dabel.oculusbank.mapper.AccountMapper;
import com.dabel.oculusbank.mapper.TrunkMapper;
import com.dabel.oculusbank.mapper.VaultMapper;
import com.dabel.oculusbank.model.*;
import com.dabel.oculusbank.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class AccountService {

    @Autowired
    AccountRepository accountRepository;
    @Autowired
    VaultRepository vaultRepository;
    @Autowired
    VaultViewRepository vaultViewRepository;
    @Autowired
    TrunkRepository trunkRepository;
    @Autowired
    TrunkViewRepository trunkViewRepository;


    public AccountDTO save(AccountDTO accountDTO) {
        Account account = accountRepository.save(AccountMapper.toEntity(accountDTO));
        return AccountMapper.toDTO(account);
    }

    public AccountDTO findByNumber(String accountNumber) {
        Account account = accountRepository.findByAccountNumber(accountNumber)
                .orElseThrow(AccountNotFoundException::new);
        return AccountMapper.toDTO(account);
    }

    public VaultDTO saveVault(VaultDTO vaultDTO) {
        Account savedAccount = accountRepository.save(AccountMapper.toEntity(vaultDTO));

        //TODO: actualize vault info
        vaultDTO.setAccountId(savedAccount.getAccountId());
        vaultDTO.setCreatedAt(savedAccount.getCreatedAt());
        vaultDTO.setUpdatedAt(savedAccount.getUpdatedAt());

        //TODO: save vault
        Vault savedVault = vaultRepository.save(VaultMapper.toEntity(vaultDTO));

        //TODO: actualize again vault info
        vaultDTO.setVaultId(savedVault.getVaultId());

        return vaultDTO;
    }

    public List<VaultDTO> findAllVaultsByBranchId(int branchId) {
        List<VaultView> vaults = vaultViewRepository.findAllByBranchId(branchId);
        return vaults.stream()
                .map(AccountService::formatVaultStatusToNameAndGetDTO)
                .collect(Collectors.toList());
    }

    public VaultDTO findVaultByBranchId(int branchId) {
        VaultView vaultView = vaultViewRepository.findByBranchId(branchId)
                .orElseThrow(AccountNotFoundException::new);

        return formatVaultStatusToNameAndGetDTO(vaultView);
    }

    public TrunkDTO saveTrunk(TrunkDTO trunkDTO) {
        Account savedAccount = accountRepository.save(AccountMapper.toEntity(trunkDTO));

        //TODO: actualize trunk info
        trunkDTO.setAccountId(savedAccount.getAccountId());
        trunkDTO.setCreatedAt(savedAccount.getCreatedAt());
        trunkDTO.setUpdatedAt(savedAccount.getUpdatedAt());

        //TODO: save trunk
        Trunk savedTrunk = trunkRepository.save(TrunkMapper.toEntity(trunkDTO));

        //TODO: actualize again trunk info
        trunkDTO.setTrunkId(savedTrunk.getTrunkId());

        return trunkDTO;
    }

    public List<TrunkDTO> findAllTrunksByCustomerId(int customerId) {
        List<TrunkView> trunks = trunkViewRepository.findAllByCustomerId(customerId);
        return trunks.stream()
                .map(AccountService::formatTrunkStatusToNameAndGetDTO)
                .collect(Collectors.toList());
    }

    public TrunkDTO findTrunkByCustomerId(int customerId) {
        TrunkView trunkView = trunkViewRepository.findByCustomerId(customerId)
                .orElseThrow(AccountNotFoundException::new);

        return formatTrunkStatusToNameAndGetDTO(trunkView);
    }

    public boolean isTrunk(String accountNumber) {
        return trunkViewRepository.findByAccountNumber(accountNumber).isPresent();
    }

    private static VaultDTO formatVaultStatusToNameAndGetDTO(VaultView vaultView) {
        vaultView.setStatus(Status.nameOf(vaultView.getStatus()));
        return VaultMapper.toDTO(vaultView);
    }

    private static TrunkDTO formatTrunkStatusToNameAndGetDTO(TrunkView t) {
        t.setStatus(Status.nameOf(t.getStatus()));
        return TrunkMapper.toDTO(t);
    }

}
