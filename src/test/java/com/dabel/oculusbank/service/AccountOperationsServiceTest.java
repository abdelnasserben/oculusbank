package com.dabel.oculusbank.service;

import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class AccountOperationsServiceTest {
/*
    @Autowired
    AccountOperationService accountOperationsService;
    @Autowired
    AccountService accountService;
    @Autowired
    DatabaseSettingsForTests databaseSettingsForTests;

    private AccountDTO savedAccount;


    @BeforeEach
    void init() {
        databaseSettingsForTests.truncate();
        savedAccount = accountService.save(AccountDTO.builder()
                .accountName("John Doe")
                .accountNumber("123456789")
                .accountType(AccountType.Saving.name())
                .balance(1000)
                .status(Status.Pending.code())
                .build());
    }


    @Test
    void shouldDebitAnAccount() {
        //GIVEN

        //WHEN
        accountOperationsService.debit(savedAccount, 200);
        AccountDTO expected = accountService.findByNumber(savedAccount.getAccountNumber());

        //THEN
        assertThat(expected.getBalance()).isEqualTo(800);
    }

    @Test
    void shouldCreditAnAccount() {
        //GIVEN

        //WHEN
        accountOperationsService.credit(savedAccount, 200);
        AccountDTO expected = accountService.findByNumber(savedAccount.getAccountNumber());

        //THEN
        assertThat(expected.getBalance()).isEqualTo(1200);
    }

 */
}
