package com.dabel.oculusbank.service;

import com.dabel.oculusbank.DatabaseSettingsForTests;
import com.dabel.oculusbank.constant.AccountType;
import com.dabel.oculusbank.constant.CardType;
import com.dabel.oculusbank.constant.Status;
import com.dabel.oculusbank.dto.AccountDTO;
import com.dabel.oculusbank.dto.CardApplicationDTO;
import com.dabel.oculusbank.exception.CardAppRequestNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;


@SpringBootTest
class CardApplicationServiceTest {

    @Autowired
    CardApplicationService cardApplicationService;
    @Autowired
    AccountService accountService;
    @Autowired
    DatabaseSettingsForTests databaseSettingsForTests;

    @BeforeEach
    void init() {
        databaseSettingsForTests.truncate();
    }

    @Test
    void shouldSaveNewCardApplicationRequest() {
        //GIVEN
        AccountDTO savedAccount = accountService.save(AccountDTO.builder()
                .accountName("John Doe")
                .accountNumber("123456789")
                .accountType(AccountType.Saving.name())
                .status(Status.Pending.code())
                .build());

        CardApplicationDTO cardApplicationDTO = CardApplicationDTO.builder()
                .cardType(CardType.Visa.name())
                .accountId(savedAccount.getAccountId())
                .status(Status.Pending.code())
                .build();

        //WHEN
        CardApplicationDTO expected = cardApplicationService.save(cardApplicationDTO);

        //THEN
        assertThat(expected.getRequestId()).isGreaterThan(0);
    }

    @Test
    void shouldFindCardRequestApplicationByRequestId() {
        //GIVEN
        AccountDTO savedAccount = accountService.save(AccountDTO.builder()
                .accountName("John Doe")
                .accountNumber("123456789")
                .accountType(AccountType.Saving.name())
                .status(Status.Pending.code())
                .build());
        CardApplicationDTO cardApplicationDTO = CardApplicationDTO.builder()
                .cardType(CardType.Visa.name())
                .accountId(savedAccount.getAccountId())
                .status(Status.Pending.code())
                .build();
        CardApplicationDTO savedRequestApp = cardApplicationService.save(cardApplicationDTO);

        //WHEN
        CardApplicationDTO expected = cardApplicationService.findById(savedRequestApp.getRequestId());

        //THEN
        assertThat(expected.getAccountNumber()).isEqualTo(savedAccount.getAccountNumber());
        assertThat(expected.getAccountName()).isEqualTo(savedAccount.getAccountName());
        assertThat(expected.getStatus()).isEqualTo(Status.Pending.name());
    }

    @Test
    void shouldThrowAnCardAppNotFoundExceptionWhenTryFindCardRequestApplicationByANotExistsRequestId() {
        //GIVEN

        //WHEN
        Exception expected = assertThrows(CardAppRequestNotFoundException.class,
                () -> cardApplicationService.findById(-1));

        //THEN
        assertThat(expected.getMessage()).isEqualTo("Card application request not found");
    }
}