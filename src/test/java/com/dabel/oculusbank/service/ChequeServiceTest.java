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
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class ChequeServiceTest {

    @Autowired
    ChequeService chequeService;
    @Autowired
    AccountService accountService;
    @Autowired
    DatabaseSettingsForTests databaseSettingsForTests;

    private AccountDTO savedAccount;

    private void setSavedAccount() {
        savedAccount = accountService.save(AccountDTO.builder()
                .accountName("John Doe")
                .accountNumber("66398832015")
                .accountType(AccountType.Saving.name())
                .status(Status.Pending.code())
                .build());
    }

    @BeforeEach
    void init() {
        databaseSettingsForTests.truncate();
    }

    @Test
    void shouldSaveNewCheque() {
        //GIVEN
        setSavedAccount();
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
        setSavedAccount();
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
        assertThat(expected.getAccountNumber()).isEqualTo(savedAccount.getAccountNumber());
    }

    @Test
    void shouldCheckTheExistenceOfAChequeByNumber() {
        //GIVEN
        setSavedAccount();
        ChequeDTO savedCheque = chequeService.save(ChequeDTO.builder()
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

        //WHEN
        Exception expected = assertThrows(ChequeNotFountException.class,
                () -> chequeService.findByNumber("fake number"));

        //THEN
        assertThat(expected.getMessage()).isEqualTo("Cheque not found");
    }
}