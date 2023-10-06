package com.dabel.oculusbank.service;

import com.dabel.oculusbank.DatabaseSettingsForTests;
import com.dabel.oculusbank.app.util.Fee;
import com.dabel.oculusbank.constant.AccountType;
import com.dabel.oculusbank.constant.Status;
import com.dabel.oculusbank.dto.AccountDTO;
import com.dabel.oculusbank.dto.TransactionDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class FeeServiceTest {
    @Autowired
    FeeService feeService;
    @Autowired
    TransactionService transactionService;
    @Autowired
    AccountService accountService;
    @Autowired
    DatabaseSettingsForTests databaseSettingsForTests;

    @BeforeEach
    void init() {
        databaseSettingsForTests.truncate();
    }

    @Test
    void shouldApplyFeeOnAccountAndSaveTheTransaction() {
        //GIVEN
        AccountDTO savedAccount = accountService.save(AccountDTO.builder()
                .accountName("John Doe")
                .accountNumber("123456789")
                .accountType(AccountType.Saving.name())
                .status(Status.Active.code())
                .balance(1000)
                .build());

        //WHEN
        feeService.apply(savedAccount, new Fee(200, "withdraw"));
        TransactionDTO expectedTransaction = transactionService.findAll().get(0);
        AccountDTO expectedAccount = accountService.findByNumber(savedAccount.getAccountNumber());

        //THEN
        assertThat(expectedTransaction.getAccountId()).isEqualTo(savedAccount.getAccountId());
        assertThat(expectedTransaction.getAmount()).isEqualTo(200);
        assertThat(expectedAccount.getBalance()).isEqualTo(800);
    }
}