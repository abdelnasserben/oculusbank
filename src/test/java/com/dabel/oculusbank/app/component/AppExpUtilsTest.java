package com.dabel.oculusbank.app.component;

import com.dabel.oculusbank.constant.*;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class AppExpUtilsTest {

    @Test
    void shouldRetryWarningColorFromPendingStatus() {
        assertThat(new AppExpUtils().statusColor(Status.PENDING.name())).isEqualTo("warning");
    }

    @Test
    void shouldRetrySuccessColorFromActiveStatus() {
        assertThat(new AppExpUtils().statusColor(Status.ACTIVE.name())).isEqualTo("success");
    }

    @Test
    void shouldRetrySuccessFromApprovedStatus() {
        assertThat(new AppExpUtils().statusColor(Status.APPROVED.name())).isEqualTo("success");
    }

    @Test
    void shouldRetryDangerFromFailedStatus() {
        assertThat(new AppExpUtils().statusColor(Status.FAILED.name())).isEqualTo("danger");
    }

    @Test
    void shouldRetryAfghanistanNation() {
        assertThat(new AppExpUtils().countries()[0]).isEqualTo(Country.AF.getName());
    }

    @Test
    void shouldRetryUsdCurrency() {
        assertThat(new AppExpUtils().currencies()[2]).isEqualTo(Currency.USD.name());
    }

    @Test
    void shouldRetryVisaCardType() {
        assertThat(new AppExpUtils().cardTypes()[0]).isEqualTo(CardType.VISA.name());
    }

    @Test
    void shouldRetryGoldLoanType() {
        assertThat(new AppExpUtils().loanTypes()[0]).isEqualTo(LoanType.GOLD.name());
    }

    @Test
    void shouldRetryDepositTransactionType() {
        assertThat(new AppExpUtils().transactionTypes()[0]).isEqualTo(TransactionType.DEPOSIT.name());
    }
}