package com.dabel.oculusbank.service;

import com.dabel.oculusbank.DatabaseSettingsForTests;
import com.dabel.oculusbank.constant.Currency;
import com.dabel.oculusbank.constant.Status;
import com.dabel.oculusbank.dto.ExchangeDTO;
import com.dabel.oculusbank.exception.TransactionNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
class ExchangeServiceTest {

    @Autowired
    ExchangeService exchangeService;
    @Autowired
    DatabaseSettingsForTests databaseSettingsForTests;


    @BeforeEach
    void init() {
        databaseSettingsForTests.truncate();
    }

    @Test
    void shouldSaveAnExchange() {
        //GIVEN
        ExchangeDTO exchangeDTO = ExchangeDTO.builder()
                .customerName("John Doe")
                .customerIdentity("NBE454532")
                .status(Status.Pending.code())
                .amount(500)
                .buyCurrency(Currency.KMF.name())
                .saleCurrency(Currency.EUR.name())
                .reason("Sample reason")
                .build();

        //WHEN
        ExchangeDTO expected = exchangeService.save(exchangeDTO);

        //THEN
        assertThat(expected.getExchangeId()).isGreaterThan(0);
    }

    @Test
    void shouldRetrieveListOfAllSavedExchanges() {
        //GIVEN
        ExchangeDTO exchangeDTO = ExchangeDTO.builder()
                .customerName("John Doe")
                .customerIdentity("NBE454532")
                .status(Status.Pending.code())
                .amount(500)
                .buyCurrency(Currency.KMF.name())
                .saleCurrency(Currency.EUR.name())
                .reason("Sample reason")
                .build();
        exchangeService.save(exchangeDTO);

        //WHEN
        List<ExchangeDTO> expected = exchangeService.findAll();

        //THEN
        assertThat(expected.size()).isEqualTo(1);
        assertThat(expected.get(0).getCustomerIdentity()).isEqualTo("NBE454532");
        assertThat(expected.get(0).getStatus()).isEqualTo(Status.Pending.name());
    }

    @Test
    void shouldFindAnExchangeById() {
        //GIVEN
        ExchangeDTO exchangeDTO = ExchangeDTO.builder()
                .customerName("John Doe")
                .customerIdentity("NBE454532")
                .status(Status.Pending.code())
                .amount(500)
                .buyCurrency(Currency.KMF.name())
                .saleCurrency(Currency.EUR.name())
                .reason("Sample reason")
                .build();
        ExchangeDTO savedExchange = exchangeService.save(exchangeDTO);

        //WHEN
        ExchangeDTO expected = exchangeService.findById(savedExchange.getExchangeId());

        //THEN
        assertThat(expected.getAmount()).isEqualTo(500);
        assertThat(expected.getStatus()).isEqualTo(Status.Pending.name());
    }

    @Test
    void shouldThrowATransactionNotFoundExceptionWhenTryFindAnExchangeByANotExistsId() {
        //GIVEN

        //WHEN
        Exception expected = assertThrows(TransactionNotFoundException.class,
                () -> exchangeService.findById(-1));

        //THEN
        assertThat(expected.getMessage()).isEqualTo("Exchange not found");
    }
}