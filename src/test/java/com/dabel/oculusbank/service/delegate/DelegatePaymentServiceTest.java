package com.dabel.oculusbank.service.delegate;

import com.dabel.oculusbank.DatabaseSettingsForTests;
import com.dabel.oculusbank.constant.AccountType;
import com.dabel.oculusbank.constant.Currency;
import com.dabel.oculusbank.constant.Status;
import com.dabel.oculusbank.dto.AccountDTO;
import com.dabel.oculusbank.dto.PaymentDTO;
import com.dabel.oculusbank.exception.BalanceInsufficientException;
import com.dabel.oculusbank.exception.IllegalOperationException;
import com.dabel.oculusbank.service.AccountService;
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
    private PaymentDTO paymentDTO;

    private void configSavedAccountsAndPaymentDTO(boolean isActiveAccount1, double balanceOfAccount1) {

        String account1Status = isActiveAccount1 ? Status.Active.code() : Status.Pending.code();

        savedAccount1 = accountService.save(AccountDTO.builder()
                .accountName("John Doe")
                .accountNumber("123456789")
                .accountType(AccountType.Saving.name())
                .currency(Currency.KMF.name())
                .balance(balanceOfAccount1)
                .status(account1Status)
                .build());

        savedAccount2 = accountService.save(AccountDTO.builder()
                .accountName("Tom Hunt")
                .accountNumber("987654321")
                .accountType(AccountType.Saving.name())
                .currency(Currency.KMF.name())
                .balance(300)
                .status(Status.Active.code())
                .build());

        paymentDTO = PaymentDTO.builder()
                .debitAccountNumber(savedAccount1.getAccountNumber())
                .creditAccountNumber(savedAccount2.getAccountNumber())
                .amount(50)
                .reason("Sample reason")
                .build();
    }

    @BeforeEach
    void init() {
        databaseSettingsForTests.truncate();
    }

    @Test
    void shouldInitPaymentAndPendingItBetweenTwoActiveAccounts() {
        //GIVEN
        configSavedAccountsAndPaymentDTO(true, 900);

        //WHEN
        PaymentDTO expected = delegatePaymentService.pay(paymentDTO);

        //THEN
        assertThat(expected.getPaymentId()).isGreaterThan(0);
        assertThat(expected.getStatus()).isEqualTo(Status.Pending.code());
    }

    @Test
    void shouldThrowAnIllegalOperationExceptionWhenTryInitPaymentWithAnInactiveAccount() {
        //GIVEN
        configSavedAccountsAndPaymentDTO(false, 900);

        //WHEN
        Exception expected = assertThrows(IllegalOperationException.class,
                () -> delegatePaymentService.pay(paymentDTO));

        //THEN
        assertThat(expected.getMessage()).isEqualTo("Debit and credit account must be active");
    }

    @Test
    void shouldThrowABalanceInsufficientExceptionWhenTryInitPaymentBetweenTwoActiveAccountsWithInsufficientBalance() {
        //GIVEN
        configSavedAccountsAndPaymentDTO(true, 500);

        //WHEN
        Exception expected = assertThrows(BalanceInsufficientException.class,
                () -> delegatePaymentService.pay(paymentDTO));

        //THEN
        assertThat(expected.getMessage()).isEqualTo("Account balance is insufficient");
    }

    @Test
    void shouldApprovePendingSavedPayment() {
        //GIVEN
        configSavedAccountsAndPaymentDTO(true, 900);
        PaymentDTO savedPayment = delegatePaymentService.pay(paymentDTO);

        //WHEN
        PaymentDTO expected = delegatePaymentService.approve(savedPayment.getPaymentId());

        //THEN
        assertThat(expected.getStatus()).isEqualTo(Status.Approved.code());

        AccountDTO expectedDebitAccount = accountService.findByNumber(savedAccount1.getAccountNumber());
        assertThat(expectedDebitAccount.getBalance()).isEqualTo(325); // Payment fees are 525KMF

        AccountDTO expectedCreditAccount = accountService.findByNumber(savedAccount2.getAccountNumber());
        assertThat(expectedCreditAccount.getBalance()).isEqualTo(350);
    }

    @Test
    void shouldRejectedPendingSavedPayment() {
        //GIVEN
        configSavedAccountsAndPaymentDTO(true, 900);
        PaymentDTO savedPayment = delegatePaymentService.pay(paymentDTO);

        //WHEN
        PaymentDTO expected = delegatePaymentService.reject(savedPayment.getPaymentId(), "Sample remark");

        //THEN
        assertThat(expected.getStatus()).isEqualTo(Status.Rejected.code());

        AccountDTO expectedDebitAccount = accountService.findByNumber(savedAccount1.getAccountNumber());
        assertThat(expectedDebitAccount.getBalance()).isEqualTo(900);

        AccountDTO expectedCreditAccount = accountService.findByNumber(savedAccount2.getAccountNumber());
        assertThat(expectedCreditAccount.getBalance()).isEqualTo(300);
    }


}