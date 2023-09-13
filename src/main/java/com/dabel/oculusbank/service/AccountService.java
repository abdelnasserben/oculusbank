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

    public VaultDTO saveVault(int accountId, int branchId) {

        VaultDTO vaultDTO = VaultDTO.builder()
                .accountId(accountId)
                .branchId(branchId)
                .build();
        Vault savedVault = vaultRepository.save(VaultMapper.toEntity(vaultDTO));
        vaultDTO.setVaultId(savedVault.getVaultId());

        return vaultDTO;
    }

    public TrunkDTO saveTrunk(int accountId, int customerId, String membership) {

        TrunkDTO trunkDTO = TrunkDTO.builder()
                .accountId(accountId)
                .customerId(customerId)
                .membership(membership)
                .build();
        Trunk savedTrunk = trunkRepository.save(TrunkMapper.toEntity(trunkDTO));
        trunkDTO.setTrunkId(savedTrunk.getTrunkId());

        return trunkDTO;
    }

    public AccountDTO findByNumber(String accountNumber) {
        Account account = accountRepository.findByAccountNumber(accountNumber)
                .orElseThrow(AccountNotFoundException::new);
        return AccountMapper.toDTO(account);
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

    public TrunkDTO findTrunkByNumber(String accountNumber) {
        TrunkView trunkView = trunkViewRepository.findByAccountNumber(accountNumber)
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
