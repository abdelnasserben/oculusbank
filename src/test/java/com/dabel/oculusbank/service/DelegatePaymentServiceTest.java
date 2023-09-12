package com.dabel.oculusbank.service;

import com.dabel.oculusbank.DatabaseSettingsForTests;
import com.dabel.oculusbank.constant.AccountType;
import com.dabel.oculusbank.constant.Currency;
import com.dabel.oculusbank.constant.Status;
import com.dabel.oculusbank.dto.AccountDTO;
import com.dabel.oculusbank.dto.PaymentDTO;
import com.dabel.oculusbank.exception.BalanceInsufficientException;
import com.dabel.oculusbank.service.delegate.DelegatePaymentService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
class DelegatePaymentServiceTest {

    @Autowired
    DelegatePaymentService delegatePaymentService;
    @Autowired
    AccountService accountService;
    @Autowired
    DatabaseSettingsForTests databaseSettingsForTests;

    private AccountDTO savedAccount1, savedAccount2;

    @BeforeEach
    void init() {
        databaseSettingsForTests.truncate();
        savedAccount1 = accountService.save(
                AccountDTO.builder()
                .accountName("John Doe")
                .accountNumber("123456789")
                .accountType(AccountType.Current.name())
                .currency(Currency.KMF.name())
                .balance(500)
                .status(Status.Pending.code())
                .build());
        savedAccount2 = accountService.save(
                AccountDTO.builder()
                .accountName("Tom Hunt")
                .accountNumber("987654321")
                .accountType(AccountType.Current.name())
                .currency(Currency.KMF.name())
                .balance(300)
                .status(Status.Pending.code())
                .build());
    }

    @Test
    void shouldMakePayment() {
        //GIVEN
        String debitAccountNumber = savedAccount1.getAccountNumber();
        double amount = 50;
        String creditAccountNumber = savedAccount2.getAccountNumber();
        String reason = "Sample reason";

        //WHEN
        PaymentDTO expected = delegatePaymentService.pay(debitAccountNumber, amount, creditAccountNumber, reason);

        //THEN
        assertThat(expected.getPaymentId()).isGreaterThan(0);
        assertThat(expected.getStatus()).isEqualTo(Status.Pending.code());
    }

    @Test
    void shouldApprovePendingSavedPayment() {
        //GIVEN
        String debitAccountNumber = savedAccount1.getAccountNumber();
        double amount = 50;
        String creditAccountNumber = savedAccount2.getAccountNumber();
        String reason = "Sample reason";
        PaymentDTO savedPayment = delegatePaymentService.pay(debitAccountNumber, amount, creditAccountNumber, reason);

        //WHEN
        PaymentDTO expected = delegatePaymentService.approve(savedPayment.getPaymentId());

        //THEN
        assertThat(expected.getStatus()).isEqualTo(Status.Approved.code());

        AccountDTO expectedDebitAccount = accountService.findByNumber(savedAccount1.getAccountNumber());
        assertThat(expectedDebitAccount.getBalance()).isEqualTo(450);

        AccountDTO expectedCreditAccount = accountService.findByNumber(savedAccount2.getAccountNumber());
        assertThat(expectedCreditAccount.getBalance()).isEqualTo(350);
    }

    @Test
    void shouldRejectedPendingSavedPayment() {
        //GIVEN
        String debitAccountNumber = savedAccount1.getAccountNumber();
        double amount = 50;
        String creditAccountNumber = savedAccount2.getAccountNumber();
        String reason = "Sample reason";
        PaymentDTO savedPayment = delegatePaymentService.pay(debitAccountNumber, amount, creditAccountNumber, reason);

        //WHEN
        PaymentDTO expected = delegatePaymentService.reject(savedPayment.getPaymentId(), "Sample remark");

        //THEN
        assertThat(expected.getStatus()).isEqualTo(Status.Rejected.code());

        AccountDTO expectedDebitAccount = accountService.findByNumber(savedAccount1.getAccountNumber());
        assertThat(expectedDebitAccount.getBalance()).isEqualTo(500);

        AccountDTO expectedCreditAccount = accountService.findByNumber(savedAccount2.getAccountNumber());
        assertThat(expectedCreditAccount.getBalance()).isEqualTo(300);
    }

    @Test
    void shouldThrowBalanceInsufficientExceptionWhenTryMakePaymentWithInsufficientBalance() {
        //GIVEN
        String debitAccountNumber = savedAccount1.getAccountNumber();
        double amount = 600;
        String creditAccountNumber = savedAccount2.getAccountNumber();
        String reason = "Sample reason";

        //WHEN
        Exception expected = assertThrows(BalanceInsufficientException.class,
                () -> delegatePaymentService.pay(debitAccountNumber, amount, creditAccountNumber, reason));

        //THEN
        assertThat(expected.getMessage()).isEqualTo("Account balance is insufficient");
    }

}