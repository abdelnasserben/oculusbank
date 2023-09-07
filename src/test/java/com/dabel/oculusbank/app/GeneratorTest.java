package com.dabel.oculusbank.app;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class GeneratorTest {

    @Test
    void shouldGenerateAccountNumberOfElevenCharacters() {
        String accountNumber = Generator.generateAccountNumber();
        assertThat(accountNumber.length()).isEqualTo(11);
    }
}