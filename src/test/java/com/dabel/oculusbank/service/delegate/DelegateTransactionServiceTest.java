package com.dabel.oculusbank.service.delegate;

import com.dabel.oculusbank.DatabaseSettingsForTests;
import com.dabel.oculusbank.constant.*;
import com.dabel.oculusbank.dto.AccountDTO;
import com.dabel.oculusbank.dto.TransactionDTO;
import com.dabel.oculusbank.exception.BalanceInsufficientException;
import com.dabel.oculusbank.exception.IllegalOperationException;
import com.dabel.oculusbank.service.AccountService;
import com.dabel.oculusbank.service.TransactionService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
class DelegateTransactionServiceTest {

    @Autowired
    DelegateTransactionService basicDelegateTransactionService;
    @Autowired
    AccountService accountService;
    @Autowired
    TransactionService transactionService;
    @Autowired
    DatabaseSettingsForTests databaseSettingsForTests;

    private AccountDTO savedAccount;
    private TransactionDTO transactionDTO;

    private void configSavedAccountAndTransactionDTO(boolean isActive, String transactionSourceType, double transactionAmount, String transactionCurrency) {

        String accountStatus = isActive ? Status.Active.code() : Status.Pending.code();

        savedAccount = accountService.save(
                AccountDTO.builder()
                        .accountName("John Doe")
                        .accountNumber("123456789")
                        .balance(1500)
                        .currency(Currency.KMF.name())
                        .accountType(AccountType.Saving.name())
                        .status(accountStatus)
                        .build());

        transactionDTO = TransactionDTO.builder()
                .accountNumber(savedAccount.getAccountNumber())
                .amount(transactionAmount)
                .currency(transactionCurrency)
                .sourceType(transactionSourceType)
                .sourceValue("Branch 1")
                .reason("Sample description")
                .build();
    }

    @BeforeEach
    void init() {
        databaseSettingsForTests.truncate();
    }

    @Test
    void shouldInitDepositAndPendingItOnActiveAccount() {
        //GIVEN
        configSavedAccountAndTransactionDTO(true, SourceType.Online.name(), 500, Currency.KMF.name());

        //WHEN
        TransactionDTO expected = basicDelegateTransactionService.deposit(transactionDTO);

        //THEN
        assertThat(expected.getTransactionId()).isGreaterThan(0);
        assertThat(expected.getTransactionType()).isEqualTo(TransactionType.Deposit.name());
        assertThat(expected.getStatus()).isEqualTo(Status.Pending.code());
    }

    @Test
    void shouldThrowAnIllegalOperationExceptionWhenTryInitDepositOnAnInactiveAccount() {
        //GIVEN
        configSavedAccountAndTransactionDTO(false, SourceType.Online.name(), 500, Currency.KMF.name());

        //WHEN
        Exception expected = assertThrows(IllegalOperationException.class,
                () -> basicDelegateTransactionService.deposit(transactionDTO));

        //THEN
        assertThat(expected.getMessage()).isEqualTo("Account must be active");
    }

    @Test
    void shouldApprovePendingSavedDeposit() {
        //GIVEN
        configSavedAccountAndTransactionDTO(true, SourceType.Online.name(), 500, Currency.KMF.name());
        TransactionDTO savedTransaction = basicDelegateTransactionService.deposit(transactionDTO);

        //WHEN
        TransactionDTO expected = basicDelegateTransactionService.approve(savedTransaction.getTransactionId());

        //THEN
        assertThat(expected.getStatus()).isEqualTo(Status.Approved.code());

        AccountDTO expectedAccount = accountService.findByNumber(savedAccount.getAccountNumber());
        assertThat(expectedAccount.getBalance()).isEqualTo(2000);
    }

    @Test
    void shouldRejectPendingSavedDeposit() {
        //GIVEN
        configSavedAccountAndTransactionDTO(true, SourceType.Online.name(), 500, Currency.KMF.name());
        TransactionDTO savedTransaction = basicDelegateTransactionService.deposit(transactionDTO);

        //WHEN
        TransactionDTO expected =  basicDelegateTransactionService.reject(savedTransaction.getTransactionId(), "Sample remark");

        //THEN
        assertThat(expected.getStatus()).isEqualTo(Status.Rejected.code());

        AccountDTO expectedAccount = accountService.findByNumber(savedAccount.getAccountNumber());
        assertThat(expectedAccount.getBalance()).isEqualTo(1500);
    }

    @Test
    void shouldConvertDepositAmountWhenTryDepositEurOnKmfAccount() {
        //GIVEN
        configSavedAccountAndTransactionDTO(true, SourceType.Online.name(), 20, Currency.EUR.name());
        TransactionDTO savedTransaction = basicDelegateTransactionService.deposit(transactionDTO);
        basicDelegateTransactionService.approve(savedTransaction.getTransactionId());

        //WHEN
        AccountDTO expected = accountService.findByNumber(savedAccount.getAccountNumber());

        //THEN
        //1£ = 490.31KMF => 20£ = 9806.2KMF
        assertThat(expected.getBalance()).isEqualTo(11306.2);
    }

    @Test
    void shouldConvertDepositAmountWhenTryDepositUsdOnKmfAccount() {
        //GIVEN
        configSavedAccountAndTransactionDTO(true, SourceType.Online.name(), 20, Currency.USD.name());
        TransactionDTO savedTransaction = basicDelegateTransactionService.deposit(transactionDTO);
        basicDelegateTransactionService.approve(savedTransaction.getTransactionId());

        //WHEN
        AccountDTO expected = accountService.findByNumber(savedAccount.getAccountNumber());

        //THEN
        //1$ = 456.51KMF => 20$ = 9130.2KMF
        assertThat(expected.getBalance()).isEqualTo(10630.2);
    }

    @Test
    void shouldInitWithdrawAndPendingItOnActiveAccount() {
        //GIVEN
        configSavedAccountAndTransactionDTO(true, SourceType.Online.name(), 500, Currency.KMF.name());

        //WHEN
        TransactionDTO expected = basicDelegateTransactionService.withdraw(transactionDTO);

        //THEN
        assertThat(expected.getTransactionId()).isGreaterThan(0);
        assertThat(expected.getTransactionType()).isEqualTo(TransactionType.Withdraw.name());
        assertThat(expected.getStatus()).isEqualTo(Status.Pending.code());
    }

    @Test
    void shouldThrowAnIllegalOperationExceptionWhenTryInitWithdrawOnAnInactiveAccount() {
        //GIVEN
        configSavedAccountAndTransactionDTO(false, SourceType.Online.name(), 500, Currency.KMF.name());

        //WHEN
        Exception expected = assertThrows(IllegalOperationException.class,
                () -> basicDelegateTransactionService.withdraw(transactionDTO));

        //THEN
        assertThat(expected.getMessage()).isEqualTo("Account must be active");
    }

    @Test
    void shouldApprovePendingSavedWithdrawDoneInAgency() {
        //GIVEN
        configSavedAccountAndTransactionDTO(true, SourceType.Online.name(), 500, Currency.KMF.name());
        TransactionDTO savedTransaction = basicDelegateTransactionService.withdraw(transactionDTO);

        //WHEN
        TransactionDTO expected = basicDelegateTransactionService.approve(savedTransaction.getTransactionId());

        //THEN
        assertThat(expected.getStatus()).isEqualTo(Status.Approved.code());

        AccountDTO expectedAccount = accountService.findByNumber(savedAccount.getAccountNumber());
        assertThat(expectedAccount.getBalance()).isEqualTo(800); //in agency withdraw fees are 200KMF then we expect 800 but not 1000
    }

    @Test
    void shouldApprovePendingSavedWithdrawDoneInATM() {
        //GIVEN
        configSavedAccountAndTransactionDTO(true, SourceType.ATM.name(), 500, Currency.KMF.name());
        TransactionDTO savedTransaction = basicDelegateTransactionService.withdraw(transactionDTO);

        //WHEN
        TransactionDTO expected = basicDelegateTransactionService.approve(savedTransaction.getTransactionId());

        //THEN
        assertThat(expected.getStatus()).isEqualTo(Status.Approved.code());

        AccountDTO expectedAccount = accountService.findByNumber(savedAccount.getAccountNumber());
        assertThat(expectedAccount.getBalance()).isEqualTo(700); //in agency withdraw fees are 300KMF then we expect 700 but not 1000
    }

    @Test
    void shouldRejectPendingSavedWithdraw() {
        //GIVEN
        configSavedAccountAndTransactionDTO(true, SourceType.Online.name(), 500, Currency.KMF.name());
        TransactionDTO savedTransaction = basicDelegateTransactionService.withdraw(transactionDTO);

        //WHEN
        TransactionDTO expected = basicDelegateTransactionService.reject(savedTransaction.getTransactionId(), "Sample remark");

        //THEN
        assertThat(expected.getStatus()).isEqualTo(Status.Rejected.code());

        AccountDTO expectedAccount = accountService.findByNumber(savedAccount.getAccountNumber());
        assertThat(expectedAccount.getBalance()).isEqualTo(1500);
    }

    @Test
    void shouldThrowABalanceInsufficientExceptionWhenTryMakeAgencyWithdrawAndAccountBalanceIsLessThanTransactionAmountPlusWithdrawAgencyFees() {
        //GIVEN
        configSavedAccountAndTransactionDTO(true, SourceType.Online.name(), 1699, Currency.KMF.name());

        //WHEN
        Exception expected = assertThrows(BalanceInsufficientException.class,
                () -> basicDelegateTransactionService.withdraw(transactionDTO));

        //THEN
        TransactionDTO expectedTransaction = transactionService.findAll().get(0);
        assertThat(expectedTransaction.getStatus()).isEqualTo(Status.Failed.name());
        assertThat(expected.getMessage()).isEqualTo("Account balance is insufficient");
    }

    @Test
    void shouldThrowABalanceInsufficientExceptionWhenTryMakeWithdrawOnATMAndAccountBalanceIsLessThanTransactionAmountPlusWithdrawATMFees() {
        //GIVEN
        configSavedAccountAndTransactionDTO(true, SourceType.Online.name(), 1799, Currency.KMF.name());

        //WHEN
        Exception expected = assertThrows(BalanceInsufficientException.class,
                () -> basicDelegateTransactionService.withdraw(transactionDTO));

        //THEN
        TransactionDTO expectedTransaction = transactionService.findAll().get(0);
        assertThat(expectedTransaction.getStatus()).isEqualTo(Status.Failed.name());
        assertThat(expected.getMessage()).isEqualTo("Account balance is insufficient");
    }
}