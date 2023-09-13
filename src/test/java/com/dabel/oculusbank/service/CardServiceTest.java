package com.dabel.oculusbank.service;

import com.dabel.oculusbank.DatabaseSettingsForTests;
import com.dabel.oculusbank.constant.AccountType;
import com.dabel.oculusbank.constant.CardType;
import com.dabel.oculusbank.constant.Status;
import com.dabel.oculusbank.dto.AccountDTO;
import com.dabel.oculusbank.dto.CardDTO;
import com.dabel.oculusbank.exception.CardNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
class CardServiceTest {

    @Autowired
    CardService cardService;
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
                .accountType(AccountType.Saving.name())
                .status(Status.Pending.code())
                .build());
    }

    @Test
    void shouldSaveANewCard() {
        //GIVEN
        CardDTO cardDTO = CardDTO.builder()
                .accountId(savedAccount.getAccountId())
                .cardName("John Doe")
                .cardNumber("123456789")
                .cardType(CardType.Visa.name())
                .status(Status.Pending.code())
                .build();
        //WHEN
        CardDTO expected = cardService.save(cardDTO);

        //THEN
        assertThat(expected.getCardId()).isGreaterThan(0);
    }

    @Test
    void shouldFindByCardById() {
        //GIVEN
        CardDTO savedCard = cardService.save(
                CardDTO.builder()
                .accountId(savedAccount.getAccountId())
                .cardName("John Doe")
                .cardNumber("123456789")
                .cardType(CardType.Visa.name())
                .status(Status.Pending.code())
                .build());

        //WHEN
        CardDTO expected = cardService.findById(savedCard.getCardId());

        //THEN
        assertThat(expected.getStatus()).isEqualTo(Status.Pending.name());
        assertThat(expected.getAccountNumber()).isEqualTo(savedAccount.getAccountNumber());
    }

    @Test
    void shouldThrowCardNotFoundExceptionWhenTryFindCardByANotExistsCardId() {
        //GIVEN

        //WHEN
        Exception expected = assertThrows(CardNotFoundException.class,
                () -> cardService.findById(-1));

        //THEN
        assertThat(expected.getMessage()).isEqualTo("Card not found");
    }

    @Test
    void findByCardNumber() {
        //GIVEN
        CardDTO savedCard = cardService.save(
                CardDTO.builder()
                .accountId(savedAccount.getAccountId())
                .cardName("John Doe")
                .cardNumber("123456789")
                .cardType(CardType.Visa.name())
                .status(Status.Pending.code())
                .build());

        //WHEN
        CardDTO expected = cardService.findByCardNumber(savedCard.getCardNumber());

        //THEN
        assertThat(expected.getStatus()).isEqualTo(Status.Pending.name());
        assertThat(expected.getAccountNumber()).isEqualTo(savedAccount.getAccountNumber());
    }

    @Test
    void shouldThrowCardNotFoundExceptionWhenTryFindCardByANotExistsCardNumber() {
        //GIVEN

        //WHEN
        Exception expected = assertThrows(CardNotFoundException.class,
                () -> cardService.findByCardNumber("fake"));

        //THEN
        assertThat(expected.getMessage()).isEqualTo("Card not found");
    }

    @Test
    void findAllByAccountId() {
        //GIVEN
        cardService.save(
                CardDTO.builder()
                .accountId(savedAccount.getAccountId())
                .cardName("John Doe")
                .cardNumber("123456789")
                .cardType(CardType.Visa.name())
                .status(Status.Pending.code())
                .build());

        //WHEN
        List<CardDTO> expected = cardService.findAllByAccountId(savedAccount.getAccountId());

        //THEN
        assertThat(expected.size()).isEqualTo(1);
        assertThat(expected.get(0).getStatus()).isEqualTo(Status.Pending.name());
        assertThat(expected.get(0).getAccountNumber()).isEqualTo(savedAccount.getAccountNumber());
    }
}