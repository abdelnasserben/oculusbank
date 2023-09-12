package com.dabel.oculusbank.service;

import com.dabel.oculusbank.DatabaseSettingsForTests;
import com.dabel.oculusbank.constant.AccountType;
import com.dabel.oculusbank.constant.CardType;
import com.dabel.oculusbank.constant.Status;
import com.dabel.oculusbank.dto.AccountDTO;
import com.dabel.oculusbank.dto.CardDTO;
import com.dabel.oculusbank.service.delegate.DelegateCardService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class DelegateCardServiceTest {

    @Autowired
    DelegateCardService delegateCardService;
    @Autowired
    AccountService accountService;
    @Autowired
    DatabaseSettingsForTests databaseSettingsForTests;

    private AccountDTO savedAccount;

    @BeforeEach
    void init() {
        databaseSettingsForTests.truncate();
        savedAccount = accountService.save(
                AccountDTO.builder()
                .accountName("John Doe")
                .accountNumber("66398832015")
                .accountType(AccountType.Current.name())
                .status(Status.Pending.code())
                .build());
    }

    @Test
    void shouldSaveNewCardAndMakeItPending() {
        //GIVEN
        CardDTO cardDTO = CardDTO.builder()
                .accountId(savedAccount.getAccountId())
                .cardName("John Doe")
                .cardNumber("123456789")
                .cardType(CardType.Mastercard.name())
                .build();
        //WHEN
        CardDTO expected = delegateCardService.save(cardDTO);

        //THEN
        assertThat(expected.getCardId()).isGreaterThan(0);
        assertThat(expected.getStatus()).isEqualTo(Status.Pending.code());
    }

    @Test
    void shouldApprovePendingSavedCard() {
        //GIVEN
        CardDTO cardDTO = CardDTO.builder()
                .accountId(savedAccount.getAccountId())
                .cardName("John Doe")
                .cardNumber("123456789")
                .cardType(CardType.Mastercard.name())
                .build();
        CardDTO savedCard = delegateCardService.save(cardDTO);

        //WHEN
        CardDTO expected = delegateCardService.approve(savedCard.getCardId());

        //THEN
        assertThat(expected.getStatus()).isEqualTo(Status.Approved.code());
    }

    @Test
    void shouldRejectPendingSavedCard() {
        //GIVEN
        CardDTO cardDTO = CardDTO.builder()
                .accountId(savedAccount.getAccountId())
                .cardName("John Doe")
                .cardNumber("123456789")
                .cardType(CardType.Mastercard.name())
                .build();
        CardDTO savedCard = delegateCardService.save(cardDTO);

        //WHEN
        CardDTO expected = delegateCardService.reject(savedCard.getCardId(), "Sample remark");

        //THEN
        assertThat(expected.getStatus()).isEqualTo(Status.Rejected.code());
        assertThat(expected.getFailureReason()).isEqualTo("Sample remark");
    }
}