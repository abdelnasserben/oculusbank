package com.dabel.oculusbank.service;

import com.dabel.oculusbank.DatabaseSettingsForTests;
import com.dabel.oculusbank.constant.*;
import com.dabel.oculusbank.dto.AccountDTO;
import com.dabel.oculusbank.dto.TransactionDTO;
import com.dabel.oculusbank.exception.BalanceInsufficientException;
import com.dabel.oculusbank.service.delegate.DelegateTransactionService;
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
        TransactionDTO transactionDTO = TransactionDTO.builder()
                .accountNumber(savedAccount.getAccountNumber())
                .amount(500)
                .currency(Currency.KMF.name())
                .sourceType(SourceType.Online.name())
                .sourceValue("Branch 1")
                .reason("Sample description")
                .build();

        //WHEN
        TransactionDTO expected = basicDelegateTransactionService.deposit(transactionDTO);

        //THEN
        assertThat(expected.getTransactionId()).isGreaterThan(0);
        assertThat(expected.getTransactionType()).isEqualTo(TransactionType.Deposit.name());
        assertThat(expected.getStatus()).isEqualTo(Status.Pending.code());
    }

    @Test
    void shouldApprovePendingSavedDeposit() {
        //GIVEN
        TransactionDTO transactionDTO = TransactionDTO.builder()
                .accountNumber(savedAccount.getAccountNumber())
                .amount(500)
                .currency(Currency.KMF.name())
                .sourceType(SourceType.Online.name())
                .sourceValue("Branch 1")
                .reason("Sample description")
                .build();
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
        TransactionDTO transactionDTO = TransactionDTO.builder()
                .accountNumber(savedAccount.getAccountNumber())
                .amount(500)
                .currency(Currency.KMF.name())
                .sourceType(SourceType.Online.name())
                .sourceValue("Branch 1")
                .reason("Sample description")
                .build();
        TransactionDTO savedTransaction = basicDelegateTransactionService.deposit(transactionDTO);

        //WHEN
        TransactionDTO expected =  basicDelegateTransactionService.reject(savedTransaction.getTransactionId(), "Sample remark");

        //THEN
        assertThat(expected.getStatus()).isEqualTo(Status.Rejected.code());

        AccountDTO expectedAccount = accountService.findByNumber(savedAccount.getAccountNumber());
        assertThat(expectedAccount.getBalance()).isEqualTo(1500);
    }

    @Test
    void shouldMakeWithdraw() {
        //GIVEN
        TransactionDTO transactionDTO = TransactionDTO.builder()
                .accountNumber(savedAccount.getAccountNumber())
                .amount(500)
                .currency(Currency.KMF.name())
                .sourceType(SourceType.Online.name())
                .sourceValue("Branch 1")
                .reason("Sample description")
                .build();
        //WHEN
        TransactionDTO expected = basicDelegateTransactionService.withdraw(transactionDTO);

        //THEN
        assertThat(expected.getTransactionId()).isGreaterThan(0);
        assertThat(expected.getTransactionType()).isEqualTo(TransactionType.Withdraw.name());
        assertThat(expected.getStatus()).isEqualTo(Status.Pending.code());
    }

    @Test
    void shouldApprovePendingSavedWithdraw() {
        //GIVEN
        TransactionDTO transactionDTO = TransactionDTO.builder()
                .accountNumber(savedAccount.getAccountNumber())
                .amount(500)
                .currency(Currency.KMF.name())
                .sourceType(SourceType.Online.name())
                .sourceValue("Branch 1")
                .reason("Sample description")
                .build();
        TransactionDTO savedTransaction = basicDelegateTransactionService.withdraw(transactionDTO);

        //WHEN
        TransactionDTO expected = basicDelegateTransactionService.approve(savedTransaction.getTransactionId());

        //THEN
        assertThat(expected.getStatus()).isEqualTo(Status.Approved.code());

        AccountDTO expectedAccount = accountService.findByNumber(savedAccount.getAccountNumber());
        assertThat(expectedAccount.getBalance()).isEqualTo(1000);
    }

    @Test
    void shouldRejectPendingSavedWithdraw() {
        //GIVEN
        TransactionDTO transactionDTO = TransactionDTO.builder()
                .accountNumber(savedAccount.getAccountNumber())
                .amount(500)
                .currency(Currency.KMF.name())
                .sourceType(SourceType.Online.name())
                .sourceValue("Branch 1")
                .reason("Sample description")
                .build();
        TransactionDTO savedTransaction = basicDelegateTransactionService.withdraw(transactionDTO);

        //WHEN
        TransactionDTO expected = basicDelegateTransactionService.reject(savedTransaction.getTransactionId(), "Sample remark");

        //THEN
        assertThat(expected.getStatus()).isEqualTo(Status.Rejected.code());

        AccountDTO expectedAccount = accountService.findByNumber(savedAccount.getAccountNumber());
        assertThat(expectedAccount.getBalance()).isEqualTo(1500);
    }

    @Test
    void shouldThrowABalanceInsufficientExceptionWhenTryWithdrawMoreAmountThanActualBalance() {
        //GIVEN
        TransactionDTO transactionDTO = TransactionDTO.builder()
                .accountNumber(savedAccount.getAccountNumber())
                .amount(5000)
                .currency(Currency.KMF.name())
                .sourceType(SourceType.Online.name())
                .sourceValue("Branch 1")
                .reason("Sample description")
                .build();

        //WHEN
        Exception expected = assertThrows(BalanceInsufficientException.class,
                () -> basicDelegateTransactionService.withdraw(transactionDTO));

        //THEN
        TransactionDTO expectedTransaction = transactionService.findAll().get(0);
        assertThat(expectedTransaction.getStatus()).isEqualTo(Status.Failed.name());
        assertThat(expected.getMessage()).isEqualTo("Account balance is insufficient");
    }

    @Test
    void shouldCreditConvertAmountWhenTryMakeADepositWithEurToAnAccountWithKMFCurrency() {
        //GIVEN
        TransactionDTO transactionDTO = TransactionDTO.builder()
                .accountNumber(savedAccount.getAccountNumber())
                .amount(20)
                .currency(Currency.EUR.name())
                .sourceType(SourceType.Online.name())
                .sourceValue("Branch 1")
                .reason("Sample description")
                .build();
        TransactionDTO savedTransaction = basicDelegateTransactionService.deposit(transactionDTO);
        basicDelegateTransactionService.approve(savedTransaction.getTransactionId());

        //WHEN
        AccountDTO expected = accountService.findByNumber(savedAccount.getAccountNumber());

        //THEN
        //1£ = 490.31KMF => 20£ = 9806.2KMF
        assertThat(expected.getBalance()).isEqualTo(11306.2);
    }

    @Test
    void shouldCreditConvertAmountWhenTryMakeADepositWithUsdToAnAccountWithKMFCurrency() {
        //GIVEN
        TransactionDTO transactionDTO = TransactionDTO.builder()
                .accountNumber(savedAccount.getAccountNumber())
                .amount(20)
                .currency(Currency.USD.name())
                .sourceType(SourceType.Online.name())
                .sourceValue("Branch 1")
                .reason("Sample description")
                .build();
        TransactionDTO savedTransaction = basicDelegateTransactionService.deposit(transactionDTO);
        basicDelegateTransactionService.approve(savedTransaction.getTransactionId());

        //WHEN
        AccountDTO expected = accountService.findByNumber(savedAccount.getAccountNumber());

        //THEN
        //1$ = 456.51KMF => 20$ = 9130.2KMF
        assertThat(expected.getBalance()).isEqualTo(10630.2);
    }
}