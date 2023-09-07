package com.dabel.oculusbank.service;

import com.dabel.oculusbank.DatabaseSettingsForTests;
import com.dabel.oculusbank.constant.*;
import com.dabel.oculusbank.dto.AccountDTO;
import com.dabel.oculusbank.dto.TransactionDTO;
import com.dabel.oculusbank.exception.BalanceInsufficientException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
class TransactionOperationServiceTest {

    @Autowired
    TransactionOperationService basicTransactionOperationService;
    @Autowired
    AccountService accountService;
    @Autowired
    TransactionService transactionService;
    @Autowired
    DatabaseSettingsForTests databaseSettingsForTests;

    private AccountDTO savedAccount;

    @BeforeEach
    void init() {
        databaseSettingsForTests.truncate();
        savedAccount = accountService.save(
                AccountDTO.builder()
                .accountName("John Doe")
                .accountNumber("123456789")
                .balance(1500)
                .currency(Currency.KMF.name())
                .accountType(AccountType.Current.name())
                .status(Status.Pending.code())
                .build());
    }

    @Test
    void shouldMakeDeposit() {
        //GIVEN
        DepositParams depositParams = getDepositParams();

        //WHEN
        TransactionDTO expected = basicTransactionOperationService.deposit(depositParams.accountNumber(), depositParams.amount(), depositParams.currency(), depositParams.sourceType(), depositParams.sourceValue(), depositParams.reason());

        //THEN
        assertThat(expected.getTransactionId()).isGreaterThan(0);
        assertThat(expected.getTransactionType()).isEqualTo(TransactionType.Deposit.name());
        assertThat(expected.getStatus()).isEqualTo(Status.Pending.code());
    }

    @Test
    void shouldApprovePendingSavedDeposit() {
        //GIVEN
        DepositParams depositParams = getDepositParams();
        TransactionDTO savedTransaction = basicTransactionOperationService.deposit(depositParams.accountNumber(), depositParams.amount(), depositParams.currency(), depositParams.sourceType(), depositParams.sourceValue(), depositParams.reason());

        //WHEN
        TransactionDTO expected = basicTransactionOperationService.approve(savedTransaction.getTransactionId());

        //THEN
        assertThat(expected.getStatus()).isEqualTo(Status.Approved.code());

        AccountDTO expectedAccount = accountService.findByNumber(depositParams.accountNumber());
        assertThat(expectedAccount.getBalance()).isEqualTo(2000);
    }

    @Test
    void shouldRejectPendingSavedDeposit() {
        //GIVEN
        DepositParams depositParams = getDepositParams();
        TransactionDTO savedTransaction = basicTransactionOperationService.deposit(depositParams.accountNumber(), depositParams.amount(), depositParams.currency(), depositParams.sourceType(), depositParams.sourceValue(), depositParams.reason());

        //WHEN
        TransactionDTO expected =  basicTransactionOperationService.reject(savedTransaction.getTransactionId(), "Sample remark");

        //THEN
        assertThat(expected.getStatus()).isEqualTo(Status.Rejected.code());

        AccountDTO expectedAccount = accountService.findByNumber(depositParams.accountNumber());
        assertThat(expectedAccount.getBalance()).isEqualTo(1500);
    }

    @Test
    void shouldMakeWithdraw() {
        //GIVEN
        WithdrawParams result = getWithdrawParams();

        //WHEN
        TransactionDTO expected = basicTransactionOperationService.withdraw(result.accountNumber(), result.amount(), result.sourceType(), result.sourceValue(), result.reason());

        //THEN
        assertThat(expected.getTransactionId()).isGreaterThan(0);
        assertThat(expected.getTransactionType()).isEqualTo(TransactionType.Withdraw.name());
        assertThat(expected.getStatus()).isEqualTo(Status.Pending.code());
    }

    @Test
    void shouldApprovePendingSavedWithdraw() {
        //GIVEN
        WithdrawParams withdrawParams = getWithdrawParams();
        TransactionDTO savedTransaction = basicTransactionOperationService.withdraw(withdrawParams.accountNumber(), withdrawParams.amount(), withdrawParams.sourceType(), withdrawParams.sourceValue(), withdrawParams.reason());

        //WHEN
        TransactionDTO expected = basicTransactionOperationService.approve(savedTransaction.getTransactionId());

        //THEN
        assertThat(expected.getStatus()).isEqualTo(Status.Approved.code());

        AccountDTO expectedAccount = accountService.findByNumber(withdrawParams.accountNumber());
        assertThat(expectedAccount.getBalance()).isEqualTo(1000);
    }

    @Test
    void shouldRejectPendingSavedWithdraw() {
        //GIVEN
        WithdrawParams withdrawParams = getWithdrawParams();
        TransactionDTO savedTransaction = basicTransactionOperationService.withdraw(withdrawParams.accountNumber(), withdrawParams.amount(), withdrawParams.sourceType(), withdrawParams.sourceValue(), withdrawParams.reason());

        //WHEN
        TransactionDTO expected = basicTransactionOperationService.reject(savedTransaction.getTransactionId(), "Sample remark");

        //THEN
        assertThat(expected.getStatus()).isEqualTo(Status.Rejected.code());

        AccountDTO expectedAccount = accountService.findByNumber(withdrawParams.accountNumber());
        assertThat(expectedAccount.getBalance()).isEqualTo(1500);
    }

    @Test
    void shouldThrowABalanceInsufficientExceptionWhenTryWithdrawMoreAmountThanActualBalance() {
        //GIVEN
        WithdrawParams withdrawParams = getWithdrawParams();

        //WHEN
        Exception expected = assertThrows(BalanceInsufficientException.class,
                () -> basicTransactionOperationService.withdraw(withdrawParams.accountNumber(), 2000, withdrawParams.sourceType(), withdrawParams.sourceValue(), withdrawParams.reason()));

        //THEN
        TransactionDTO expectedTransaction = transactionService.findAll().get(0);
        assertThat(expectedTransaction.getStatus()).isEqualTo(Status.Failed.name());
        assertThat(expected.getMessage()).isEqualTo("Account balance is insufficient");
    }

    @Test
    void shouldCreditConvertAmountWhenTryMakeADepositWithEurToAnAccountWithKMFCurrency() {
        //GIVEN
        DepositParams depositParams = getDepositParams();
        TransactionDTO savedTransaction = basicTransactionOperationService.deposit(depositParams.accountNumber(), 20, Currency.EUR.name(), depositParams.sourceType(), depositParams.sourceValue(), depositParams.reason());
        basicTransactionOperationService.approve(savedTransaction.getTransactionId());

        //WHEN
        AccountDTO expected = accountService.findByNumber(depositParams.accountNumber());

        //THEN
        //1£ = 490.31KMF => 20£ = 9806.2KMF
        assertThat(expected.getBalance()).isEqualTo(11306.2);
    }

    @Test
    void shouldCreditConvertAmountWhenTryMakeADepositWithUsdToAnAccountWithKMFCurrency() {
        //GIVEN
        DepositParams depositParams = getDepositParams();
        TransactionDTO savedTransaction = basicTransactionOperationService.deposit(depositParams.accountNumber(), 20, Currency.USD.name(), depositParams.sourceType(), depositParams.sourceValue(), depositParams.reason());
        basicTransactionOperationService.approve(savedTransaction.getTransactionId());

        //WHEN
        AccountDTO expected = accountService.findByNumber(depositParams.accountNumber());

        //THEN
        //1$ = 456.51KMF => 20$ = 9130.2KMF
        assertThat(expected.getBalance()).isEqualTo(10630.2);
    }

    private record WithdrawParams(String accountNumber, double amount, String sourceType, String sourceValue, String reason) {
    }
    private record DepositParams(String accountNumber, double amount, String currency, String sourceType, String sourceValue, String reason) {

    }

    private WithdrawParams getWithdrawParams() {
        return new WithdrawParams(
                savedAccount.getAccountNumber(),
                500,
                SourceType.Online.name(),
                "Branch 1",
                "Sample description");
    }

    private DepositParams getDepositParams() {
        return new DepositParams(
                savedAccount.getAccountNumber(),
                500,
                Currency.KMF.name(),
                SourceType.Online.name(),
                "Branch 1",
                "Sample description");
    }
}