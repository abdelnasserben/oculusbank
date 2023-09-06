package com.dabel.oculusbank.service;

import com.dabel.oculusbank.DatabaseSettingsForTests;
import com.dabel.oculusbank.constant.AccountMemberShip;
import com.dabel.oculusbank.constant.AccountType;
import com.dabel.oculusbank.constant.Status;
import com.dabel.oculusbank.dto.*;
import com.dabel.oculusbank.exception.AccountNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
public class AccountServiceTest {

    @Autowired
    AccountService accountService;
    @Autowired
    DatabaseSettingsForTests databaseSettingsForTests;

    @Autowired
    BranchService branchService;
    @Autowired
    CustomerService customerService;

    private BranchDTO getSavedBranch() {
        return branchService.save(
                BranchDTO.builder()
                        .branchName("HQ")
                        .branchAddress("Moroni")
                        .status(Status.Active.code())
                        .build());
    }

    private CustomerDTO getSavedCustomer() {

        BranchDTO savedBranch = getSavedBranch();

        return customerService.save(
                CustomerDTO.builder()
                        .branchId(savedBranch.getBranchId())
                        .firstName("John")
                        .lastName("Doe")
                        .identityNumber("NBE466754")
                        .status(Status.Pending.code())
                        .build());
    }

    @BeforeEach
    void init() {
        databaseSettingsForTests.truncate();
    }


    @Test
    void shouldSaveNewAccount() {
        //GIVEN
        AccountDTO accountDTO = AccountDTO.builder()
                .accountName("John Doe")
                .accountNumber("123456789")
                .accountType(AccountType.Current.name())
                .status(Status.Pending.code())
                .build();

        //WHEN
        AccountDTO expected = accountService.save(accountDTO);

        //THEN
        assertThat(expected.getAccountId()).isGreaterThan(0);
    }

    @Test
    void shouldSaveNewVault() {
        //GIVEN
        BranchDTO savedBranch = getSavedBranch();

        VaultDTO vaultDTO = VaultDTO.builder()
                .accountName("Branch 2")
                .accountNumber("123456789")
                .accountType(AccountType.Business.name())
                .status(Status.Pending.code())
                .branchId(savedBranch.getBranchId())
                .build();

        //WHEN
        VaultDTO expected = accountService.saveVault(vaultDTO);

        //THEN
        assertThat(expected.getVaultId()).isGreaterThan(0);
        assertThat(expected.getBranchId()).isEqualTo(savedBranch.getBranchId());
    }

    @Test
    void shouldRetrieveListOfVaultsByBranchId() {
        //GIVEN
        BranchDTO savedBranch = getSavedBranch();

        VaultDTO savedVault = accountService.saveVault(
                VaultDTO.builder()
                        .accountName("Branch 2")
                        .accountNumber("123456789")
                        .accountType(AccountType.Business.name())
                        .status(Status.Pending.code())
                        .branchId(savedBranch.getBranchId())
                        .build());

        //WHEN
        List<VaultDTO> expected = accountService.findAllVaultsByBranchId(savedVault.getBranchId());

        //THEN
        assertThat(expected.size()).isEqualTo(1);
        assertThat(expected.get(0).getBranchId()).isEqualTo(savedVault.getBranchId());
        assertThat(expected.get(0).getStatus()).isEqualTo(Status.Pending.name());
    }

    @Test
    void shouldFindVaultByBranchId() {
        //GIVEN
        BranchDTO savedBranch = getSavedBranch();

        VaultDTO savedVault = accountService.saveVault(
                VaultDTO.builder()
                        .accountName("Branch 2")
                        .accountNumber("123456789")
                        .accountType(AccountType.Business.name())
                        .status(Status.Pending.code())
                        .branchId(savedBranch.getBranchId())
                        .build());

        //WHEN
        VaultDTO expected = accountService.findVaultByBranchId(savedVault.getBranchId());

        //THEN
        assertThat(expected.getBranchId()).isEqualTo(savedVault.getBranchId());
        assertThat(expected.getStatus()).isEqualTo(Status.Pending.name());
    }

    @Test
    void shouldThrowAnAccountNotFoundExceptionWhenTryFindVaultByANotExistsBranchId() {
        //GIVEN

        //WHEN
        Exception expected = assertThrows(AccountNotFoundException.class,
                () -> accountService.findVaultByBranchId(-12));

        //THEN
        assertThat(expected.getMessage()).isEqualTo("Account not found");
    }

    @Test
    void shouldSaveNewTrunk() {
        //GIVEN
        CustomerDTO savedCustomer = getSavedCustomer();

        TrunkDTO trunkDTO =  TrunkDTO.builder()
                .accountName("John Doe")
                .accountNumber("123456789")
                .accountType(AccountType.Business.name())
                .status(Status.Pending.code())
                .customerId(savedCustomer.getCustomerId())
                .membership(AccountMemberShip.Owner.name())
                .build();

        //WHEN
        TrunkDTO expected = accountService.saveTrunk(trunkDTO);

        //THEN
        assertThat(expected.getTrunkId()).isGreaterThan(0);
        assertThat(expected.getCustomerId()).isEqualTo(savedCustomer.getCustomerId());
    }

    @Test
    void shouldRetrieveListOfTrunksByCustomerId() {
        //GIVEN
        CustomerDTO savedCustomer = getSavedCustomer();

        TrunkDTO savedTrunk = accountService.saveTrunk(
                TrunkDTO.builder()
                .accountName("John Doe")
                .accountNumber("123456789")
                .accountType(AccountType.Business.name())
                .status(Status.Pending.code())
                .customerId(savedCustomer.getCustomerId())
                .membership(AccountMemberShip.Owner.name())
                .build());

        //WHEN
        List<TrunkDTO> expected = accountService.findAllTrunksByCustomerId(savedTrunk.getCustomerId());

        //THEN
        assertThat(expected.size()).isEqualTo(1);
        assertThat(expected.get(0).getCustomerId()).isEqualTo(savedCustomer.getCustomerId());
        assertThat(expected.get(0).getStatus()).isEqualTo(Status.Pending.name());
    }

    @Test
    void shouldFindTrunkByCustomerId() {
        //GIVEN
        CustomerDTO savedCustomer = getSavedCustomer();

        TrunkDTO savedTrunk = accountService.saveTrunk(
                TrunkDTO.builder()
                        .accountName("John Doe")
                        .accountNumber("123456789")
                        .accountType(AccountType.Business.name())
                        .status(Status.Pending.code())
                        .customerId(savedCustomer.getCustomerId())
                        .membership(AccountMemberShip.Owner.name())
                        .build());

        //WHEN
        TrunkDTO expected = accountService.findTrunkByCustomerId(savedTrunk.getCustomerId());

        //THEN
        assertThat(expected.getCustomerId()).isEqualTo(savedCustomer.getCustomerId());
        assertThat(expected.getStatus()).isEqualTo(Status.Pending.name());
    }

    @Test
    void shouldThrowAnAccountNotFoundExceptionWhenTryFindTrunkByANotExistsCustomerId() {
        //GIVEN

        //WHEN
        Exception expected = assertThrows(AccountNotFoundException.class,
                () -> accountService.findTrunkByCustomerId(-12));

        //THEN
        assertThat(expected.getMessage()).isEqualTo("Account not found");
    }
}
