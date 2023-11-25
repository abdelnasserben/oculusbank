package com.dabel.oculusbank.service.core.branch;

import com.dabel.oculusbank.app.util.AccountNumberGenerator;
import com.dabel.oculusbank.constant.AccountProfile;
import com.dabel.oculusbank.constant.AccountType;
import com.dabel.oculusbank.constant.Currency;
import com.dabel.oculusbank.constant.Status;
import com.dabel.oculusbank.dto.AccountDTO;
import com.dabel.oculusbank.dto.BranchDTO;
import com.dabel.oculusbank.service.core.account.BasicAccountCrudService;
import com.dabel.oculusbank.service.core.account.VaultService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BranchFacadeService {

    @Autowired
    BranchCrudService branchCrudService;
    @Autowired
    BasicAccountCrudService basicAccountCrudService;
    @Autowired
    VaultService vaultService;

    public BranchDTO create(BranchDTO branchDTO, double[] vaultsAsset) {

        branchDTO.setStatus(Status.ACTIVE.code());
        BranchDTO savedBranch = branchCrudService.save(branchDTO);

        //TODO: save vault KMF
        AccountDTO savedAccountKmf = basicAccountCrudService.save(
                AccountDTO.builder()
                .accountName(savedBranch.getBranchName())
                .accountNumber(AccountNumberGenerator.generate())
                .accountType(AccountType.BUSINESS.name())
                .accountProfile(AccountProfile.SYSTEM.name())
                .currency(Currency.KMF.name())
                .balance(vaultsAsset[0])
                .status(Status.ACTIVE.code())
                .build());
        vaultService.save(savedAccountKmf.getAccountId(), savedBranch.getBranchId());

        //TODO: save vault EUR
        AccountDTO savedAccountEur = basicAccountCrudService.save(
                AccountDTO.builder()
                .accountName(savedBranch.getBranchName())
                .accountNumber(AccountNumberGenerator.generate())
                .accountType(AccountType.BUSINESS.name())
                .accountProfile(AccountProfile.SYSTEM.name())
                .currency(Currency.EUR.name())
                .balance(vaultsAsset[1])
                .status(Status.ACTIVE.code())
                .build());
        vaultService.save(savedAccountEur.getAccountId(), savedBranch.getBranchId());

        //TODO: save vault USD
        AccountDTO savedAccountUsd = basicAccountCrudService.save(
                AccountDTO.builder()
                        .accountName(savedBranch.getBranchName())
                        .accountNumber(AccountNumberGenerator.generate())
                        .accountType(AccountType.BUSINESS.name())
                        .accountProfile(AccountProfile.SYSTEM.name())
                        .currency(Currency.USD.name())
                        .balance(vaultsAsset[0])
                        .status(Status.ACTIVE.code())
                        .build());
        vaultService.save(savedAccountUsd.getAccountId(), savedBranch.getBranchId());

        return savedBranch;
    }

    public List<BranchDTO> findAll() {
        return branchCrudService.findAll();
    }
}
