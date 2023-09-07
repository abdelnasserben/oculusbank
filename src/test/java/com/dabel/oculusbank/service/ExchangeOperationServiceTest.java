package com.dabel.oculusbank.service;

import com.dabel.oculusbank.DatabaseSettingsForTests;
import com.dabel.oculusbank.constant.Currency;
import com.dabel.oculusbank.constant.Status;
import com.dabel.oculusbank.dto.ExchangeDTO;
import com.dabel.oculusbank.exception.IllegalTransactionException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
class ExchangeOperationServiceTest {

    @Autowired
    ExchangeOperationService exchangeOperationService;
    @Autowired
    DatabaseSettingsForTests databaseSettingsForTests;

    @BeforeEach
    void init() {
        databaseSettingsForTests.truncate();
    }

    @Test
    void shouldExchangeEur2Kmf() {
        //GIVEN
        String customerName = "John Doe", customerIdentity = "NBE454532", buyCurrency = Currency.EUR.name(),
                saleCurrency = Currency.KMF.name(), reason = "Sample reason";
        double amount = 20;

        //WHEN
        ExchangeDTO expected = exchangeOperationService.exchange(customerName, customerIdentity, buyCurrency, saleCurrency, amount, reason);

        //THEN
        //1£ = 490.31KMF => 20£ = 9806.2KMF | Note that this is a purchase
        assertThat(expected.getStatus()).isEqualTo(Status.Pending.code());
        assertThat(expected.getAmount()).isEqualTo(9806.2);
    }

    @Test
    void shouldExchangeKmf2Usd() {
        //GIVEN
        String customerName = "John Doe", customerIdentity = "NBE454532", buyCurrency = Currency.KMF.name(),
                saleCurrency = Currency.USD.name(), reason = "Sample reason";
        double amount = 10_000;

        //WHEN
        ExchangeDTO expected = exchangeOperationService.exchange(customerName, customerIdentity, buyCurrency, saleCurrency, amount, reason);

        //THEN
        //1$ = 462.12KMF => 10 000KMF = 21.63$ | note that this is a sale
        assertThat(expected.getStatus()).isEqualTo(Status.Pending.code());
        assertThat(expected.getAmount()).isEqualTo(21.63);
    }

    @Test
    void shouldApprovePendingSavedExchange() {
        //GIVEN
        String customerName = "John Doe", customerIdentity = "NBE454532", buyCurrency = Currency.KMF.name(),
                saleCurrency = Currency.USD.name(), reason = "Sample reason";
        double amount = 10_000;
        ExchangeDTO savedExchange = exchangeOperationService.exchange(customerName, customerIdentity, buyCurrency, saleCurrency, amount, reason);

        //WHEN
        ExchangeDTO expected = exchangeOperationService.approve(savedExchange.getExchangeId());

        //THEN
        //1$ = 462.12KMF => 10 000KMF = 21.63$ | note that this is a sale
        assertThat(expected.getStatus()).isEqualTo(Status.Approved.code());
        assertThat(expected.getAmount()).isEqualTo(21.63);
    }

    @Test
    void shouldRejectPendingSavedExchange() {
        //GIVEN
        String customerName = "John Doe", customerIdentity = "NBE454532", buyCurrency = Currency.KMF.name(),
                saleCurrency = Currency.USD.name(), reason = "Sample reason";
        double amount = 10_000;
        ExchangeDTO savedExchange = exchangeOperationService.exchange(customerName, customerIdentity, buyCurrency, saleCurrency, amount, reason);

        //WHEN
        ExchangeDTO expected = exchangeOperationService.reject(savedExchange.getExchangeId(), "Sample remark");

        //THEN
        //1$ = 462.12KMF => 10 000KMF = 21.63$ | note that this is a sale
        assertThat(expected.getStatus()).isEqualTo(Status.Rejected.code());
        assertThat(expected.getFailureReason()).isEqualTo("Sample remark");
        assertThat(expected.getAmount()).isEqualTo(21.63);
    }

    @Test
    void shouldThrowAnIllegalTransactionExceptionWhenTryMakeAnExchangeWithSameCurrencies() {
        //GIVEN
        String customerName = "John Doe", customerIdentity = "NBE454532", buyCurrency = Currency.KMF.name(),
                saleCurrency = Currency.KMF.name(), reason = "Sample reason";
        double amount = 20;

        //WHEN
        Exception expected = assertThrows(IllegalTransactionException.class,
                () -> exchangeOperationService.exchange(customerName, customerIdentity, buyCurrency, saleCurrency, amount, reason));

        //THEN
        assertThat(expected.getMessage()).isEqualTo("Currencies must be different");
    }
}