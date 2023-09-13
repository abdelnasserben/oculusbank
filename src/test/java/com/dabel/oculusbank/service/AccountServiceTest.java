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
import static org.junit.jupiter.api.Assertions.assertTrue;

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
                .accountType(AccountType.Saving.name())
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

        AccountDTO savedAccount = accountService.save(
                AccountDTO.builder()
                .accountName("Branch 2")
                .accountNumber("123456789")
                .accountType(AccountType.Business.name())
                .status(Status.Pending.code())
                .build());

        //WHEN
        VaultDTO expected = accountService.saveVault(savedAccount.getAccountId(), savedBranch.getBranchId());

        //THEN
        assertThat(expected.getVaultId()).isGreaterThan(0);
        assertThat(expected.getBranchId()).isEqualTo(savedBranch.getBranchId());
    }

    @Test
    void shouldRetrieveListOfVaultsByBranchId() {
        //GIVEN
        BranchDTO savedBranch = getSavedBranch();
        AccountDTO savedAccount = accountService.save(
                AccountDTO.builder()
                        .accountName("Branch 2")
                        .accountNumber("123456789")
                        .accountType(AccountType.Business.name())
                        .status(Status.Pending.code())
                        .build());
        accountService.saveVault(savedAccount.getAccountId(), savedBranch.getBranchId());

        //WHEN
        List<VaultDTO> expected = accountService.findAllVaultsByBranchId(savedBranch.getBranchId());

        //THEN
        assertThat(expected.size()).isEqualTo(1);
        assertThat(expected.get(0).getBranchId()).isEqualTo(savedBranch.getBranchId());
        assertThat(expected.get(0).getStatus()).isEqualTo(Status.Pending.name());
    }

    @Test
    void shouldFindVaultByBranchId() {
        //GIVEN
        BranchDTO savedBranch = getSavedBranch();
        AccountDTO savedAccount = accountService.save(
                AccountDTO.builder()
                .accountName("Branch 2")
                 .accountNumber("123456789")
                 .accountType(AccountType.Business.name())
                 .status(Status.Pending.code())
                 .build());
        accountService.saveVault(savedAccount.getAccountId(), savedBranch.getBranchId());

        //WHEN
        VaultDTO expected = accountService.findVaultByBranchId(savedBranch.getBranchId());

        //THEN
        assertThat(expected.getBranchId()).isEqualTo(savedBranch.getBranchId());
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
        AccountDTO savedAccount = accountService.save(
                AccountDTO.builder()
                .accountName("John Doe")
                .accountNumber("123456789")
                .accountType(AccountType.Business.name())
                .status(Status.Pending.code())
                .build());

        //WHEN
        TrunkDTO expected = accountService.saveTrunk(savedAccount.getAccountId(), savedCustomer.getCustomerId(), AccountMemberShip.Owner.name());

        //THEN
        assertThat(expected.getTrunkId()).isGreaterThan(0);
        assertThat(expected.getCustomerId()).isEqualTo(savedCustomer.getCustomerId());
    }

    @Test
    void shouldRetrieveListOfTrunksByCustomerId() {
        //GIVEN
        CustomerDTO savedCustomer = getSavedCustomer();
        AccountDTO savedAccount = accountService.save(
                AccountDTO.builder()
                .accountName("John Doe")
                .accountNumber("123456789")
                .accountType(AccountType.Business.name())
                .status(Status.Pending.code())
                .build());
        accountService.saveTrunk(savedAccount.getAccountId(), savedCustomer.getCustomerId(), AccountMemberShip.Owner.name());

        //WHEN
        List<TrunkDTO> expected = accountService.findAllTrunksByCustomerId(savedCustomer.getCustomerId());

        //THEN
        assertThat(expected.size()).isEqualTo(1);
        assertThat(expected.get(0).getCustomerId()).isEqualTo(savedCustomer.getCustomerId());
        assertThat(expected.get(0).getStatus()).isEqualTo(Status.Pending.name());
    }

    @Test
    void shouldFindTrunkByCustomerId() {
        //GIVEN
        CustomerDTO savedCustomer = getSavedCustomer();
        AccountDTO savedAccount = accountService.save(
                AccountDTO.builder()
                .accountName("John Doe")
                .accountNumber("123456789")
                .accountType(AccountType.Business.name())
                .status(Status.Pending.code())
                .build());
        accountService.saveTrunk(savedAccount.getAccountId(), savedCustomer.getCustomerId(), AccountMemberShip.Owner.name());

        //WHEN
        TrunkDTO expected = accountService.findTrunkByCustomerId(savedCustomer.getCustomerId());

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

    @Test
    void shouldCheckIfAnAccountIsATrunkByAccountNumber() {
        //GIVEN
        CustomerDTO savedCustomer = getSavedCustomer();
        AccountDTO savedAccount = accountService.save(
                AccountDTO.builder()
                .accountName("John Doe")
                .accountNumber("123456789")
                .accountType(AccountType.Business.name())
                .status(Status.Pending.code())
                .build());
        accountService.saveTrunk(savedAccount.getAccountId(), savedCustomer.getCustomerId(), AccountMemberShip.Owner.name());

        //WHEN
        boolean expected = accountService.isTrunk(savedAccount.getAccountNumber());

        //THEN
        assertTrue(expected);
    }
}
