package com.dabel.oculusbank.service;

import com.dabel.oculusbank.DatabaseSettingsForTests;
import com.dabel.oculusbank.constant.AccountType;
import com.dabel.oculusbank.constant.Currency;
import com.dabel.oculusbank.constant.Status;
import com.dabel.oculusbank.dto.AccountDTO;
import com.dabel.oculusbank.dto.PaymentDTO;
import com.dabel.oculusbank.exception.TransactionNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
class PaymentServiceTest {

    @Autowired
    PaymentService paymentService;
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
                .accountType(AccountType.Saving.name())
                .currency(Currency.KMF.name())
                .balance(500)
                .status(Status.Pending.code())
                .build());
        savedAccount2 = accountService.save(
                AccountDTO.builder()
                .accountName("Tom Hunt")
                .accountNumber("987654321")
                .accountType(AccountType.Saving.name())
                .currency(Currency.KMF.name())
                .balance(300)
                .status(Status.Pending.code())
                .build());
    }

    @Test
    void shouldSaveNewPayment() {
        //GIVEN
        PaymentDTO paymentDTO = PaymentDTO.builder()
                .debitAccountId(savedAccount1.getAccountId())
                .creditAccountId(savedAccount2.getAccountId())
                .currency(Currency.KMF.name())
                .amount(250)
                .reason("Sample reason")
                .status(Status.Pending.code())
                .build();

        //WHEN
        PaymentDTO expected = paymentService.save(paymentDTO);

        //THEN
        assertThat(expected.getPaymentId()).isGreaterThan(0);
    }

    @Test
    void shouldRetrieveListOfAllPayments() {
        //GIVEN
        PaymentDTO paymentDTO = PaymentDTO.builder()
                .debitAccountId(savedAccount1.getAccountId())
                .creditAccountId(savedAccount2.getAccountId())
                .currency(Currency.KMF.name())
                .amount(250)
                .reason("Sample reason")
                .status(Status.Pending.code())
                .build();
        paymentService.save(paymentDTO);

        //WHEN
        List<PaymentDTO> expected = paymentService.findAll();

        //THEN
        assertThat(expected.size()).isEqualTo(1);
        assertThat(expected.get(0).getStatus()).isEqualTo(Status.Pending.name());
    }

    @Test
    void shouldFindAPaymentById() {
        //GIVEN
        PaymentDTO paymentDTO = PaymentDTO.builder()
                .debitAccountId(savedAccount1.getAccountId())
                .creditAccountId(savedAccount2.getAccountId())
                .currency(Currency.KMF.name())
                .amount(250)
                .reason("Sample reason")
                .status(Status.Pending.code())
                .build();
        PaymentDTO savedPayment = paymentService.save(paymentDTO);

        //WHEN
        PaymentDTO expected = paymentService.findById(savedPayment.getPaymentId());

        //THEN
        assertThat(expected.getDebitAccountNumber()).isEqualTo(savedAccount1.getAccountNumber());
        assertThat(expected.getStatus()).isEqualTo(Status.Pending.name());
    }

    @Test
    void shouldThrowATransactionNotFoundExceptionWhenTryFindAPayment() {
        //GIVEN

        //WHEN
        Exception excepted = assertThrows(TransactionNotFoundException.class,
                () -> paymentService.findById(-1));

        //THEN
        assertThat(excepted.getMessage()).isEqualTo("Payment not found");
    }

}