package com.dabel.oculusbank.service;

import com.dabel.oculusbank.DatabaseSettingsForTests;
import com.dabel.oculusbank.constant.AccountType;
import com.dabel.oculusbank.constant.Status;
import com.dabel.oculusbank.dto.AccountDTO;
import com.dabel.oculusbank.dto.ChequeDTO;
import com.dabel.oculusbank.exception.ChequeNotFountException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
class ChequeServiceTest {

    @Autowired
    ChequeService chequeService;
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
    void shouldSaveNewCheque() {
        //GIVEN
        ChequeDTO chequeDTO = ChequeDTO.builder()
                .chequeName("John Doe")
                .chequeNumber("25687133213")
                .accountId(savedAccount.getAccountId())
                .status(Status.Active.code())
                .build();

        //WHEN
        ChequeDTO expected = chequeService.save(chequeDTO);

        //THEN
        assertThat(expected.getChequeId()).isGreaterThan(0);
    }

    @Test
    void shouldFindChequeByNumber() {
        //GIVEN
        ChequeDTO savedCheque = chequeService.save(
                ChequeDTO.builder()
                .chequeName("John Doe")
                .chequeNumber("25687133213")
                .accountId(savedAccount.getAccountId())
                .status(Status.Active.code())
                .build());

        //WHEN
        ChequeDTO expected = chequeService.findByNumber(savedCheque.getChequeNumber());

        //THEN
        assertThat(expected.getStatus()).isEqualTo(Status.Active.name());
    }

    @Test
    void shouldCheckTheExistenceOfAChequeByNumber() {
        //GIVEN
        ChequeDTO savedCheque = chequeService.save(
                ChequeDTO.builder()
                        .chequeName("John Doe")
                        .chequeNumber("25687133213")
                        .accountId(savedAccount.getAccountId())
                        .status(Status.Active.code())
                        .build());

        //WHEN
        boolean expectedTrue = chequeService.exists(savedCheque.getChequeNumber());
        boolean expectedFalse = chequeService.exists("fake number");

        //THEN
        assertTrue(expectedTrue);
        assertFalse(expectedFalse);
    }

    @Test
    void shouldThrowChequeNotFoundExceptionWhenTryFindChequeByANotExistsNumber() {
        //GIVEN
        chequeService.save(
                ChequeDTO.builder()
                        .chequeName("John Doe")
                        .chequeNumber("25687133213")
                        .accountId(savedAccount.getAccountId())
                        .status(Status.Active.code())
                        .build());

        //WHEN
        Exception expected = assertThrows(ChequeNotFountException.class,
                () -> chequeService.findByNumber("fake number"));

        //THEN
        assertThat(expected.getMessage()).isEqualTo("Cheque not found");
    }
}