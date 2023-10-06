package com.dabel.oculusbank.service.delegate;

import com.dabel.oculusbank.app.util.account.AccountNumberGenerator;
import com.dabel.oculusbank.constant.AccountProfile;
import com.dabel.oculusbank.constant.AccountType;
import com.dabel.oculusbank.constant.Currency;
import com.dabel.oculusbank.constant.Status;
import com.dabel.oculusbank.dto.AccountDTO;
import com.dabel.oculusbank.dto.BranchDTO;
import com.dabel.oculusbank.service.AccountService;
import com.dabel.oculusbank.service.BranchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DelegateBranchService {

    @Autowired
    BranchService branchService;
    @Autowired
    AccountService accountService;

    public BranchDTO create(BranchDTO branchDTO, double[] vaultsAsset) {

        branchDTO.setStatus(Status.Active.code());
        BranchDTO savedBranch = branchService.save(branchDTO);

        //TODO: save vault KMF
        AccountDTO savedAccountKmf = accountService.save(
                AccountDTO.builder()
                .accountName(savedBranch.getBranchName())
                .accountNumber(AccountNumberGenerator.generate())
                .accountType(AccountType.Business.name())
                .accountProfile(AccountProfile.System.name())
                .currency(Currency.KMF.name())
                .balance(vaultsAsset[0])
                .status(Status.Active.code())
                .build());
        accountService.saveVault(savedAccountKmf.getAccountId(), savedBranch.getBranchId());

        //TODO: save vault EUR
        AccountDTO savedAccountEur = accountService.save(
                AccountDTO.builder()
                .accountName(savedBranch.getBranchName())
                .accountNumber(AccountNumberGenerator.generate())
                .accountType(AccountType.Business.name())
                .accountProfile(AccountProfile.System.name())
                .currency(Currency.EUR.name())
                .balance(vaultsAsset[1])
                .status(Status.Active.code())
                .build());
        accountService.saveVault(savedAccountEur.getAccountId(), savedBranch.getBranchId());

        //TODO: save vault USD
        AccountDTO savedAccountUsd = accountService.save(
                AccountDTO.builder()
                        .accountName(savedBranch.getBranchName())
                        .accountNumber(AccountNumberGenerator.generate())
                        .accountType(AccountType.Business.name())
                        .accountProfile(AccountProfile.System.name())
                        .currency(Currency.USD.name())
                        .balance(vaultsAsset[0])
                        .status(Status.Active.code())
                        .build());
        accountService.saveVault(savedAccountUsd.getAccountId(), savedBranch.getBranchId());

        return savedBranch;
    }

    public List<BranchDTO> findAll() {
        return branchService.findAll();
    }
}
