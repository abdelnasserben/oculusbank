package com.dabel.oculusbank.app;

import com.dabel.oculusbank.app.util.AccountNumberGenerator;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class GeneratorTest {

    @Test
    void shouldGenerateAccountNumberOfElevenCharacters() {
        String accountNumber = AccountNumberGenerator.generate();
        assertThat(accountNumber.length()).isEqualTo(11);
    }
}