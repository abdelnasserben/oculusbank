package com.dabel.oculusbank.app;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class LoanCalculatorTest {

    @Test
    void shouldCalculateTotalAndPerMonthAmountDueOfALoan() {
        LoanCalculator loanCalculator = new LoanCalculator(9743, 2.17, 6);
        assertThat(loanCalculator.getTotalAmountDue()).isEqualTo(9954.42);
        assertThat(loanCalculator.getPerMonthAmountDue()).isEqualTo(1659.07);
    }

    @Test
    void shouldCalculateTotalAndPerMonthAmountDueOfAnotherLoan() {
        LoanCalculator loanCalculator = new LoanCalculator(15239, 1.24, 3);
        assertThat(loanCalculator.getTotalAmountDue()).isEqualTo(15427.96);
        assertThat(loanCalculator.getPerMonthAmountDue()).isEqualTo(5142.66);
    }
}