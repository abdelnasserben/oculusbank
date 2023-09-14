package com.dabel.oculusbank.service.delegate;

import com.dabel.oculusbank.DatabaseSettingsForTests;
import com.dabel.oculusbank.constant.*;
import com.dabel.oculusbank.dto.*;
import com.dabel.oculusbank.exception.BalanceInsufficientException;
import com.dabel.oculusbank.exception.IllegalOperationException;
import com.dabel.oculusbank.service.AccountService;
import com.dabel.oculusbank.service.BranchService;
import com.dabel.oculusbank.service.CustomerService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
class DelegateCardAppRequestRequestServiceTest {

    @Autowired
    DelegateCardAppService delegateCardAppService;
    @Autowired
    BranchService branchService;
    @Autowired
    CustomerService customerService;
    @Autowired
    AccountService accountService;
    @Autowired
    DatabaseSettingsForTests databaseSettingsForTests;

    @BeforeEach
    void init() {
        databaseSettingsForTests.truncate();
    }

    @Test
    void shouldSendCardApplicationRequestAndMakeItPending() {
        //GIVEN
        AccountDTO savedAccount = getSavedTrunk();

        CardAppRequestDTO cardAppRequestDTO = CardAppRequestDTO.builder()
                .cardType(CardType.Visa.name())
                .accountId(savedAccount.getAccountId())
                .accountNumber(savedAccount.getAccountNumber())
                .build();

        //WHEN
        CardAppRequestDTO expected = delegateCardAppService.sendRequest(cardAppRequestDTO);

        //THEN
        assertThat(expected.getRequestId()).isGreaterThan(0);
        assertThat(expected.getStatus()).isEqualTo(Status.Pending.code());
    }

    @Test
    void shouldApproveACardApplicationRequest() {
        //GIVEN
        AccountDTO savedAccount = getSavedTrunk();

        CardAppRequestDTO cardAppRequestDTO = CardAppRequestDTO.builder()
                .cardType(CardType.Visa.name())
                .accountId(savedAccount.getAccountId())
                .accountNumber(savedAccount.getAccountNumber())
                .build();
        CardAppRequestDTO savedRequestApp = delegateCardAppService.sendRequest(cardAppRequestDTO);

        //WHEN
        CardAppRequestDTO expected = delegateCardAppService.approve(savedRequestApp.getRequestId());

        //THEN
        assertThat(expected.getStatus()).isEqualTo(Status.Approved.code());

        AccountDTO expectedAccount = accountService.findByNumber(savedAccount.getAccountNumber());
        assertThat(expectedAccount.getBalance()).isEqualTo(20000);
    }

    @Test
    void shouldRejectACardApplicationRequest() {
        //GIVEN
        AccountDTO savedAccount = getSavedTrunk();

        CardAppRequestDTO cardAppRequestDTO = CardAppRequestDTO.builder()
                .cardType(CardType.Visa.name())
                .accountId(savedAccount.getAccountId())
                .accountNumber(savedAccount.getAccountNumber())
                .build();
        CardAppRequestDTO savedRequestApp = delegateCardAppService.sendRequest(cardAppRequestDTO);

        //WHEN
        CardAppRequestDTO expected = delegateCardAppService.reject(savedRequestApp.getRequestId(), "Sample remark");

        //THEN
        assertThat(expected.getStatus()).isEqualTo(Status.Rejected.code());
    }

    @Test
    void shouldThrowAnIllegalOperationExceptionWhenTrySendRequestOnAnInActiveAccount() {
        //GIVEN
        AccountDTO savedAccount = getSavedTrunk();
        savedAccount.setStatus(Status.Pending.code());
        accountService.save(savedAccount);

        CardAppRequestDTO cardAppRequestDTO = CardAppRequestDTO.builder()
                .cardType(CardType.Visa.name())
                .accountId(savedAccount.getAccountId())
                .accountNumber(savedAccount.getAccountNumber())
                .build();

        //WHEN
        Exception expected = assertThrows(IllegalOperationException.class,
                () -> delegateCardAppService.sendRequest(cardAppRequestDTO));

        //THEN
        assertThat(expected.getMessage()).isEqualTo("The account is not eligible for this operation");
    }

    @Test
    void shouldThrowAnIllegalOperationExceptionWhenTrySendRequestOnAnAssociativeAccount() {
        //GIVEN
        AccountDTO savedAccount = getSavedTrunk();
        savedAccount.setStatus(Status.Pending.code());
        savedAccount.setAccountProfile(AccountProfile.Associative.name());
        accountService.save(savedAccount);

        CardAppRequestDTO cardAppRequestDTO = CardAppRequestDTO.builder()
                .cardType(CardType.Visa.name())
                .accountId(savedAccount.getAccountId())
                .accountNumber(savedAccount.getAccountNumber())
                .build();

        //WHEN
        Exception expected = assertThrows(IllegalOperationException.class,
                () -> delegateCardAppService.sendRequest(cardAppRequestDTO));

        //THEN
        assertThat(expected.getMessage()).isEqualTo("The account is not eligible for this operation");
    }

    @Test
    void shouldThrowABalanceInsufficientExceptionWhenTrySendRequestOnAnAccountWithBalanceIsLessThanCardApplicationFees() {
        //GIVEN
        AccountDTO savedAccount = getSavedTrunk();
        savedAccount.setStatus(Status.Active.code());
        savedAccount.setBalance(100);
        accountService.save(savedAccount);

        CardAppRequestDTO cardAppRequestDTO = CardAppRequestDTO.builder()
                .cardType(CardType.Visa.name())
                .accountId(savedAccount.getAccountId())
                .accountNumber(savedAccount.getAccountNumber())
                .build();

        //WHEN
        Exception expected = assertThrows(BalanceInsufficientException.class,
                () -> delegateCardAppService.sendRequest(cardAppRequestDTO));

        //THEN
        assertThat(expected.getMessage()).isEqualTo("Account balance is insufficient for application fees");
    }

    @Test
    void approve() {
    }

    @Test
    void reject() {
    }

    private TrunkDTO getSavedTrunk() {
        BranchDTO savedBranch = branchService.save(BranchDTO.builder()
                .branchName("HQ")
                .branchAddress("Moroni")
                .status(Status.Active.code())
                .build());

        CustomerDTO savedCustomer = customerService.save(CustomerDTO.builder()
                .branchId(savedBranch.getBranchId())
                .firstName("John")
                .lastName("Doe")
                .identityNumber("NBE466754")
                .status(Status.Active.code())
                .build());

        AccountDTO savedAccount = accountService.save(AccountDTO.builder()
                .accountName("John Doe")
                .accountNumber("123456789")
                .accountType(AccountType.Saving.name())
                .accountProfile(AccountProfile.Personal.name())
                .status(Status.Active.code())
                .balance(25000)
                .build());

        accountService.saveTrunk(savedAccount.getAccountId(), savedCustomer.getCustomerId(), AccountMemberShip.Owner.name());

        return accountService.findTrunkByCustomerId(savedCustomer.getCustomerId());
    }
}