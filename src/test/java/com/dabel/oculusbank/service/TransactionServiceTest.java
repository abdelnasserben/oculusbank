package com.dabel.oculusbank.service;

import com.dabel.oculusbank.DatabaseSettingsForTests;
import com.dabel.oculusbank.constant.*;
import com.dabel.oculusbank.dto.AccountDTO;
import com.dabel.oculusbank.dto.TransactionDTO;
import com.dabel.oculusbank.exception.TransactionNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
public class TransactionServiceTest {

    @Autowired
    TransactionService transactionService;
    @Autowired
    AccountService accountService;
    @Autowired
    DatabaseSettingsForTests databaseSettingsForTests;

    private AccountDTO savedAccount;

    private void setSavedAccount() {
        savedAccount = accountService.save(AccountDTO.builder()
                .accountName("John Doe")
                .accountNumber("123456789")
                .accountType(AccountType.Saving.name())
                .status(Status.Pending.code())
                .build());
    }

    @BeforeEach
    void init() {
        databaseSettingsForTests.truncate();
    }

    @Test
    void shouldSaveNewTransaction() {
        //GIVEN
        setSavedAccount();
        TransactionDTO transactionDTO = TransactionDTO.builder()
                .transactionType(TransactionType.Deposit.name())
                .accountId(savedAccount.getAccountId())
                .amount(500)
                .currency(Currency.KMF.name())
                .reason("Just for test")
                .status(Status.Pending.code())
                .build();

        //WHEN
        TransactionDTO expected = transactionService.save(transactionDTO);

        //THEN
        assertThat(expected.getTransactionId()).isGreaterThan(0);
    }

    @Test
    void shouldRetrieveListOfAllSavedTransactions() {
        //GIVEN
        setSavedAccount();
        transactionService.save(
                TransactionDTO.builder()
                .transactionType(TransactionType.Deposit.name())
                .accountId(savedAccount.getAccountId())
                .amount(500)
                .currency(Currency.KMF.name())
                .reason("Just for test")
                .sourceType(SourceType.Online.name())
                .status(Status.Pending.code())
                .build());

        //WHEN
        List<TransactionDTO> expected = transactionService.findAll();

        //THEN
        assertThat(expected.size()).isEqualTo(1);
    }

    @Test
    void shouldFindTransactionById() {
        //GIVEN
        setSavedAccount();
        TransactionDTO savedTransaction = transactionService.save(TransactionDTO.builder()
                .transactionType(TransactionType.Deposit.name())
                .accountId(savedAccount.getAccountId())
                .amount(500)
                .sourceType(SourceType.Online.name())
                .currency(Currency.KMF.name())
                .reason("Just for test")
                .status(Status.Pending.code())
                .build());

        //WHEN
        TransactionDTO expected = transactionService.findById(savedTransaction.getTransactionId());

        //THEN
        assertThat(expected.getAmount()).isEqualTo(500);
        assertThat(expected.getAccountNumber()).isEqualTo(savedAccount.getAccountNumber());
    }

    @Test
    void shouldThrowATransactionNotFoundExceptionWhenTryFindTransactionByANotExistsId() {
        //GIVEN

        //WHEN
        Exception expected = assertThrows(TransactionNotFoundException.class,
                () -> transactionService.findById(-1));

        //THEN
        assertThat(expected.getMessage()).isEqualTo("Transaction not found");
    }
}
