package com.dabel.oculusbank.service.delegate;

import com.dabel.oculusbank.DatabaseSettingsForTests;
import com.dabel.oculusbank.constant.Currency;
import com.dabel.oculusbank.constant.Status;
import com.dabel.oculusbank.dto.ExchangeDTO;
import com.dabel.oculusbank.exception.IllegalOperationException;
import com.dabel.oculusbank.service.delegate.DelegateExchangeService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
class DelegateExchangeServiceTest {

    @Autowired
    DelegateExchangeService delegateExchangeService;
    @Autowired
    DatabaseSettingsForTests databaseSettingsForTests;

    @BeforeEach
    void init() {
        databaseSettingsForTests.truncate();
    }

    @Test
    void shouldExchangeEur2Kmf() {
        //GIVEN
        ExchangeDTO exchangeDTO = ExchangeDTO.builder()
                .customerName("John Doe")
                .customerIdentity("NBE454532")
                .buyCurrency(Currency.EUR.name())
                .saleCurrency(Currency.KMF.name())
                .amount(20)
                .reason("Sample reason")
                .build();

        //WHEN
        ExchangeDTO expected = delegateExchangeService.exchange(exchangeDTO);

        //THEN
        //1£ = 490.31KMF => 20£ = 9806.2KMF | Note that this is a purchase
        assertThat(expected.getStatus()).isEqualTo(Status.Pending.code());
        assertThat(expected.getAmount()).isEqualTo(9806.2);
    }

    @Test
    void shouldExchangeKmf2Usd() {
        //GIVEN
        ExchangeDTO exchangeDTO = ExchangeDTO.builder()
                .customerName("John Doe")
                .customerIdentity("NBE454532")
                .buyCurrency(Currency.KMF.name())
                .saleCurrency(Currency.USD.name())
                .amount(10000)
                .reason("Sample reason")
                .build();

        //WHEN
        ExchangeDTO expected = delegateExchangeService.exchange(exchangeDTO);

        //THEN
        //1$ = 462.12KMF => 10 000KMF = 21.63$ | note that this is a sale
        assertThat(expected.getStatus()).isEqualTo(Status.Pending.code());
        assertThat(expected.getAmount()).isEqualTo(21.63);
    }

    @Test
    void shouldApprovePendingSavedExchange() {
        //GIVEN
        ExchangeDTO exchangeDTO = ExchangeDTO.builder()
                .customerName("John Doe")
                .customerIdentity("NBE454532")
                .buyCurrency(Currency.KMF.name())
                .saleCurrency(Currency.USD.name())
                .amount(10000)
                .reason("Sample reason")
                .build();
        ExchangeDTO savedExchange = delegateExchangeService.exchange(exchangeDTO);

        //WHEN
        ExchangeDTO expected = delegateExchangeService.approve(savedExchange.getExchangeId());

        //THEN
        //1$ = 462.12KMF => 10 000KMF = 21.63$ | note that this is a sale
        assertThat(expected.getStatus()).isEqualTo(Status.Approved.code());
        assertThat(expected.getAmount()).isEqualTo(21.63);
    }

    @Test
    void shouldRejectPendingSavedExchange() {
        //GIVEN
        ExchangeDTO exchangeDTO = ExchangeDTO.builder()
                .customerName("John Doe")
                .customerIdentity("NBE454532")
                .buyCurrency(Currency.KMF.name())
                .saleCurrency(Currency.USD.name())
                .amount(10000)
                .reason("Sample reason")
                .build();
        ExchangeDTO savedExchange = delegateExchangeService.exchange(exchangeDTO);

        //WHEN
        ExchangeDTO expected = delegateExchangeService.reject(savedExchange.getExchangeId(), "Sample remark");

        //THEN
        //1$ = 462.12KMF => 10 000KMF = 21.63$ | note that this is a sale
        assertThat(expected.getStatus()).isEqualTo(Status.Rejected.code());
        assertThat(expected.getFailureReason()).isEqualTo("Sample remark");
        assertThat(expected.getAmount()).isEqualTo(21.63);
    }

    @Test
    void shouldThrowAnIllegalOperationExceptionWhenTryMakeAnExchangeWithSameCurrencies() {
        //GIVEN
        ExchangeDTO exchangeDTO = ExchangeDTO.builder()
                .customerName("John Doe")
                .customerIdentity("NBE454532")
                .buyCurrency(Currency.KMF.name())
                .saleCurrency(Currency.KMF.name())
                .amount(20)
                .reason("Sample reason")
                .build();

        //WHEN
        Exception expected = assertThrows(IllegalOperationException.class,
                () -> delegateExchangeService.exchange(exchangeDTO));

        //THEN
        assertThat(expected.getMessage()).isEqualTo("Currencies must be different");
    }
}