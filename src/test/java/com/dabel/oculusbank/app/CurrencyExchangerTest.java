package com.dabel.oculusbank.app;

import com.dabel.oculusbank.constant.Currency;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class CurrencyExchangerTest {

    @Test
    void shouldExchangeEurToKmf() {
        double expected = CurrencyExchanger.exchange(Currency.EUR.name(), Currency.KMF.name(), 20);
        assertThat(expected).isEqualTo(9806.2);
    }

    @Test
    void shouldExchangeKmf2Eur() {
        double expected = CurrencyExchanger.exchange(Currency.KMF.name(), Currency.EUR.name(), 1500);
        assertThat(expected).isEqualTo(3.02);
    }

    @Test
    void shouldExchangeKmf2Usd() {
        double expected = CurrencyExchanger.exchange(Currency.KMF.name(), Currency.USD.name(), 1500);
        assertThat(expected).isEqualTo(3.24);
    }

    @Test
    void shouldExchangeUsd2Kmf() {
        double expected = CurrencyExchanger.exchange(Currency.USD.name(), Currency.KMF.name(), 20);
        assertThat(expected).isEqualTo(9130.2);
    }
}