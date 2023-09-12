package com.dabel.oculusbank.service.delegate;

import com.dabel.oculusbank.app.Generator;
import com.dabel.oculusbank.constant.AccountProfile;
import com.dabel.oculusbank.constant.AccountType;
import com.dabel.oculusbank.constant.Currency;
import com.dabel.oculusbank.constant.Status;
import com.dabel.oculusbank.dto.BranchDTO;
import com.dabel.oculusbank.dto.VaultDTO;
import com.dabel.oculusbank.service.AccountService;
import com.dabel.oculusbank.service.BranchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DelegateBranchService {

    @Autowired
    BranchService branchService;
    @Autowired
    AccountService accountService;

    public BranchDTO createWithOwnAccountsAtOnce(BranchDTO branchDTO) {

        branchDTO.setStatus(Status.Active.code());
        BranchDTO savedBranch = branchService.save(branchDTO);
        //TODO: save vault KMF
        VaultDTO vaultKMF = VaultDTO.builder()
                .branchId(savedBranch.getBranchId())
                .accountName(savedBranch.getBranchName())
                .accountNumber(Generator.generateAccountNumber())
                .accountType(AccountType.Business.name())
                .accountProfile(AccountProfile.Personal.name())
                .currency(Currency.KMF.name())
                .balance(0.0)
                .status(Status.Active.code())
                .build();
        accountService.saveVault(vaultKMF);

        //TODO: save vault EUR
        VaultDTO vaultEUR = VaultDTO.builder()
                .branchId(savedBranch.getBranchId())
                .accountName(savedBranch.getBranchName())
                .accountNumber(Generator.generateAccountNumber())
                .accountType(AccountType.Business.name())
                .accountProfile(AccountProfile.Personal.name())
                .currency(Currency.EUR.name())
                .balance(0.0)
                .status(Status.Active.code())
                .build();
        accountService.saveVault(vaultEUR);

        //TODO: save vault USD
        VaultDTO vaultUSD = VaultDTO.builder()
                .branchId(savedBranch.getBranchId())
                .accountName(savedBranch.getBranchName())
                .accountNumber(Generator.generateAccountNumber())
                .accountType(AccountType.Business.name())
                .accountProfile(AccountProfile.Personal.name())
                .currency(Currency.USD.name())
                .balance(0.0)
                .status(Status.Active.code())
                .build();
        accountService.saveVault(vaultUSD);

        return savedBranch;
    }
}
